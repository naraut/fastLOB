package com.test.midprice;

import static com.test.midprice.PriceProvider.*;
import static com.test.midprice.PriceSource.*;

import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class FXPriceImplTest {
	
	private FXPrice fxPriceTest;
	
	@Test
	public void testObjectCreation() {
		fxPriceTest = new FXPriceImpl(1.2d, 1.4d, true, SOURCE1, PROVIDER1);
		assertNotNull(fxPriceTest);
	}
	
	@After
	public void tearDown() {
		fxPriceTest = null;
	}	
}
