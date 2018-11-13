package model;

import java.util.Date;

public class Transaction {
	public enum TransactionType {DEBIT, CREDIT};
	
	private String id;
	private TransactionType transactionType;
	private Date transactionDate;
	
	private Account account;
	private Float amount;
	private String description;

	private String originBankId;
	private String originAccountId;
	private Float originAmount;
	private Currency originCurrency;
	
	public Transaction() {
		this.transactionDate = new Date();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public TransactionType getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}
	public Date getTransactionDate() {
		return transactionDate;
	}

	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public Float getAmount() {
		return amount;
	}
	public void setAmount(Float amount) {
		this.amount = amount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOriginBankId() {
		return originBankId;
	}
	public void setOriginBankId(String originBankId) {
		this.originBankId = originBankId;
	}
	public String getOriginAccountId() {
		return originAccountId;
	}
	public void setOriginAccountId(String originAccountId) {
		this.originAccountId = originAccountId;
	}
	public Float getOriginAmount() {
		return originAmount;
	}
	public void setOriginAmount(Float originAmount) {
		this.originAmount = originAmount;
	}
	public Currency getOriginCurrency() {
		return originCurrency;
	}
	public void setOriginCurrency(Currency originCurrency) {
		this.originCurrency = originCurrency;
	}
}
