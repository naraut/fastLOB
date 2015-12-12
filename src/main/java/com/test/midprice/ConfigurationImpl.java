package com.test.midprice;

import com.test.midprice.ReferenceRateCalculator.Configuration;
import static com.test.midprice.PriceSource.*;
import static com.test.midprice.PriceProvider.*;
import static com.test.midprice.MarketFactory.*;

public class ConfigurationImpl implements Configuration {
	
	private final Long[] markets; 
	
	public ConfigurationImpl(){
		markets = new Long[7];
		markets[0] = getMarketKey(SOURCE1, PROVIDER1);
		markets[1] = getMarketKey(SOURCE1, null);
		markets[2] = getMarketKey(SOURCE3, PROVIDER3);
		markets[3] = getMarketKey(SOURCE1, PROVIDER3);
		markets[4] = getMarketKey(SOURCE2, PROVIDER2);
		markets[5] = getMarketKey(SOURCE3, PROVIDER1);
		markets[6] = getMarketKey(SOURCE2, null);
	}
	
	@Override
	public int getSize() {
		return markets.length;
	}

	@Override
	public PriceSource getSource(int index) {
		return MarketFactory.getPriceSource(markets[index]);
	}

	@Override
	public PriceProvider getProvider(int index) {
		return MarketFactory.getPriceProvider(markets[index]);
	}
}
