package com.test.sampleLOB;

import java.util.Collection;

public interface OrderBook {
	
	public void addOrder(Order order);
	public void removeOrder(long orderId);
	public void modifyOrder(long orderId, long size);
	public double getPriceForLevel(char side, int level);
	public long getAvailableSizeForLevel(char side, int level);
	public Collection<Order> getAllOrders(char side); 
	
}
