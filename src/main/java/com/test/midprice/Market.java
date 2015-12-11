package com.test.midprice;

public class Market {
	
	private final PriceSource source;
	private final PriceProvider provider;
	
	public Market(PriceSource source, PriceProvider provider) {
		super();
		this.source = source;
		this.provider = provider;
	}

	public PriceSource getSource() {
		return source;
	}

	public PriceProvider getProvider() {
		return provider;
	}
}
