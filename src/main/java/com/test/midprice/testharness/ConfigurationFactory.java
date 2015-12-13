package com.test.midprice.testharness;

import static com.test.midprice.PriceProvider.PROVIDER1;
import static com.test.midprice.PriceProvider.PROVIDER2;
import static com.test.midprice.PriceProvider.PROVIDER3;
import static com.test.midprice.PriceSource.SOURCE1;
import static com.test.midprice.PriceSource.SOURCE2;
import static com.test.midprice.PriceSource.SOURCE3;

import com.test.midprice.ConfigurationImpl;
import com.test.midprice.ReferenceRateCalculator.Configuration;

public class ConfigurationFactory {
	
	public static Configuration get3MarketConfiguration() {
		ConfigurationImpl config = new ConfigurationImpl();
		config.addMarket(SOURCE1, PROVIDER1);		
		config.addMarket(SOURCE1, PROVIDER2);
		config.addMarket(SOURCE1, null);	
		return config;
	}
	
	public static Configuration get2MarketConfiguration() {
		ConfigurationImpl config = new ConfigurationImpl();
		config.addMarket(SOURCE1, PROVIDER1);		
		config.addMarket(SOURCE1, PROVIDER2);
		return config;
	}
	
	public static Configuration get7MarketConfiguration() {
		ConfigurationImpl config = new ConfigurationImpl();
		config.addMarket(SOURCE1, PROVIDER1);
		config.addMarket(SOURCE1, null);
		config.addMarket(SOURCE3, PROVIDER3);
		config.addMarket(SOURCE1, PROVIDER3);
		config.addMarket(SOURCE2, PROVIDER2);
		config.addMarket(SOURCE3, PROVIDER1);
		config.addMarket(SOURCE2, null);
		return config;
	}
	
	public static Configuration get4MarketConfiguration() {
		ConfigurationImpl config = new ConfigurationImpl();
		config.addMarket(SOURCE1, PROVIDER1);
		config.addMarket(SOURCE1, null);
		config.addMarket(SOURCE3, PROVIDER3);
		config.addMarket(SOURCE1, PROVIDER3);		
		return config;
	}
	
	public static Configuration get10MarketConfiguration() {
		ConfigurationImpl config = new ConfigurationImpl();
		config.addMarket(SOURCE1, PROVIDER1);
		config.addMarket(SOURCE1, null);
		config.addMarket(SOURCE3, PROVIDER3);
		config.addMarket(SOURCE1, PROVIDER3);
		config.addMarket(SOURCE2, PROVIDER2);
		config.addMarket(SOURCE3, PROVIDER1);
		config.addMarket(SOURCE2, null);
		config.addMarket(SOURCE3, null);
		config.addMarket(SOURCE1, PROVIDER2);
		config.addMarket(SOURCE2, PROVIDER1);
		return config;
	}
}
