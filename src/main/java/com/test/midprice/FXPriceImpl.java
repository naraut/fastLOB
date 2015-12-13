package com.test.midprice;

public class FXPriceImpl implements FXPrice {
	
	private final double bid;
	private final double offer;
	private final boolean isStale;
	private final PriceSource source;
	private final PriceProvider provider;
	
	
	public FXPriceImpl(double bid, double offer, boolean isStale, PriceSource source, PriceProvider provider) {
		super();
		this.bid = bid;
		this.offer = offer;
		this.isStale = isStale;
		this.source = source;
		this.provider = provider;
	}

	@Override
	public double getBid() {
		return bid;
	}

	@Override
	public double getOffer() {
		return offer;
	}

	@Override
	public boolean isStale() {
		return isStale;
	}

	@Override
	public PriceSource getSource() {
		return source;
	}

	@Override
	public PriceProvider getProvider() {
		return provider;
	}

	@Override
	public String toString() {
		return "FXPriceImpl [bid=" + bid + ", offer=" + offer + ", isStale=" + isStale + ", source=" + source
				+ ", provider=" + provider + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(bid);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (isStale ? 1231 : 1237);
		temp = Double.doubleToLongBits(offer);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((provider == null) ? 0 : provider.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
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
		FXPriceImpl other = (FXPriceImpl) obj;
		if (Double.doubleToLongBits(bid) != Double.doubleToLongBits(other.bid))
			return false;
		if (isStale != other.isStale)
			return false;
		if (Double.doubleToLongBits(offer) != Double.doubleToLongBits(other.offer))
			return false;
		if (provider != other.provider)
			return false;
		if (source != other.source)
			return false;
		return true;
	}
}
