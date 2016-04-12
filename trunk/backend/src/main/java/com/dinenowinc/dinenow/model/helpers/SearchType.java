package com.dinenowinc.dinenow.model.helpers;

public enum SearchType {
	    DELIVERY(0),
	    PICKUP(2),
	    BOTH(4);
	    
	    private final int mValue;
	    private SearchType(int value) {
	        this.mValue = value;
	    }
	    public int getValue() {
	        return mValue;
	    }
	    public static SearchType fromInteger(int value) {
	    	for (SearchType zoneType : SearchType.values()) {
				if(zoneType.getValue() == value) {
					return zoneType;
				}
			}
	    	return SearchType.BOTH;
	    }
}
