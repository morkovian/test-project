package model;

/**
 * Describes bank's customer
 * 
 * @author vchernou
 */
public class Customer {


	public enum CustomerStatus {ACTIVE, INACTIVE}; 
	
	private String id;
	private String firstName;
	private String middleName;
	private String lastName;
	
	private CustomerStatus status;
	
	public Customer(String firstName, String middleName, String lastName) {
		super();
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.status = CustomerStatus.ACTIVE;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public CustomerStatus getStatus() {
		return status;
	}

	public void setStatus(CustomerStatus status) {
		this.status = status;
	}
}
