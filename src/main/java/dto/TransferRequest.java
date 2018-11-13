package dto;
/**
 * Used as request parameter to Money Transfer Servlet
 * @author vchernou
 *
 */
public class TransferRequest {
	private String account;
	private Float amount;
	private String currency;
	private String description;

	private String originBank;
	private String originAccount;
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
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
	public String getOriginBank() {
		return originBank;
	}
	public void setOriginBank(String originBank) {
		this.originBank = originBank;
	}
	public String getOriginAccount() {
		return originAccount;
	}
	public void setOriginAccount(String originAccount) {
		this.originAccount = originAccount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String originCurrency) {
		this.currency = originCurrency;
	}
	
}
