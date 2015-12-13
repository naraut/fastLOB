package com.test.midprice;

import static com.test.midprice.PriceProvider.*;
import static com.test.midprice.PriceSource.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This class acts as a Test FXPrice input and corresponding output reference rate Generator. 
 *
 */
public class FXPriceTestFactory {
	
	static List<Tuple<FXPrice, FXPrice>> getInputPricesAndExpectedRateFor3Markets() {
		
    	List<Tuple<FXPrice, FXPrice>> priceAndRefRateList = new ArrayList<Tuple<FXPrice, FXPrice>>();
    	
    	Tuple<FXPrice, FXPrice> p1 
    				= new Tuple<FXPrice, FXPrice>(new FXPriceImpl(10.2d, 11.3d, false, SOURCE1, PROVIDER1), //mid price = 10.75
    											new FXPriceImpl(10.75d, 10.75d, false, SOURCE1, PROVIDER1));//(10.2+11.3)/2
    	Tuple<FXPrice, FXPrice> p2 
    				= new Tuple<FXPrice, FXPrice>(new FXPriceImpl(7.1d, 8.3d, false, SOURCE1, PROVIDER2),// mid price = 7.7
    												new FXPriceImpl(9.225d, 9.225d, false, null, null)); // median of 10.75 and 7.7
    	Tuple<FXPrice, FXPrice> p3 
    				= new Tuple<FXPrice, FXPrice>(new FXPriceImpl(9.2d, 10.3d, false, SOURCE1, null), // mid price = 9.75
    												new FXPriceImpl(9.75, 9.75, false, SOURCE1, null)); // median of 10.75, 9.75, 7.7
    	
    	Tuple<FXPrice, FXPrice> p4 
					= new Tuple<FXPrice, FXPrice>(new FXPriceImpl(1.2d, 2.3d, false, SOURCE1, PROVIDER1), // mid price = 1.75 replaces 10.75
									new FXPriceImpl(7.7d, 7.7d, false, SOURCE1, PROVIDER2));// median of 9.75, 7.7, 1.75
    	Tuple<FXPrice, FXPrice> p5 
					= new Tuple<FXPrice, FXPrice>(new FXPriceImpl(20.5d, 30.1d, false, SOURCE1, PROVIDER2),// mid price of 25.3 
										new FXPriceImpl(9.75d, 9.75d, false, SOURCE1, null));//median of 1.75, 9.75, 25.3
    	
    	Tuple<FXPrice, FXPrice> p6 
				= new Tuple<FXPrice, FXPrice>(new FXPriceImpl(0d, 0d, true, SOURCE1, PROVIDER1),//stale
							new FXPriceImpl(17.525d, 17.525d, false, null, null));// median of 9.75 and 25.3
    	
    	Tuple<FXPrice, FXPrice> p7 
		= new Tuple<FXPrice, FXPrice>(new FXPriceImpl(0d, 0d, true, SOURCE1, PROVIDER2),//stale
					new FXPriceImpl(9.75d, 9.75d, false, SOURCE1, null));// median of 9.75

    	Tuple<FXPrice, FXPrice> p8 
		= new Tuple<FXPrice, FXPrice>(new FXPriceImpl(0d, 0d, true, SOURCE1, null),//stale
					new FXPriceImpl(Double.NaN, Double.NaN, true, null, null));// all stale

    	priceAndRefRateList.add(p1);
    	priceAndRefRateList.add(p2);
    	priceAndRefRateList.add(p3);
    	priceAndRefRateList.add(p4);
    	priceAndRefRateList.add(p5);
    	priceAndRefRateList.add(p6);
    	priceAndRefRateList.add(p7);
    	priceAndRefRateList.add(p8);
    	return priceAndRefRateList;
	}
    
    static List<Tuple<FXPrice, FXPrice>> getInputPricesAndExpectedRateFor2Markets() {	
    	List<Tuple<FXPrice, FXPrice>> priceAndRefRateList = new ArrayList<Tuple<FXPrice, FXPrice>>();
    	
    	Tuple<FXPrice, FXPrice> p1 
		= new Tuple<FXPrice, FXPrice>(new FXPriceImpl(10.2d, 11.3d, false, SOURCE1, PROVIDER1), //mid price = 10.75
									new FXPriceImpl(10.75d, 10.75d, false, SOURCE1, PROVIDER1));//(10.2+11.3)/2
		Tuple<FXPrice, FXPrice> p2 
				= new Tuple<FXPrice, FXPrice>(new FXPriceImpl(7.1d, 8.3d, false, SOURCE1, PROVIDER2),// mid price = 7.7
												new FXPriceImpl(9.225d, 9.225d, false, null, null)); // median of 10.75 and 7.7
		Tuple<FXPrice, FXPrice> p3 
				= new Tuple<FXPrice, FXPrice>(new FXPriceImpl(9.2d, 10.3d, false, SOURCE1, null), // ignored as mkt not configured
												new FXPriceImpl(9.225d, 9.225d, false, null, null)); // median of 10.75 and 7.7
    	
    	
    	priceAndRefRateList.add(p1);
    	priceAndRefRateList.add(p2);
    	priceAndRefRateList.add(p3);
    	return priceAndRefRateList;
	}
}
