This is a MoneyTransfer test project.
It is a standalone web application running on embedded Tomcat.

To generate the app:
$ mvn package

Run this application on Mac and Linux:
$ sh target/bin/webapp

On Windows the command is:
C:/> target/bin/webapp.bat

The application should start up on port 8080. 
REST service is running at
http://localhost:8080/rest/moneytransfer
it accepts POSTs with following JSON
{	
	"account":null,
	"amount":null,
	"description":null,
	"originBank":null,
	"originAccount":null,
	"currency": "USD"|"RUR"|"EURO"|"GBP"
}

Current MY_BANK_ID = "REVOLUT". If you supply it in originBank, then transfer will be internal bank transfer with both account and originAccount being validated.
There is following test data setup:
		Customer ivanov = new Customer("Ivan", "Ivanovich", "Ivanov");
		ivanov.setId("A");
		
		Account account = new Account(ivanov, Currency.USD);
		account.setId("1");
		account = new Account(ivanov, Currency.EURO);
		account.setId("2");
		
		
		
		
		Customer petrov = new Customer("Petr", "Petrovich", "Petrov");
		petrov.setId("B");

		account = new Account(petrov, Currency.GBP);
		account.setId("3");
		account = new Account(petrov, Currency.EURO);
		account.setId("4");

		