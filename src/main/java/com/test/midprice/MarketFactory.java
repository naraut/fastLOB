package com.test.midprice;

public class MarketFactory {
	
	static long getMarketKey(PriceSource source, PriceProvider provider) {
		int x = source.ordinal();
		int y = (provider!= null?provider.ordinal():-1);
		return (((long)x) << 32) | (y & 0xffffffffL);
	}
	
	static PriceSource getPriceSource(long key) {
		int x = (int)(key >> 32);
		PriceSource[] sources = PriceSource.values();
		return sources[x];
	}
	
	static PriceProvider getPriceProvider(long key) {
		int y = (int)key;
		if(y > -1) {
			PriceProvider[] providers = PriceProvider.values();
			return providers[y];
		}
		return null;
	}
}
