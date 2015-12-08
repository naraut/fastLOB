package com.test.sampleLOB;

import java.util.Comparator;

public final class BidComparator implements Comparator<Order> {
	
	@Override
	public int compare(Order o1, Order o2) {
		
		if(o1.getSide() != o2.getSide()) {
			throw new RuntimeException("Unequal sides on orders");
		}
		switch(o1.getSide()) {
			case 'B':
				if (o1.getPrice() > o2.getPrice()) {
					return -1;
				}else if(o1.getPrice() < o2.getPrice()){
					return 1;
				}else {
					if(o1.timeStamp > o2.timeStamp) {
						return 1;
					}else {
						return -1;
					}
				}
			default:
				throw new RuntimeException("Unknown side " + o1.getSide());	
		}		
	}
}
