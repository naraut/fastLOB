package com.test.midprice.testharness;

import static com.test.midprice.PriceProvider.*;
import static com.test.midprice.PriceSource.*;
import static com.test.midprice.testharness.ConfigurationFactory.*;

import java.util.ArrayList;
import java.util.List;

import com.test.midprice.FXPrice;
import com.test.midprice.FXPriceImpl;
import com.test.midprice.ReferenceRateCalculator;
import com.test.midprice.ReferenceRateCalculatorImpl;
import com.test.midprice.ReferenceRateCalculator.Configuration;

public class TestHarness {
	
	public static void main(String[] args) {		
		ReferenceRateCalculator rateCalculator = new ReferenceRateCalculatorImpl();		
		List<FXPrice> prices = getPrices();
		Configuration config = get7MarketConfiguration();
		sendPrices(rateCalculator, config, prices);
		Configuration config4 = get4MarketConfiguration();
		sendPrices(rateCalculator, config4, prices);
		Configuration config10 = get10MarketConfiguration();
		sendPrices(rateCalculator, config10, prices);
	}

	private static void sendPrices(ReferenceRateCalculator rateCalculator, Configuration config, List<FXPrice> prices) {
		double sum = 0;
		rateCalculator.onConfiguration(config);		
		System.out.println("No of prices: " + prices.size());
		long start = System.currentTimeMillis();
		for(FXPrice price: prices){
			rateCalculator.onFxPrice(price);
			FXPrice refPrice = rateCalculator.calculate();
			if(!refPrice.isStale()) {				
				sum += refPrice.getBid();
			}
//			System.out.println(rateCalculator.calculate());
		}
		System.out.format("Memory %,d total, %,d free\n",Runtime.getRuntime().totalMemory(),Runtime.getRuntime().freeMemory());
		System.out.println("Time taken (ms): " + (System.currentTimeMillis() - start)  +" sum is "+ sum);
	}
	
	
	
	private static List<FXPrice> getPrices() {		
		int count = 40_000_000;
		List<FXPrice> prices = new ArrayList<FXPrice>(count);
		for(int i=0;i<(count/20);i++) {			
			prices.add(new FXPriceImpl(10.2d, 11.3d, false, SOURCE1, PROVIDER1));
			prices.add(new FXPriceImpl(7.1d, 8.3d, false, SOURCE2, PROVIDER2));
			prices.add(new FXPriceImpl(9.2d, 10.3d, false, SOURCE3, PROVIDER3));
			prices.add(new FXPriceImpl(18.2d, 19.1d, false, SOURCE1, PROVIDER1));
			prices.add(new FXPriceImpl(50.2d, 61.5d, false, SOURCE2, null));
			
			prices.add(new FXPriceImpl(11.2d, 12.3d, false, SOURCE1, PROVIDER3));
			prices.add(new FXPriceImpl(2.1d, 3.3d, false, SOURCE1, null));
			prices.add(new FXPriceImpl(49.2d, 50.3d, false, SOURCE3, PROVIDER1));
			prices.add(new FXPriceImpl(28.2d, 29.1d, false, SOURCE1, PROVIDER1));
			prices.add(new FXPriceImpl(25.2d, 26.5d, false, SOURCE2, null));
			
			
			prices.add(new FXPriceImpl(Double.NaN, Double.NaN, true, SOURCE1, PROVIDER1));
			prices.add(new FXPriceImpl(Double.NaN, Double.NaN, true, SOURCE2, PROVIDER2));
			prices.add(new FXPriceImpl(Double.NaN, Double.NaN, true, SOURCE3, PROVIDER3));
			prices.add(new FXPriceImpl(Double.NaN, Double.NaN, true, SOURCE1, PROVIDER1));
			prices.add(new FXPriceImpl(Double.NaN, Double.NaN, true, SOURCE2, null));
			
			prices.add(new FXPriceImpl(Double.NaN, Double.NaN, true, SOURCE1, PROVIDER3));
			prices.add(new FXPriceImpl(Double.NaN, Double.NaN, true, SOURCE1, null));
			prices.add(new FXPriceImpl(Double.NaN, Double.NaN, true, SOURCE3, PROVIDER1));
			prices.add(new FXPriceImpl(Double.NaN, Double.NaN, true, SOURCE1, PROVIDER1));
			prices.add(new FXPriceImpl(Double.NaN, Double.NaN, true, SOURCE2, null));
		}
		System.out.format("Memory %,d total, %,d free\n",Runtime.getRuntime().totalMemory(),Runtime.getRuntime().freeMemory());
		return prices;
	}
}
