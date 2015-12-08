package com.test.sampleLOB;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class OrderBookImpl implements OrderBook {
	
	private final ConcurrentMap<Long, Order> bidOrders = new ConcurrentHashMap<Long, Order>();
	private final ConcurrentMap<Long, Order> askOrders = new ConcurrentHashMap<Long, Order>();
	private static final BidComparator bidComp = new BidComparator();
	private static final AskComparator askComp = new AskComparator();
	
	@Override
	public void addOrder(Order order) {
		switch(order.getSide()) {
			case 'B':
				bidOrders.put(order.getId(), order);
				break;
			case 'A':
				askOrders.put(order.getId(), order);
				break;
		}				
	}

	@Override
	public void removeOrder(long orderId) {
		bidOrders.remove(orderId);
		askOrders.remove(orderId);
	}

	@Override
	public void modifyOrder(long orderId, long size) {
		if(bidOrders.containsKey(orderId)){
			Order o = bidOrders.remove(orderId);
			Order newOrder = new Order(o.getId(), o.getPrice(), o.getSide(), size, o.getSym());
			bidOrders.putIfAbsent(orderId, newOrder);
		}else if(askOrders.containsKey(orderId)){
			Order o = askOrders.remove(orderId);
			Order newOrder = new Order(o.getId(), o.getPrice(), o.getSide(), size, o.getSym());
			askOrders.putIfAbsent(orderId, newOrder);
		}
	}

	@Override
	public double getPriceForLevel(char side, int level) {
		List<Order> list;
		switch(side) {
			case 'B':
				list = new ArrayList<Order>(bidOrders.values());
				Collections.sort(list, bidComp);
				if(list.size() < level) return 0d;
				return list.get(level-1).getPrice();
			case 'A':
				list = new ArrayList<Order>(askOrders.values());
				Collections.sort(list, askComp);
				if(list.size() < level) return 0d;
				return list.get(level-1).getPrice();
			default:
				throw new RuntimeException("Unknown side");
		}
	}

	@Override
	public long getAvailableSizeForLevel(char side, int level) {
		List<Order> list;
		switch(side) {
			case 'B':
				list = new ArrayList<Order>(bidOrders.values());
				Collections.sort(list, bidComp);
				return getSum(list, level);
			case 'A':
				list = new ArrayList<Order>(askOrders.values());
				Collections.sort(list, askComp);
				return getSum(list, level);
			default:
				throw new RuntimeException("Unknown side");
		}
	}

	@Override
	public Collection<Order> getAllOrders(char side) {
		List<Order> list;
		switch(side) {
			case 'B':
				list = new ArrayList<Order>(bidOrders.values());
				Collections.sort(list, bidComp);
				return list;
			case 'A':
				list = new ArrayList<Order>(askOrders.values());
				Collections.sort(list, askComp);
				return list;
			default:
				throw new RuntimeException("Unknown side");
		}
	}
	
	private long getSum(List<Order> orders, int level) {
		long sum = 0L;
		for(int i=level-1;i<orders.size();i++) {
			sum += orders.get(i).getSize();
		}
		return sum;
	}	
}
