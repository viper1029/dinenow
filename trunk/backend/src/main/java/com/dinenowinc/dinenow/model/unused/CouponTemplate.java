/*
package com.dinenowinc.dinenow.model.unused;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.dinenowinc.dinenow.model.Coupon;
import com.dinenowinc.dinenow.model.Restaurant;
import com.dinenowinc.dinenow.model.helpers.BaseEntity;
import com.dinenowinc.dinenow.model.helpers.NetworkStatus;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="coupon_template")
@NamedQueries({@NamedQuery(name="CouponTemplate.GetAll", query = "from CouponTemplate c")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class CouponTemplate extends BaseEntity {

	
	private int minimum_value;
	private int maximum_value;
	private Date expiry;
	
	private String url;	
	private NetworkStatus networkStatus;
	
	@ManyToOne(cascade = {CascadeType.PERSIST},  fetch= FetchType.LAZY)
    @JoinColumn(name="id_restaurant",nullable=false)
	@ForeignKey(name="Fk_restaurant_couponTemplates")
    private Restaurant restaurant;
	
	@ManyToOne(cascade = {CascadeType.PERSIST},  fetch= FetchType.LAZY)
    @JoinColumn(name="id_coupon",nullable=false)
	@ForeignKey(name="Fk_coupon_couponTemplates")
    private Coupon coupon;
	
	public int getMinimum_value() {
		return minimum_value;
	}
	public void setMinimum_value(int minimum_value) {
		this.minimum_value = minimum_value;
	}
	public int getMaximum_value() {
		return maximum_value;
	}
	public void setMaximum_value(int maximum_value) {
		this.maximum_value = maximum_value;
	}
	public Date getExpiry() {
		return expiry;
	}
	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public NetworkStatus getNetworkStatus() {
		return networkStatus;
	}
	public void setNetworkStatus(NetworkStatus networkStatus) {
		this.networkStatus = networkStatus;
	}
	public Coupon getCoupon() {
		return coupon;
	}
	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}
	public Restaurant getRestaurant() {
		return restaurant;
	}
	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

}
*/
