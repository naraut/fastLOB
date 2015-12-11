package com.test.midprice;

import static com.test.midprice.PriceProvider.*;
import static com.test.midprice.PriceSource.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReferenceRateCalculatorImpl implements ReferenceRateCalculator {
	
	private final ArrayList<MidPriceHolder> priceList = new ArrayList<MidPriceHolder>();
	private Configuration configuration;
	
	@Override
	public void onConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public void onFxPrice(FXPrice fxPrice) {
		if(checkMarket(fxPrice)) {
			double midPrice = (fxPrice.getBid()+fxPrice.getOffer())/2;
			MidPriceHolder holder = new MidPriceHolder(midPrice, fxPrice);
			int removeIndex = Collections.binarySearch(priceList, holder);
			
			if(removeIndex > -1) { 
				priceList.remove(removeIndex);
			}
			
			if(!fxPrice.isStale()) {
				int insertLocation = Collections.binarySearch(priceList, holder);
				priceList.add(~insertLocation, holder);
			}
		}else{
			System.out.println("Dropping price. Market not configured " + fxPrice);
		}
	}

	@Override
	public FXPrice calculate() {
		if(!priceList.isEmpty()) {
			int medianIndex = (priceList.size()+1)/2;
			MidPriceHolder median = priceList.get(medianIndex-1);
			return new FXPriceImpl(median.getMidPrice(), median.getMidPrice(), median.getFxPrice().isStale(), median.getFxPrice().getSource(), median.getFxPrice().getProvider());
		}else{
			return new FXPriceImpl(Double.NaN, Double.NaN, true, null, null);
		}
	}
	
	private boolean checkMarket(FXPrice fxPrice) {
		PriceSource source = fxPrice.getSource();
		PriceProvider provider = fxPrice.getProvider();
		int size = configuration.getSize();
		for(int i=0;i<size;i++) {
			if(source.equals(configuration.getSource(i))) {
				if((provider == null && configuration.getProvider(i) == null)
						|| (provider !=null && provider.equals(configuration.getProvider(i)))) {
					return true;
				}
			}
		}
		return false;
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
			sum += rateCalculator.calculate().getBid();
//			System.out.println("Reference Rate is: "+ rateCalculator.calculate());
		}
		System.out.println("Time taken (ms): " + (System.currentTimeMillis() - start)  +" sum is "+ sum);
		
	}
	
	private static List<FXPrice> getPrices() {
		int count = 20_000_000;
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
		return prices;
	}
	
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
}
