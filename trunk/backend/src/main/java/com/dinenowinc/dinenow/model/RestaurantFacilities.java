package com.dinenowinc.dinenow.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name="restaurant_facilities")
@Audited
@NamedQueries({@NamedQuery(name="RestaurantFacilities.GetAll", query = "from RestaurantFacilities rf")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestaurantFacilities implements Serializable{

	@Id @GeneratedValue(generator="uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false)
    private String id = null;
	
	private boolean ac;
	private boolean smoking;
	private boolean table_booking;
	private boolean home_delivery;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isAc() {
		return ac;
	}
	public void setAc(boolean ac) {
		this.ac = ac;
	}
	public boolean isSmoking() {
		return smoking;
	}
	public void setSmoking(boolean smoking) {
		this.smoking = smoking;
	}
	public boolean isTable_booking() {
		return table_booking;
	}
	public void setTable_booking(boolean table_booking) {
		this.table_booking = table_booking;
	}
	public boolean isHome_delivery() {
		return home_delivery;
	}
	public void setHome_delivery(boolean home_delivery) {
		this.home_delivery = home_delivery;
	}

}
