package dao;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import model.Account;
import model.Transaction;

public class TransactionDAO {
	private final ConcurrentMap<String, Transaction> transactionMap;
	
	public TransactionDAO() {
		transactionMap = new ConcurrentHashMap<String, Transaction>();
	}
	
	public Transaction getTransaction(String transactionId) {
		if (transactionId == null)
			return null;
		return transactionMap.get(transactionId);
	}
	
	public Transaction saveTransaction(Transaction tx) {
		if (tx.getId() == null) {
			Long nextId = Math.round(Math.random() * 100000000);
			while (transactionMap.containsKey(nextId.toString())) {
				nextId = Math.round(Math.random() * 100000000);
			}

			tx.setId(nextId.toString());			
		}
		transactionMap.put(tx.getId(), tx);
        return tx;
	}
	
	@SuppressWarnings("unchecked")
	public List<Transaction> listTransactions(Account account) {
		if (account == null || account.getId() == null)
			return Collections.EMPTY_LIST;
		
		return transactionMap.values().stream()
				.filter(tx -> tx.getAccount().getId().equals(account.getId()))
				.sorted(Comparator.comparing(Transaction::getTransactionDate))
				.collect(Collectors.toList());
	}
}
