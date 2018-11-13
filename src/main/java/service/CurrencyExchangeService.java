package service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Singleton;

import model.Currency;

@Singleton
public class CurrencyExchangeService {

	private Map<Currency, Float> rates;
	
	public CurrencyExchangeService() {
		rates = new ConcurrentHashMap<Currency, Float>();
		rates.put(Currency.USD, 1f);
		rates.put(Currency.EURO, 0.88985f);
		rates.put(Currency.GBP, 0.77593f);
		rates.put(Currency.RUR, 67.605f);
	}
	
	public Float exchange(Float amount, Currency from, Currency to) {
		return amount / rates.get(from) * rates.get(to);
	}
}
