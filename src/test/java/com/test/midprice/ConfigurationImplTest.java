package com.test.midprice;

import static com.test.midprice.PriceProvider.*;
import static com.test.midprice.PriceSource.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

@RunWith(Parameterized.class)// requires JUnit 4.10 onwards
public class ConfigurationImplTest {
	
	@SuppressWarnings({"rawtypes", "unchecked"})	
	@Parameters	
    public static Collection<Object[]> data() {
    	    	
    	Object[] testCaseNoMarkets = new Object[1];    	    	
    	
    	
    	Object[] testCase1Market = new Object[1];
    	Tuple[] inputTuple1 = {new Tuple( SOURCE1, PROVIDER1)};
    	testCase1Market[0] = inputTuple1;
    	
    	
    	Object[] testCase4Markets = new Object[1];
    	Tuple[] inputTuple4 = {new Tuple( SOURCE1, PROVIDER1), new Tuple( SOURCE1, PROVIDER2), 
    							new Tuple( SOURCE1, PROVIDER3), new Tuple( SOURCE1, null)};
    	testCase4Markets[0] = inputTuple4;
    	
    	Collection<Object[]> testCases = new ArrayList<Object[]>();
    	testCases.add(testCaseNoMarkets); // No markets in configuration
    	testCases.add(testCase1Market); // 1 mkt
    	testCases.add(testCase4Markets);// 4 mkts
    	return testCases;
    }
    
    private Tuple<PriceSource, PriceProvider>[] inputTuples = new Tuple[0];  
    
	private ConfigurationImpl configTest;
	
	public ConfigurationImplTest(Tuple<PriceSource, PriceProvider>[] inputTuples) {
		if(inputTuples != null) {
			this.inputTuples = inputTuples;
		}
	}
	
	@Before
	public void setup() {
		configTest = new ConfigurationImpl();
	}
	
	@Test
	public void testEmptyConfiguration() {
		assertEquals("Configuration size is not 0", 0, configTest.getSize());
	}
	
	@Test
	public void testAddingMarkets() {
		for(Tuple<PriceSource, PriceProvider> test : inputTuples) {
			configTest.addMarket(test.getObject1(), test.getObject2());
		}		
		//assertions
		assertEquals("Configuration size is not "+inputTuples.length, inputTuples.length, configTest.getSize());		
		for(int i=0;i<inputTuples.length;i++) {
			Tuple<PriceSource, PriceProvider> test = inputTuples[i];
			assertEquals("Configuration source is not correct", test.getObject1(), configTest.getSource(i));
			assertEquals("Configuration provider is not correct", test.getObject2(), configTest.getProvider(i));	
		}
	}
	
	@After
	public void tearDown() {
		configTest = null;
	}	
}
