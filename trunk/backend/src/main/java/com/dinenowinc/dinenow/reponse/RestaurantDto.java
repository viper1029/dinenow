package com.dinenowinc.dinenow.reponse;
import com.dinenowinc.dinenow.model.*;


public class RestaurantDto {
	/*
	 * 	private static final long serialVersionUID = -524833031492135630L;
	
	private String restaurantName;
	
	private String description;
 
	private EStatus status;
	
	private String phoneNumber;
	
	private String website;
	
	private String address;
	
	private String tax;
	
	private EPaymentTypes deliveryPaymentType;
	
	private boolean acceptDeliveryOrders;
	
	private boolean acceptTakeOutOrders;
	 */
	private String id;
	
	private String restaurantName;
	
	private String description;
 
	private NetworkStatus status;
	
	private String phoneNumber;
	
	private String website;
	
	private String address;
	
	private String tax;
	
	private PaymentType deliveryPaymentType;
	
	private boolean acceptDeliveryOrders;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getRestaurantName() {
		return restaurantName;
	}

	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public NetworkStatus getStatus() {
		return status;
	}

	public void setStatus(NetworkStatus status) {
		this.status = status;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTax() {
		return tax;
	}

	public void setTax(String tax) {
		this.tax = tax;
	}

	public PaymentType getDeliveryPaymentType() {
		return deliveryPaymentType;
	}

	public void setDeliveryPaymentType(PaymentType deliveryPaymentType) {
		this.deliveryPaymentType = deliveryPaymentType;
	}

	public boolean isAcceptDeliveryOrders() {
		return acceptDeliveryOrders;
	}

	public void setAcceptDeliveryOrders(boolean acceptDeliveryOrders) {
		this.acceptDeliveryOrders = acceptDeliveryOrders;
	}

	public boolean isAcceptTakeOutOrders() {
		return acceptTakeOutOrders;
	}

	public void setAcceptTakeOutOrders(boolean acceptTakeOutOrders) {
		this.acceptTakeOutOrders = acceptTakeOutOrders;
	}

	private boolean acceptTakeOutOrders;
}
