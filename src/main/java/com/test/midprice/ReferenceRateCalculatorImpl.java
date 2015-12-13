package com.test.midprice;

import static com.test.midprice.MarketFactory.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * NotThreadSafe 
 **/
public class ReferenceRateCalculatorImpl implements ReferenceRateCalculator {
	
	/**
	 * Market is Source and Provider packed into a long.   
	 * marketMap 	is Market --> MidPrice  
	 * midPricesMap is MidPrice(Long) --> FXPrice in increasing order of MidPrice. (TreeMap)
	 */
	private final Map<Long, Double> marketMap =  new HashMap<Long, Double>();
	private final TreeMap<Long, FXPrice> midPricesMap =new TreeMap<Long, FXPrice>();
	//Configured Markets (PriceSource+PriceProvider) as array of longs. 
	private Long[] markets;
	// Singleton stale price object
	private static final FXPrice stalePrice = new FXPriceImpl(Double.NaN, Double.NaN, true, null, null);
	
	/**
	 * From the configuration object the PriceSource+PriceProvider
	 * which define a market are packed into a long and stored. 
	 * This happens only few times a day as stated.
	 */
	@Override
	public void onConfiguration(Configuration config) {
		markets = new Long[config.getSize()];
		for(int i=0;i<config.getSize();i++) {
			PriceSource source = config.getSource(i);
			PriceProvider provider = config.getProvider(i);
			markets[i] = getMarketKey(source, provider);
		}
		Arrays.sort(markets);// for binary search in checkIfMarketConfigured
		removePricesForNonConfiguredMarkets();
	}

	public int getSizeOfMarkets() {
		return markets.length;
	}
	
	private void removePricesForNonConfiguredMarkets() {
		Iterator<Entry<Long, Double>> itr =  marketMap.entrySet().iterator();
		while(itr.hasNext()) {
			Entry<Long, Double> entry = itr.next();			
			int found = Arrays.binarySearch(markets, entry.getKey());
			if(found < 0) {// if not found remove it.
				Double midPriceForMkt = entry.getValue();
				itr.remove();
				if(midPriceForMkt != null) {
					midPricesMap.remove(Double.doubleToLongBits(midPriceForMkt));
				}
			}
		}		
	}
	
	@Override
	public void onFxPrice(FXPrice fxPrice) {
		if(!validPrice(fxPrice)){
			return;
		}
		if(checkIfMarketConfigured(fxPrice)) {// if FXPrice is not in list of configured mkts then it is dropped.
			long mktKey = getMarketKey(fxPrice.getSource(), fxPrice.getProvider());
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
		}
//		else{
//			System.out.println("Dropping price. Market not configured " + fxPrice);
//		}
	}
	
	private boolean validPrice(FXPrice fxPrice) {
		if(Double.isNaN(fxPrice.getBid())
			|| Double.isInfinite(fxPrice.getBid())	
			|| Double.isNaN(fxPrice.getOffer())
			|| Double.isInfinite(fxPrice.getOffer())){
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if FXPrice is within list of configured markets.
	 * @param fxPrice
	 * @return
	 */
	private boolean checkIfMarketConfigured(FXPrice fxPrice) {
		PriceSource source = fxPrice.getSource();
		PriceProvider provider = fxPrice.getProvider();
		int found = Arrays.binarySearch(markets, getMarketKey(source, provider));
		return (found >= 0 );		
	}
	
	// Mid Price is average of bid and offer.
	private double calculateMidPrice(FXPrice fxPrice) {
		return getAverage(fxPrice.getBid(),fxPrice.getOffer());
	}
	
	private double getAverage(double d1, double d2) {
		return (d1+d2)/2;
	}

	@Override
	/** 
	 * Calculate just needs to get the median value from sorted midPricesMap.
	 *  If even number of mid prices then median is average of 2 middle values. This wont have the Source and Provider set on it. 
	 *	If odd, then we can get an exact median rate which will have the source and provider set on it.
	 */
	public FXPrice calculate() {
		if(midPricesMap.isEmpty()) {
			return stalePrice;
		}
		int size = midPricesMap.size();
		if(size == 1) {
			return getReferenceRate(midPricesMap.firstEntry().getValue());
		}
		if((size&1) == 0 ) {// EVEN
			FXPrice firstMidPrice = null, secondMidPrice = null;
			int first = size>>1;
			int second = first+1;
			int i = 1;
			for(Entry<Long,FXPrice> entry: midPricesMap.entrySet()) {			
				if(i == first) {
					firstMidPrice =  entry.getValue();				
				}else if(i == second) {
					secondMidPrice =  entry.getValue();
					break;
				}
				i++;
			}
			return getReferenceRateFrom2FXPrices(firstMidPrice,secondMidPrice);	
		}else{// ODD
			int median = (size+1) >> 1;
			int i = 1;
			for(Entry<Long,FXPrice> entry: midPricesMap.entrySet()) {			
				if(i == median) {
					return getReferenceRate(entry.getValue());				
				}
				i++;
			}			
		}
		return null;
	}
	
	/**
	 * Creates and returns a Reference Rate from the chosen median FXPrice.
	 * @param fxPrice
	 * @return FXPrice
	 */
	private FXPrice getReferenceRate(FXPrice fxPrice) {
		double refRate = calculateMidPrice(fxPrice);
		return new FXPriceImpl(refRate, refRate, fxPrice.isStale(), fxPrice.getSource(), fxPrice.getProvider());
		
	}
	
	/**
	 * Creates mid prices from 2 FXPrices and then calculates average of them 
	 * as the retured reference rate. 
	 * source and provider are set as null as the 2 rates could be from different mkts.
	 *  
	 * @param firstFXPrice
	 * @param secondFXPrice
	 * @return FXPrice
	 */
	private FXPrice getReferenceRateFrom2FXPrices(FXPrice firstFXPrice, FXPrice secondFXPrice) {
		double first = calculateMidPrice(firstFXPrice);
		double second = calculateMidPrice(secondFXPrice);
		double referenceMedianRate = getAverage(first, second);
		return new FXPriceImpl(referenceMedianRate, referenceMedianRate, false, null, null);
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
