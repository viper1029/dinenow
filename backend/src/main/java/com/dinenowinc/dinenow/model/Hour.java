package com.dinenowinc.dinenow.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Hour implements Serializable{


	private static final long serialVersionUID = -3053017512634583644L;

	private WeekDayType weekDayType;
	@Temporal(TemporalType.TIME)
	private Date fromTime;
	@Temporal(TemporalType.TIME)
	private Date toTime;
	
	public Hour() {
	}
	
	public Hour(WeekDayType weekDayType, Date from, Date to) {
		this.weekDayType = weekDayType;
		this.fromTime = from;
		this.toTime = to;
	}
	
	public WeekDayType getWeekDayType() {
		return weekDayType;
	}
	public void setWeekDayType(WeekDayType weekDayType) {
		this.weekDayType = weekDayType;
	}

	public Date getFromTime() {
		return fromTime;
	}
	
/*	public String getFromTimeCustom() {
		DateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss");
		return dateFormat.format(this.fromTime);
	}*/
	public void setFromTime(Date fromTime) {
		this.fromTime = fromTime;
	}
	public Date getToTime() {
		return toTime;
	}
	
/*	public String getToTimeCustom() {
		DateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss");
		return dateFormat.format(this.fromTime);
	}*/
	public void setToTime(Date toTime) {
		this.toTime = toTime;
	}
}
