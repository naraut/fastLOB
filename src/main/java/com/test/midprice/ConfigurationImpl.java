package com.test.midprice;

import com.test.midprice.ReferenceRateCalculator.Configuration;

import java.util.ArrayList;
import java.util.List;

import static com.test.midprice.MarketFactory.*;

public class ConfigurationImpl implements Configuration {
	
	private final List<Long> markets = new ArrayList<Long>(); 
	
	@Override
	public int getSize() {
		return markets.size();
	}

	@Override
	public PriceSource getSource(int index) {
		return getPriceSource(markets.get(index));
	}

	@Override
	public PriceProvider getProvider(int index) {
		return getPriceProvider(markets.get(index));
	}
	
	public void addMarket(PriceSource source, PriceProvider provider) {
		markets.add(getMarketKey(source, provider));
	}
}
