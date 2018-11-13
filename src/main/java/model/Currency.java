package model;

import java.util.Arrays;
import java.util.List;

public class Currency {

	private String id;
	
	public static Currency USD = new Currency("USD");
	public static Currency GBP = new Currency("GBP");
	public static Currency EURO = new Currency("EURO");
	public static Currency RUR = new Currency("RUR");
	private static List<Currency> bucket = Arrays.asList(USD,GBP,EURO,RUR);

	public Currency(String id) {
		super();
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public static Currency fromString(String currencyString) {
		if (currencyString == null)
			return null;
		
		return bucket.stream()
				.filter(c -> c.getId().equalsIgnoreCase(currencyString))
				.findFirst().orElse(null);
	}
}
