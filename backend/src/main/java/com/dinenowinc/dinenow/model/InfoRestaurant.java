package com.dinenowinc.dinenow.model;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass
public class InfoRestaurant <Restaurant extends BaseEntity> extends BaseEntity {
	
	
	
	@OneToOne(cascade = CascadeType.PERSIST, fetch= FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name="id_restaurant",nullable=false)
    private Restaurant restaurant = null;
	public Restaurant getRestaurant() {
		return this.restaurant;
	}
	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}
}

