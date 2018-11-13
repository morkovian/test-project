import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

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
        request.setAmount(100f);
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
        assertEquals("Balance should be right", new Float(100f), response.getNewAccountBalance());
    }

    
    @Test
    public void testCashDepositGBPintoEUROS() throws Exception {
        TransferRequest request = new TransferRequest();
        request.setAccount("2");
        request.setAmount(100f);
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
        Float expected = exService.exchange(request.getAmount(), Currency.GBP, Currency.EURO);
        assertEquals("Balance should be right", expected, response.getNewAccountBalance());
    }

    @Test
    public void testInternalTransfer() throws Exception {
    	//first deposit some money
        TransferRequest request = new TransferRequest();
        request.setAccount("1");
        request.setAmount(100f);
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
        request.setAmount(100f);
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
        Float expected = exService.exchange(request.getAmount(), Currency.USD, Currency.GBP);
        assertEquals("Balance should be right", expected, response.getNewAccountBalance());
        
        target("/moneytransfer/debug").request().get();
    }
}
