package api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;

import com.fasterxml.jackson.databind.ObjectMapper;

import dto.TransferRequest;
import dto.TransferResponse;
import service.MoneyTransferService;

@Path("moneytransfer")
public class MoneyTransferRestAPI {

	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public TransferResponse postTransfer(TransferRequest transferRequest) throws Exception {
    	TransferResponse transferResponse = MoneyTransferService.getInstance().transfer(transferRequest);
		return transferResponse;
	}
	
	 
	@GET
	@Produces({MediaType.TEXT_PLAIN})
	public String getTransfer(@Context Request request) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		return "please use POST request with following JSON data: \n"+
				mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new TransferRequest());
	}

	
	//TODO should be disabled when in production
	@GET
	@Path("debug")
	@Produces({MediaType.TEXT_PLAIN})
	
	public String getDebugInfo(@Context Request request) throws Exception {
		return MoneyTransferService.getInstance().getDebugInfo();
	}
	
	
	public MoneyTransferRestAPI() {
		

	}
}
