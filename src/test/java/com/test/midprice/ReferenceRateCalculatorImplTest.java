package com.test.midprice;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.test.midprice.ReferenceRateCalculator.Configuration;
import com.test.midprice.testharness.ConfigurationFactory;
import static com.test.midprice.FXPriceTestFactory.*;

@RunWith(Parameterized.class)// requires JUnit 4.10 onwards
public class ReferenceRateCalculatorImplTest {
	
	private ReferenceRateCalculatorImpl refRateCalculator;
	
	//TestData
	private Configuration config;
	private List<Tuple<FXPrice, FXPrice>> fxPricesAndRefRates;
	
	@Before
	public void setup() {
		refRateCalculator = new ReferenceRateCalculatorImpl();
	}
	
	public ReferenceRateCalculatorImplTest(Configuration config, List<Tuple<FXPrice, FXPrice>> fxPricesAndRefRates) {
		this.config = config;
		this.fxPricesAndRefRates = fxPricesAndRefRates;
	}
	
	@Parameters	
    public static Collection<Object[]> data() {
    	Collection<Object[]> testCases = new ArrayList<Object[]>();
    	
    	Object[] testCase1 = testWith3Markets();    	
    	testCases.add(testCase1);
    	
    	Object[] testCase2 = testWith2Markets();    	
    	testCases.add(testCase2);
    	return testCases;
    }

	private static Object[] testWith3Markets() {
		Configuration config = ConfigurationFactory.get3MarketConfiguration();
    	List<Tuple<FXPrice, FXPrice>> fxPricesAndRates = getInputPricesAndExpectedRateFor3Markets();
    	Object[] testCase1 = new Object[2];
    	testCase1[0] = config;
    	testCase1[1] = fxPricesAndRates;
		return testCase1;
	}
	
	private static Object[] testWith2Markets() {
		Configuration config = ConfigurationFactory.get2MarketConfiguration();
    	List<Tuple<FXPrice, FXPrice>> fxPricesAndRates = getInputPricesAndExpectedRateFor2Markets();
    	Object[] testCase1 = new Object[2];
    	testCase1[0] = config;
    	testCase1[1] = fxPricesAndRates;
		return testCase1;
	}
    
    @Test
    public void testOnConfiguration() {
    	refRateCalculator.onConfiguration(config);
    	assertEquals("Number of markets not same as configuration passed in.", config.getSize(), refRateCalculator.getSizeOfMarkets()); 
    }
    
    @Test
    public void testReferenceRateCorrectlyCalculated() {
    	refRateCalculator.onConfiguration(config);
    	for(Tuple<FXPrice, FXPrice> inputAndOutput: fxPricesAndRefRates) {
    		FXPrice input = inputAndOutput.getObject1();
    		FXPrice expectedRefRate = inputAndOutput.getObject2();
    		refRateCalculator.onFxPrice(input);
    		assertEquals("Calculated Reference Rate not same.", expectedRefRate, refRateCalculator.calculate());
    	}
    }
    
    @After
	public void tearDown() {
		refRateCalculator = null;
	}
}
