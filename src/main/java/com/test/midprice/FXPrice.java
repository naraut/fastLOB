package com.test.midprice;

public interface FXPrice {
	double getBid();

	double getOffer();

	boolean isStale();

	PriceSource getSource();

//	@Nullable
	PriceProvider getProvider();
}