This is a MoneyTransfer test project.
It is a standalone web application running on embedded Tomcat.
<br/><br/>
To generate the app:<br/>
$ mvn package
<br/><br/>
Run this application on Mac and Linux:<br/>
$ sh target/bin/webapp
<br/><br/>
On Windows the command is:<br/>
C:/> target/bin/webapp.bat

The application should start up on port 8080. 
REST service is running at
<b>http://localhost:8080/rest/moneytransfer</b>
it accepts POST requests with following sample JSON
<pre>
{	
	"account":"1",
	"amount":"100",
	"description":"Transfer from USD to GBP account",
	"originBank":"REVOLUT",
	"originAccount":"2",
	"currency": "USD"|"RUR"|"EURO"|"GBP"
}
</pre>

<br/>
GET <b>http://localhost:8080/rest/moneytransfer/debug</b> will return full information about available accounts

<br/>
<br/>
Current MY_BANK_ID = "REVOLUT". <br/>
If you supply it in originBank, then transfer will be internal bank transfer with both account and originAccount getting validated.
<br/>
There is following test data preloaded:		
<pre>		
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
</pre>
		