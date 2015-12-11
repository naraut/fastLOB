package com.test.midprice;

public interface ReferenceRateCalculator {
	void onConfiguration(Configuration configuration);

	void onFxPrice(FXPrice fxPrice);

	FXPrice calculate();

	interface Configuration {
		int getSize();

		PriceSource getSource(int index);

//		@Nullable
		PriceProvider getProvider(int index);
	}
}
