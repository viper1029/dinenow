package com.dinenowinc.dinenow.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.dinenowinc.dinenow.model.helpers.BaseEntity;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="restaurant_menu")
@NamedQueries({@NamedQuery(name="RestaurantMenu.GetAll", query = "from RestaurantMenu r")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestaurantMenu extends BaseEntity {
	
	private static final long serialVersionUID = 691091407346434736L;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="id_restaurant")
	@ForeignKey(name="id_restaurant_Restaurant_FK")
	private Restaurant restaurant; 
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="id_menu")
	@ForeignKey(name="id_menu_Menu_FK")
	private Menu menu; 
	
	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	
	
}
