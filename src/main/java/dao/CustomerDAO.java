package dao;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import model.Customer;

public class CustomerDAO {
	private final ConcurrentMap<String, Customer> customerMap;
	
	public CustomerDAO() {
		customerMap = new ConcurrentHashMap<String, Customer>();
	}
	
	public Customer getCustomer(String CustomerId) {
		return customerMap.get(CustomerId);
	}
	
	public Customer saveCustomer(Customer customer) {
		if (customer.getId() == null) {
			Long nextId = Math.round(Math.random() * 100000);
			while (customerMap.containsKey(nextId.toString())) {
				nextId = Math.round(Math.random() * 100000);
			}

			customer.setId(nextId.toString());			
		}
        customerMap.put(customer.getId(), customer);
        return customer;
	}
	
	public List<Customer> listCustomers() {
		return customerMap.values().stream()
				.sorted(Comparator.comparing( Customer::getLastName ))
				.collect(Collectors.toList());
	}
}
