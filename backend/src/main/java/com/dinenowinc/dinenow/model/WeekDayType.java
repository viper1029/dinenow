package com.dinenowinc.dinenow.model;

public enum WeekDayType {
	    MON(0),
	    TUE(1),
	    WED(2),
	    THU(3),
	    FRI(4),
	    SAT(5),
	    SUN(6);
	    
	    private final int mValue;
	    private WeekDayType(int value) {
	        this.mValue = value;
	    }
	    public int getValue() {
	        return mValue;
	    }
	    public static WeekDayType fromInteger(int value) {
	    	for (WeekDayType zoneType : WeekDayType.values()) {
				if(zoneType.getValue() == value) {
					return zoneType;
				}
			}
	    	return WeekDayType.MON;
	    }
}
