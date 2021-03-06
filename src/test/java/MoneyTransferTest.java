import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import api.MoneyTransferRestAPI;
import dto.TransferRequest;
import dto.TransferResponse;
import model.Currency;
import service.CurrencyExchangeService;


public class MoneyTransferTest extends JerseyTest {

    @Override
    public Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return new ResourceConfig(MoneyTransferRestAPI.class);
    }


    @Test
    public void testCashDeposit() throws Exception {
        TransferRequest request = new TransferRequest();
        request.setAccount("1");
        request.setAmount(new BigDecimal(100));
        request.setCurrency("USD");
        request.setOriginBank("ALFA");
        request.setOriginAccount("12345");
        request.setDescription("Deposit cash through ATM");

        Response output = target("/moneytransfer")
                .request()
                .post(Entity.entity(request, MediaType.APPLICATION_JSON));

        assertEquals("Should return status 200", 200, output.getStatus());
        assertNotNull("Should return transfer response", output.getEntity());
        ObjectMapper mapper = new ObjectMapper();
        TransferResponse response = mapper.readValue(output.readEntity(String.class), TransferResponse.class);
        assertEquals("Balance should be right", new BigDecimal(100f), response.getNewAccountBalance());
    }

    
    @Test
    public void testCashDepositGBPintoEUROS() throws Exception {
        TransferRequest request = new TransferRequest();
        request.setAccount("2");
        request.setAmount(new BigDecimal(100));
        request.setCurrency("GBP");
        request.setOriginBank("ALFA");
        request.setOriginAccount("12345");
        request.setDescription("Deposit cash through ATM");

        Response output = target("/moneytransfer")
                .request()
                .post(Entity.entity(request, MediaType.APPLICATION_JSON));

        assertEquals("Should return status 200", 200, output.getStatus());
        assertNotNull("Should return transfer response", output.getEntity());
        ObjectMapper mapper = new ObjectMapper();
        TransferResponse response = mapper.readValue(output.readEntity(String.class), TransferResponse.class);
        CurrencyExchangeService exService = new CurrencyExchangeService();
        BigDecimal expected = exService.exchange(request.getAmount(), Currency.GBP, Currency.EURO);
        assertEquals("Balance should be right", expected, response.getNewAccountBalance());
    }

    @Test
    public void testInternalTransfer() throws Exception {
    	//first deposit some money
        TransferRequest request = new TransferRequest();
        request.setAccount("1");
        request.setAmount(new BigDecimal(100));
        request.setCurrency("USD");
        request.setOriginBank("ALFA");
        request.setOriginAccount("12345");
        request.setDescription("Deposit cash through ATM");

        Response output = target("/moneytransfer")
                .request()
                .post(Entity.entity(request, MediaType.APPLICATION_JSON));

        assertEquals("Should return status 200", 200, output.getStatus());
        
        //now transfer it to another account
        request = new TransferRequest();
        request.setAccount("3");
        request.setAmount(new BigDecimal(100));
        request.setCurrency("USD");
        request.setOriginBank("REVOLUT");
        request.setOriginAccount("1");
        request.setDescription("Transfer from my USD to friend's GBP account");

        output = target("/moneytransfer")
                .request()
                .post(Entity.entity(request, MediaType.APPLICATION_JSON));

        assertEquals("Should return status 200", 200, output.getStatus());
        assertNotNull("Should return transfer response", output.getEntity());
        ObjectMapper mapper = new ObjectMapper();
        TransferResponse response = mapper.readValue(output.readEntity(String.class), TransferResponse.class);
        CurrencyExchangeService exService = new CurrencyExchangeService();
        BigDecimal expected = exService.exchange(request.getAmount(), Currency.USD, Currency.GBP);
        assertEquals("Balance should be right", expected, response.getNewAccountBalance());
        
        target("/moneytransfer/debug").request().get();
    }
    
    @Test
    public void testInsufficientFundsTransferError() throws Exception {
    	//first deposit some money
        TransferRequest request = new TransferRequest();
        request.setAccount("1");
        request.setAmount(new BigDecimal(100));
        request.setCurrency("USD");
        request.setOriginBank("ALFA");
        request.setOriginAccount("12345");
        request.setDescription("Deposit cash through ATM");

        Response output = target("/moneytransfer")
                .request()
                .post(Entity.entity(request, MediaType.APPLICATION_JSON));

        assertEquals("Should return status 200", 200, output.getStatus());
        
        //now transfer it to another account
        request = new TransferRequest();
        request.setAccount("3");
        request.setAmount(new BigDecimal(300));
        request.setCurrency("USD");
        request.setOriginBank("REVOLUT");
        request.setOriginAccount("1");

        output = target("/moneytransfer")
                .request()
                .post(Entity.entity(request, MediaType.APPLICATION_JSON));

        assertEquals("Should return status 200", 200, output.getStatus());
        assertNotNull("Should return transfer response", output.getEntity());
        ObjectMapper mapper = new ObjectMapper();
        TransferResponse response = mapper.readValue(output.readEntity(String.class), TransferResponse.class);
        assertEquals("Insufficient fund message", "Insufficient funds in the From Account", response.getErrorMessage());
    }
    
    @Test
    public void testFromAccountNotFoundError() throws Exception {
    	TransferRequest request = new TransferRequest();
        request.setAccount("3");
        request.setAmount(new BigDecimal(300));
        request.setCurrency("USD");
        request.setOriginBank("REVOLUT");
        request.setOriginAccount("1NOTEXISTENT");

        Response output = target("/moneytransfer")
                .request()
                .post(Entity.entity(request, MediaType.APPLICATION_JSON));

        assertEquals("Should return status 200", 200, output.getStatus());
        assertNotNull("Should return transfer response", output.getEntity());
        ObjectMapper mapper = new ObjectMapper();
        TransferResponse response = mapper.readValue(output.readEntity(String.class), TransferResponse.class);
        assertEquals("From Account not found message", "From Account not found", response.getErrorMessage());
    }
    
    
    @Test
    public void testAccountNotFoundError() throws Exception {
    	TransferRequest request = new TransferRequest();
        request.setAccount("3NONEXISTENT");
        request.setAmount(new BigDecimal(300));
        request.setCurrency("USD");
        request.setOriginBank("REVOLUT");
        request.setOriginAccount("1");

        Response output = target("/moneytransfer")
                .request()
                .post(Entity.entity(request, MediaType.APPLICATION_JSON));

        assertEquals("Should return status 200", 200, output.getStatus());
        assertNotNull("Should return transfer response", output.getEntity());
        ObjectMapper mapper = new ObjectMapper();
        TransferResponse response = mapper.readValue(output.readEntity(String.class), TransferResponse.class);
        assertEquals("Account not found message", "Account not found", response.getErrorMessage());
    }
    
    @Test
    public void testInvalidAmountError() throws Exception {
    	TransferRequest request = new TransferRequest();
        request.setAccount("3");
        request.setAmount(new BigDecimal(-300));
        request.setCurrency("USD");
        request.setOriginBank("REVOLUT");
        request.setOriginAccount("1");

        Response output = target("/moneytransfer")
                .request()
                .post(Entity.entity(request, MediaType.APPLICATION_JSON));

        assertEquals("Should return status 200", 200, output.getStatus());
        assertNotNull("Should return transfer response", output.getEntity());
        ObjectMapper mapper = new ObjectMapper();
        TransferResponse response = mapper.readValue(output.readEntity(String.class), TransferResponse.class);
        assertEquals("Amount has to be greater than 0 message", "Amount has to be greater than 0", response.getErrorMessage());
    }
    
    @Test
    public void testCurrencyMissingError() throws Exception {
    	TransferRequest request = new TransferRequest();
        request.setAccount("3");
        request.setAmount(new BigDecimal(300));
        request.setCurrency("JPY");
        request.setOriginBank("REVOLUT");
        request.setOriginAccount("1");

        Response output = target("/moneytransfer")
                .request()
                .post(Entity.entity(request, MediaType.APPLICATION_JSON));

        assertEquals("Should return status 200", 200, output.getStatus());
        assertNotNull("Should return transfer response", output.getEntity());
        ObjectMapper mapper = new ObjectMapper();
        TransferResponse response = mapper.readValue(output.readEntity(String.class), TransferResponse.class);
        assertEquals("Currency is missing message", "Currency is missing", response.getErrorMessage());
    }
}
