package service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import model.Currency;

public class CurrencyExchangeService {

	private Map<Currency, BigDecimal> rates;
	
	public CurrencyExchangeService() {
		rates = new ConcurrentHashMap<Currency, BigDecimal>();
		rates.put(Currency.USD, new BigDecimal(1));
		rates.put(Currency.EURO, new BigDecimal(0.88985));
		rates.put(Currency.GBP, new BigDecimal(0.77593));
		rates.put(Currency.RUR, new BigDecimal(67.605));
	}
	
	public BigDecimal exchange(BigDecimal amount, Currency from, Currency to) {
		return amount.divide(rates.get(from), 2, RoundingMode.HALF_UP).multiply(rates.get(to));
	}
}
