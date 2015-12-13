package com.test.midprice;

import static com.test.midprice.PriceProvider.*;
import static com.test.midprice.PriceSource.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MarketFactoryTest {
	
	@Test
	public void testMarketKey() {
		long key = MarketFactory.getMarketKey(SOURCE1, PROVIDER1);
		assertFalse("Market key is negative", key < 0);
	}
	
	@Test
	public void testMultipleInvocationsProducesSameKey() {
		long key1 = MarketFactory.getMarketKey(SOURCE1, PROVIDER1);
		long key2 = MarketFactory.getMarketKey(SOURCE1, PROVIDER1);
		assertTrue("Market keys are not same", key1 == key2);		
	}
	
	@Test
	public void testDiffMktsProducesDiffKeys() {
		long key1 = MarketFactory.getMarketKey(SOURCE1, PROVIDER1);
		long key2 = MarketFactory.getMarketKey(SOURCE1, PROVIDER2);
		assertFalse("Market keys are same for different markets", key1 == key2);
		
		long key3 = MarketFactory.getMarketKey(SOURCE2, PROVIDER1);
		long key4 = MarketFactory.getMarketKey(SOURCE1, PROVIDER1);		
		assertFalse("Market keys are same for different markets", key3 == key4);
	}
	
	@Test
	public void testPriceSourceAndProviderCorrectlyEncoded() {
		long key = MarketFactory.getMarketKey(SOURCE1, PROVIDER2);
		assertEquals("Price Source not correctly encoded.", SOURCE1, MarketFactory.getPriceSource(key));
		assertEquals("Price Provider not correctly encoded.", PROVIDER2, MarketFactory.getPriceProvider(key));
	}
	
	@Test
	public void testNullPriceProviderCorrectlyEncoded() {
		long key = MarketFactory.getMarketKey(SOURCE1, null);
		assertNull("Price Source not correctly encoded.", MarketFactory.getPriceProvider(key));
	}
}
