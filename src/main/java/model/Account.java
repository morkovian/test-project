package model;

import java.util.Date;

/**
 * 
 * @author vchernou
 *
 */
public class Account {

	public enum AccountStatus {OPEN, CLOSED, FROZEN}; 
	
	private String id;
	private Customer customer;
	private Currency currency;

	private Date dateOpened;
	private Date dateClosed;
	
	private AccountStatus status;
	
	private Float balance = 0f;
	
	public Account(Customer customer, Currency currency) {
		super();
		this.customer = customer;
		this.currency = currency;
		this.dateOpened = new Date();
		this.status = AccountStatus.OPEN;
	}

	public Date getDateClosed() {
		return dateClosed;
	}

	public void setDateClosed(Date dateClosed) {
		this.dateClosed = dateClosed;
	}

	public AccountStatus getStatus() {
		return status;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
	}

	public Float getBalance() {
		return balance;
	}

	public void setBalance(Float balance) {
		this.balance = balance;
	}

	public String getId() {
		return id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public Currency getCurrency() {
		return currency;
	}

	public Date getDateOpened() {
		return dateOpened;
	}

	public void setId(String id) {
		this.id = id;
	}

	
}
