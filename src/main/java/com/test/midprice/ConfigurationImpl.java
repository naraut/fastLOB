package com.test.midprice;

import com.test.midprice.ReferenceRateCalculator.Configuration;
import static com.test.midprice.PriceSource.*;
import static com.test.midprice.PriceProvider.*;

public class ConfigurationImpl implements Configuration {
	
	private final Market[] markets; 
	
	public ConfigurationImpl(){
		markets = new Market[7];
		markets[0] = new Market(SOURCE1, PROVIDER1);
		markets[1] = new Market(SOURCE1, null);
		markets[2] = new Market(SOURCE3, PROVIDER3);
		markets[3] = new Market(SOURCE1, PROVIDER3);
		markets[4] = new Market(SOURCE2, PROVIDER2);
		markets[5] = new Market(SOURCE3, PROVIDER1);
		markets[6] = new Market(SOURCE2, null);
	}
	
	@Override
	public int getSize() {
		return markets.length;
	}

	@Override
	public PriceSource getSource(int index) {
		return markets[index].getSource(); 
	}

	@Override
	public PriceProvider getProvider(int index) {
		return markets[index].getProvider();
	}
}
