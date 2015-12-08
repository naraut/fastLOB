package com.test.sampleLOB;

public class Order {
	// private variables
	long id;
	double price;
	char side; // should be B or A
	long size;
	String sym;
	long timeStamp;
	static OrderKey key;
	
	
	// constructor
	public Order(long orderid, double orderprice, char orderside, long ordersize, String ordersym) {
		id = orderid;
		price = orderprice;
		size = ordersize;
		side = orderside;
		sym = ordersym;
		timeStamp =System.nanoTime();
	}

	// methods
	long getId() {
		return id;
	}

	double getPrice() {
		return price;
	}

	long getSize() {
		return size;
	}

	String getSym() {
		return sym;
	}

	char getSide() {
		return side;
	}
	
	OrderKey getOrderKey(){
		return key;
	}
	
	class OrderKey {
		
		long timeStamp;
		
		OrderKey() {
			timeStamp =System.nanoTime();
		}
		
		public boolean equals(Object obj) {
			if(obj == null || !(obj instanceof Order)) {
				return false;
			}
			Order o2 = (Order)obj;
			if(o2.id == id && o2.side == side){
				return true;	
			}
			return false;
		}
		
		public int hashCode() { 
			int result = 31;
		    result = 31 * result + side;
		    result = 31 * result + (int)(id ^ (id >>> 32));
		    return result;
		}		
	}
}
