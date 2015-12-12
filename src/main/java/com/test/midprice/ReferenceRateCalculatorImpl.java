package com.test.midprice;

import static com.test.midprice.PriceProvider.*;
import static com.test.midprice.PriceSource.*;
import static com.test.midprice.MarketFactory.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

public class ReferenceRateCalculatorImpl implements ReferenceRateCalculator {
	
	// make market as long key
	private final HashMap<Long, Double> marketMap =new HashMap<Long, Double>();
	private final TreeMap<Long, FXPrice> midPricesMap =new TreeMap<Long, FXPrice>();	
	private Long[] markets;
	private static final FXPrice stalePrice = new FXPriceImpl(Double.NaN, Double.NaN, true, null, null);
	
	@Override
	public void onConfiguration(Configuration config) {
		markets = new Long[config.getSize()];
		for(int i=0;i<config.getSize();i++) {
			PriceSource source = config.getSource(i);
			PriceProvider provider = config.getProvider(i);
			markets[i] = getMarketKey(source, provider);
		}
		Arrays.sort(markets);
	}

	@Override
	public void onFxPrice(FXPrice fxPrice) {
		if(checkMarket(fxPrice)) {
			Long mktKey = getMarketKey(fxPrice.getSource(), fxPrice.getProvider());
			if(!fxPrice.isStale()) {
				double midPrice = calculateMidPrice(fxPrice);
				Double oldMidPrice = marketMap.put(mktKey, midPrice);
				if(oldMidPrice != null) {
					midPricesMap.remove(Double.doubleToLongBits(oldMidPrice));
				}
				midPricesMap.put(Double.doubleToLongBits(midPrice), fxPrice);
			}else{				
				Double oldPrice = marketMap.remove(mktKey);
				if(oldPrice != null) {
					midPricesMap.remove(Double.doubleToLongBits(oldPrice));
				}
			}
		}else{
			System.out.println("Dropping price. Market not configured " + fxPrice);
		}
	}
	
	private double calculateMidPrice(FXPrice fxPrice) {
		return (fxPrice.getBid()+fxPrice.getOffer())/2;
	}

	@Override
	public FXPrice calculate() {
		if(midPricesMap.isEmpty()) return stalePrice;		
		int median = (midPricesMap.size()+1) >> 1;
		int i = 1;
		for(Entry<Long,FXPrice> entry: midPricesMap.entrySet()) {			
			if(i == median) {
				return entry.getValue();				
			}
			i++;
		}
		return null;
	}
	
	private boolean checkMarket(FXPrice fxPrice) {
		PriceSource source = fxPrice.getSource();
		PriceProvider provider = fxPrice.getProvider();
		int found = Arrays.binarySearch(markets, getMarketKey(source, provider));
		return (found >= 0 );		
	}
	
	public static void main(String[] args) {
		double sum = 0;
		ReferenceRateCalculator rateCalculator = new ReferenceRateCalculatorImpl();
		Configuration config = new ConfigurationImpl();
		rateCalculator.onConfiguration(config);
		List<FXPrice> prices = getPrices();
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
	
	/*
	class MidPriceHolder implements Comparable<MidPriceHolder> {
		
		private double midPrice;
		private FXPrice fxPrice;
		
		public double getMidPrice() {
			return midPrice;
		}
		public FXPrice getFxPrice() {
			return fxPrice;
		}
		public MidPriceHolder(double midPrice, FXPrice fxPrice) {
			super();
			this.midPrice = midPrice;
			this.fxPrice = fxPrice;
		}
		@Override
		public int compareTo(MidPriceHolder other) {
			return Double.compare(midPrice, other.getMidPrice());
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((fxPrice == null) ? 0 : fxPrice.hashCode());
			return result;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			MidPriceHolder other = (MidPriceHolder) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (fxPrice == null) {
				if (other.fxPrice != null)
					return false;
			} else if (!fxPrice.equals(other.fxPrice))
				return false;
			return true;
		}
		private ReferenceRateCalculatorImpl getOuterType() {
			return ReferenceRateCalculatorImpl.this;
		}
	}
	*/
}
