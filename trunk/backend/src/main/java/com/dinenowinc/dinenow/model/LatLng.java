package com.dinenowinc.dinenow.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.gson.annotations.SerializedName;

public class LatLng implements Serializable{
	@SerializedName("lat")
	private double lat;
	@SerializedName("lng")
	private double lng;
	
	public LatLng() {
	}
	
	public LatLng(double lat,double lng) {
		this.lat = lat;
		this.lng = lng;
	}
	
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	
	@Override
	public String toString() {
		return lat+","+lng;
	}
}
