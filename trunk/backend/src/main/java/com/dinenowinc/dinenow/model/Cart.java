package com.dinenowinc.dinenow.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.dinenowinc.dinenow.model.helpers.AvailabilityStatus;
import com.dinenowinc.dinenow.model.helpers.BaseEntity;
import com.dinenowinc.dinenow.model.helpers.OrderStatus;
import com.dinenowinc.dinenow.model.helpers.OrderType;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="\"Cart\"")
@Audited
@NamedQueries({@NamedQuery(name="Cart.GetAll", query = "from Cart c")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cart extends BaseEntity {

	@Column(name="order_status")
	private OrderStatus orderStatus;
	
	@Column(name="type")
	private OrderType orderType;

	
	private AvailabilityStatus availstatus;
	private double discount;
	private double tax;
	private double total;
	
	@OneToOne
	@JoinColumn (name="id_customer",unique=true)
	private Customer customer;
	
	
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@OneToMany(cascade = {CascadeType.PERSIST}, fetch= FetchType.LAZY ,mappedBy="cart" ,orphanRemoval=true)
    private final Set<CartItem> cartItems = new HashSet<CartItem>();
	
	public Cart() {
	}

	
	
	public Cart(OrderStatus orderStatus, OrderType orderType,
			AvailabilityStatus availstatus, double discount, double tax,
			double total) {
		this.orderStatus = orderStatus;
		this.orderType = orderType;
		this.availstatus = availstatus;
		this.discount = discount;
		this.tax = tax;
		this.total = total;
	}


	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}


	public double getTax() {
		return tax;
	}

	public void setTax(double tax) {
		this.tax = tax;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}
	
	public AvailabilityStatus getAvailstatus() {
		return availstatus;
	}

	public void setAvailstatus(AvailabilityStatus availstatus) {
		this.availstatus = availstatus;
	}
	
	public Set<CartItem> getCartItems() {
		return cartItems;
	}
	
	public void addCartItem(CartItem item){
		getCartItems().add(item);
	}


	@Override
	public HashMap<String, Object> toDto() {
		HashMap<String, Object> dto = new LinkedHashMap<String, Object>();
		dto.put("id", this.getId());
		dto.put("orderType", this.getOrderType());
		dto.put("orderType", this.getOrderType());
		dto.put("status", this.getAvailstatus());
		dto.put("total", this.getTotal());
		dto.put("tax", this.getTax());
		dto.put("orderStatus", this.getOrderStatus());
		return dto;
	}

}
