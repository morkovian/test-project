package service;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.AccountDAO;
import dao.CustomerDAO;
import dao.TransactionDAO;
import dto.TransferRequest;
import dto.TransferResponse;
import model.Account;
import model.Currency;
import model.Customer;
import model.Transaction;

public class MoneyTransferService {

	public static final String MY_BANK_ID = "REVOLUT";
	
	private final AccountDAO accountDAO;
	private final TransactionDAO transactionDAO;
	private final CurrencyExchangeService currencyExchangeService;
	
	private MoneyTransferService(AccountDAO accountDAO, TransactionDAO transactionDAO,
			CurrencyExchangeService currencyExchangeService) {
		super();
		this.accountDAO = accountDAO;
		this.transactionDAO = transactionDAO;
		this.currencyExchangeService = currencyExchangeService;
	}
	
	
	public TransferResponse transfer(TransferRequest request) {
		
		//first validate receiving account
		String accountId = request.getAccount();
		Account account = accountDAO.getAccount(accountId);
		if (account == null || account.getStatus() != Account.AccountStatus.OPEN)
			return new TransferResponse("Account not found");
		
		if (request.getAccount() == null || request.getAmount().compareTo(new BigDecimal(0)) < 0)
			return new TransferResponse("Amount has to be greater than 0");

		if (request.getCurrency() == null || Currency.fromString(request.getCurrency()) == null)
			return new TransferResponse("Currency is missing");
		
		Transaction debitTx = new Transaction();
		debitTx.setAccount(account);
		debitTx.setTransactionType(Transaction.TransactionType.DEBIT);
		debitTx.setDescription(request.getDescription());
		
		String originAccountId = request.getOriginAccount();
		debitTx.setOriginAccountId(originAccountId);
		debitTx.setOriginBankId(request.getOriginBank());
		debitTx.setOriginCurrency(Currency.fromString(request.getCurrency()));
		
		if (debitTx.getOriginCurrency() != account.getCurrency())
			debitTx.setAmount(currencyExchangeService.exchange(request.getAmount(), debitTx.getOriginCurrency(), account.getCurrency()));
		else
			debitTx.setAmount(request.getAmount());
		
		//if bank is ours, then validate originAccountId as well
		if (MY_BANK_ID.equals(request.getOriginBank())) {
			Account originAccount = accountDAO.getAccount(originAccountId);
			if (originAccount == null || originAccount.getStatus() != Account.AccountStatus.OPEN)
				return new TransferResponse("From Account not found");
			
			Transaction creditTx = new Transaction();
			creditTx.setAccount(originAccount);
			creditTx.setTransactionType(Transaction.TransactionType.CREDIT);
			creditTx.setDescription(request.getDescription());
			creditTx.setAmount(request.getAmount());

			synchronized (originAccount) {
				if (originAccount.getBalance().compareTo(request.getAmount()) < 0)
					return new TransferResponse("Insufficient funds in the From Account");
				transactionDAO.saveTransaction(creditTx);
				calculateAccountBalance(originAccount);
			}
		}
		
		synchronized (account) {
			transactionDAO.saveTransaction(debitTx);
			return new TransferResponse(calculateAccountBalance(account));
		}
	}
	
	
	private BigDecimal calculateAccountBalance(Account account) {
		List<Transaction> txs = transactionDAO.listTransactions(account);
		BigDecimal balance = new BigDecimal(0);
		for (Transaction transaction : txs) {
			if (transaction.getTransactionType() == Transaction.TransactionType.DEBIT)
				balance = balance.add(transaction.getAmount());
			else
				balance = balance.subtract(transaction.getAmount());
		}
		account.setBalance(balance);
		accountDAO.saveAccount(account);
		return balance;
	}
	
	public String getDebugInfo() throws Exception {
		StringBuffer result = new StringBuffer();
		ObjectMapper mapper = new ObjectMapper();
		List<Account> accounts = accountDAO.listAccounts();
		for (Account account : accounts) {
			result.append(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(account)).append("\n");
		}
		return result.toString();
	}
	
	private static MoneyTransferService instance;
	public synchronized static MoneyTransferService getInstance() {
		if (instance == null) {
			AccountDAO accountDAO = new AccountDAO();
			TransactionDAO transactionDAO = new TransactionDAO();
			CurrencyExchangeService currencyExchangeService = new CurrencyExchangeService();
			
			instance = new MoneyTransferService(accountDAO, transactionDAO, currencyExchangeService);
			
			//setup test data
			CustomerDAO customerDAO = new CustomerDAO();
			Customer ivanov = new Customer("Ivan", "Ivanovich", "Ivanov");
			ivanov.setId("A");
			customerDAO.saveCustomer(ivanov);
			
			Account account = new Account(ivanov, Currency.USD);
			account.setId("1");
			accountDAO.saveAccount(account);
			account = new Account(ivanov, Currency.EURO);
			account.setId("2");
			accountDAO.saveAccount(account);
			
			
			
			
			Customer petrov = new Customer("Petr", "Petrovich", "Petrov");
			petrov.setId("B");
			customerDAO.saveCustomer(petrov);
	
			account = new Account(petrov, Currency.GBP);
			account.setId("3");
			accountDAO.saveAccount(account);
			account = new Account(petrov, Currency.EURO);
			account.setId("4");
			accountDAO.saveAccount(account);
		} 
		
		return instance;
	}
}
