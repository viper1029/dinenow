package com.dinenowinc.dinenow.model;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@NamedQueries({@NamedQuery(name="Coupon.GetAll", query = "from Coupon c")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Coupon extends BaseEntity{

	private int minimum_value;
	private int maximum_value;
	private int discount_value;
	private long coupon_code;
	private Date start_date;
	private Date end_date;

	private CouponType coupon_type;
	private NetworkStatus networkStatus;
	
	
	@OneToMany(cascade = {CascadeType.PERSIST},  fetch= FetchType.LAZY)
    @JoinColumn(name="id_coupon",nullable=true)
	@ForeignKey(name="Fk_coupon_order")
    private final Set<Order> order = new HashSet<Order>();
	
	@OneToMany(cascade = {CascadeType.PERSIST},  fetch= FetchType.LAZY)
    @JoinColumn(name="id_coupon",nullable=true)
	@ForeignKey(name="Fk_coupon_cart")
    private final Set<Cart> carts = new HashSet<Cart>();
	
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinTable(name = "coupon_restaurant", joinColumns = @JoinColumn(name = "id_coupon"), inverseJoinColumns = @JoinColumn(name = "id_restaurant"))
	@ForeignKey(name = "Fk_coupon_restaurant")
	private Set<Restaurant> restaurants = new HashSet<Restaurant>();
	
	public Set<Order> getOrder() {
		return order;
	}
	
	public void addOrder(Order order){
    	getOrder().add(order);
    }
	public Set<Restaurant> getRestaurants() {
		return restaurants;
	}
	public void addRestaurants(Restaurant restaurants) {
		getRestaurants().add(restaurants);
	}

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
	public int getDiscount_value() {
		return discount_value;
	}
	public void setDiscount_value(int discount_value) {
		this.discount_value = discount_value;
	}
	public long getCoupon_code() {
		return coupon_code;
	}
	public void setCoupon_code(long coupon_code) {
		this.coupon_code = coupon_code;
	}
	public Date getStart_date() {
		return start_date;
	}
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	public CouponType getCoupon_type() {
		return coupon_type;
	}
	public void setCoupon_type(CouponType coupon_type) {
		this.coupon_type = coupon_type;
	}
	public NetworkStatus getNetworkStatus() {
		return networkStatus;
	}
	public void setNetworkStatus(NetworkStatus networkStatus) {
		this.networkStatus = networkStatus;
	}
	
	@Override
	public HashMap<String, Object> toDto() {
		HashMap<String, Object> dto = new HashMap<String, Object>();
		dto.put("id", this.getId());
		dto.put("couponCode", this.getCoupon_code());
		dto.put("couponType", this.getCoupon_type());		
		dto.put("discountValue", this.getDiscount_value());
		dto.put("startDate", this.getStart_date());
		dto.put("endDate", this.getEnd_date());
		dto.put("maximumValue", this.getMaximum_value());
		dto.put("manimumValue", this.getMinimum_value());
		dto.put("networkStatus", this.getNetworkStatus());
		return dto;
	}	
}
