package com.dinenowinc.dinenow.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="restaurant_user")
@Audited
@NamedQueries({@NamedQuery(name="RestaurantUser.GetAll", query = "from RestaurantUser u where status=0")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestaurantUser extends BaseEntity{

	private static final long serialVersionUID = 2228118945733901398L;
	
	@Column(updatable = false, nullable = false,unique=true)
	private String email;
	@Column(nullable=false)
	private String first_name;
	@Column(nullable=false)
	private String last_name;
	@NotNull
	@JsonIgnore
	private String password;
	@Column(nullable=false)
	private String phone_number;
	@Column(nullable=false)
	private Date registered_date;
	
	/*@Column(nullable=false)
	private NetworkStatus networkStatus;
	
	public NetworkStatus getNetworkStatus() {
		return networkStatus;
	}

	public void setNetworkStatus(NetworkStatus networkStatus) {
		this.networkStatus = networkStatus;
	}
*/
	@JsonIgnore
	private UserRole role;
	

	@JsonIgnore
	private String reset_key;
		
//	private int numberOfOrders;
	
	@Transient
	@Column(length = 10000000, name="card_strip")
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String cardStrip = "[]";
	
	@Transient
	private String customer_stripe;
	
	@Transient
	private String plan_stripe;
	
	@Transient
	private String subscriptions_stripe;
	
	
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinTable(name = "role_restaurant_user", joinColumns = @JoinColumn(name = "id_restaurant_user"), inverseJoinColumns = @JoinColumn(name = "id_role"))
	@ForeignKey(name = "Fk_roles_restaurant_user")
	private Set<Role> roles = new HashSet<Role>();

	//	public String getRestaurantID()
//	{
//		return id_restaurant;
//	}
//	public void setRestaurantID(String restaurantID)
//	{
//		this.id_restaurant=restaurantID;
//	}
	/*public RestaurantUser() {
	}

	public RestaurantUser(String email){
		this.email = email;
	}
	
	
	
	public RestaurantUser(String email, String password,UserRole role,String firstName,String lastName,String phoneNumber,
			Date registeredDate,String createdBy,Date createdDate) {
		this.email = email;
		this.password = password;
		this.role = role;
		this.first_name=firstName;
		this.last_name=lastName;
		this.phone_number=phoneNumber;
		this.registered_date=registeredDate;
//		this.id_restaurant=restaurantID;
		this.setCreatedBy(createdBy);
		this.setCreatedDate(createdDate);
	}*/
	

	public List<PaymentMethod> getCardStrip() {
		try {
			return new ObjectMapper().readValue(this.cardStrip, new TypeReference<List<PaymentMethod>>(){});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<PaymentMethod>();
	}

	public void setCardStrip(List<PaymentMethod> tokenStrip) {
		try {
			this.cardStrip = new ObjectMapper().writeValueAsString(tokenStrip);
		} catch (IOException e) {
			this.cardStrip = "[]";
			e.printStackTrace();
		}
	}
	
	public void addCardStrip(PaymentMethod paymentMethod){
			List<PaymentMethod> paymentMethods = getCardStrip();
			if (!isExistToken(paymentMethod)) {
				paymentMethods.add(paymentMethod);
				setCardStrip(paymentMethods);
			}
	}
	
	private boolean isExistToken(PaymentMethod method){
		for (PaymentMethod iterable_element : getCardStrip()) {
			if (method.getCardStripe().equals(iterable_element.getCardStripe())) {
				return true;
			}
		}
		return false;
	}
	
	
	public void deleteTokenStrip(String token) {
		List<PaymentMethod> paymentMethods = getCardStrip();
		
		for (int i = 0; i < paymentMethods.size(); i++) {
			if (paymentMethods.get(i) == null) {
				paymentMethods.remove(i);
			}
		}
		for (PaymentMethod paymentMethod : paymentMethods) {
			if (isExistToken(paymentMethod)) {
				paymentMethods.remove(paymentMethod);
				setCardStrip(paymentMethods);
			}
		}
	}
	
	public String getSubscriptionsStripe() {
		return subscriptions_stripe;
	}

	public void setSubscriptionsStripe(String subscriptionsStripe) {
		this.subscriptions_stripe = subscriptionsStripe;
	}
	
	public String getPlanStripe() {
		return plan_stripe;
	}

	public void setPlanStripe(String planStripe) {
		this.plan_stripe = planStripe;
	}
	
	public Date getRegisteredDate() {
		return registered_date;
	}

	public void setRegisteredDate(Date registeredDate) {
		this.registered_date = registeredDate;
	}

	public String getPhone() {
		return phone_number;
	}

	public void setPhone(String phone) {
		this.phone_number = phone;
	}

	
	
	
	public String getFirstName() {
		return first_name;
	}

	public void setFirstName(String firstName) {
		this.first_name = firstName;
	}

	public String getLastName() {
		return last_name;
	}

	public void setLastName(String lastName) {
		this.last_name = lastName;
	}
	
	public String getCustomerStripe() {
		return customer_stripe;
	}

	public void setCustomerStripe(String customerStripe) {
		this.customer_stripe = customerStripe;
	}
	
	
	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getResetKey() {
		return reset_key;
	}

	public void setResetKey(String reset_key) {
		this.reset_key = reset_key;
	}


	public Set<Role> getRoles() {
		return roles;
	}

	public void addRestaurant(Role role){
		getRoles().add(role);
	}

	@Override
	public HashMap<String, Object> toDto() {
		HashMap<String, Object> dto = new LinkedHashMap<String, Object>();
		dto.put("id", this.getId());
		dto.put("email", this.getEmail());
		dto.put("role", this.getRole());
		dto.put("firstName", this.getFirstName());
		dto.put("lastName", this.getLastName());
	//	dto.put("registeredDate", this.getRegisteredDate());
		dto.put("phone", this.getPhone());
	//	dto.put("customerStripe", this.getCustomerStripe());
		return dto;
	}
	
	
}
