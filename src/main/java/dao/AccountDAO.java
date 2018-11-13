package dao;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import model.Account;
import model.Customer;

@Singleton
public class AccountDAO {
	private final ConcurrentMap<String, Account> accountMap;
	
	public AccountDAO() {
		accountMap = new ConcurrentHashMap<String, Account>();
	}
	
	public Account getAccount(String accountId) {
		if (accountId == null)
			return null;
		return accountMap.get(accountId);
	}
	
	public Account saveAccount(Account account) {
		if (account.getId() == null) {
			Long nextId = Math.round(Math.random() * 100000);
			while (accountMap.containsKey(nextId.toString())) {
				nextId = Math.round(Math.random() * 100000);
			}

			account.setId(nextId.toString());			
		}
		// If we get a non null value that means the account already exists in the Map.
        if (null != accountMap.putIfAbsent(account.getId(), account)) {
            return null;
        }
        return account;
	}
	
	@SuppressWarnings("unchecked")
	public List<Account> listAccounts(Customer customer) {
		if (customer == null || customer.getId() == null)
			return Collections.EMPTY_LIST;
		
		return accountMap.values().stream()
				.filter(a -> a.getCustomer().getId().equals(customer.getId()))
				.collect(Collectors.toList());
	}
	
	public List<Account> listAccounts() {
		return accountMap.values().stream()
				.sorted( Comparator.comparing(Account::getId))
				.collect(Collectors.toList());
	}
}
