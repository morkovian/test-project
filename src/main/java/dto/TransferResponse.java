package dto;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@XmlRootElement
public class TransferResponse {
	
	private boolean successStatus;
	private String errorMessage;
	private Float newAccountBalance;


	public TransferResponse() {
		super();
	}
	//success
	public TransferResponse(Float newAccountBalance) {
		super();
		this.successStatus = true;
		this.newAccountBalance = newAccountBalance;
	}
	
	//failure
	public TransferResponse(String errorMessage) {
		super();
		this.successStatus = false;
		this.errorMessage = errorMessage;
	}

	public boolean isSuccessStatus() {
		return successStatus;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public Float getNewAccountBalance() {
		return newAccountBalance;
	}

}
