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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Audited
@NamedQueries({@NamedQuery(name="Customer.GetAll", query = "from Customer c")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer extends BaseEntity {

	private static final long serialVersionUID = -4992650490443491743L;
	
	@Column(name="email",nullable=false,unique=true)
	private String email;
	@Column(name="first_name",nullable=false,unique=false)
	private String first_name;
	@Column(name="last_name",nullable=false,unique=false)
	private String last_name;
//	private NetworkStatus status;
	@Column(name="password",nullable=false,unique=false)
	@JsonIgnore
	private String password;

	private String phone_number;

	private boolean phone_number_valid;
	
//	@Column( name="recent_addresses", columnDefinition="TEXT")
//	@Lob
//	private String recent_use_addresses = "[]";
	
	@JsonIgnore
	private String reset_key;
	
	@Column( name="recent_key_expiry")
	@JsonIgnore
	private Date reset_key_time;
	
	@JsonIgnore
	private String validation_code;
	
	@Column( name="validation_code_expiry")
	@JsonIgnore
	private Date validation_code_time;
	
	//private String address;
	
	private String customer_stripe;
	
	@Column(length = 10000000)
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String card_strip = "[]";
	
	@JsonIgnore
	@OneToMany(cascade = CascadeType.PERSIST, fetch= FetchType.LAZY)
    @JoinColumn(name="id_customer")
	@org.hibernate.annotations.ForeignKey(name="Fk_customer_addressBooks")
    private final Set<AddressBook> addressBooks = new HashSet<AddressBook>();
    
    @JsonIgnore
    @OneToMany(cascade = CascadeType.PERSIST, fetch= FetchType.LAZY)
    @JoinColumn(name="id_customer")
    @org.hibernate.annotations.ForeignKey(name="Fk_customer_socialAccounts")
    private final Set<SocialAccounts> socialAccounts = new HashSet<SocialAccounts>();
	

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,  fetch= FetchType.LAZY ,orphanRemoval=true)
    @JoinColumn(name="id_customer")
    @org.hibernate.annotations.ForeignKey(name="Fk_customer_orders")
    private final Set<Order> orders = new HashSet<Order>();
    
    @OneToOne(mappedBy="customer",fetch= FetchType.LAZY)
    private Cart cart;
    
    public Cart getCart() {
		return cart;
	}


	public void setCart(Cart cart) {
		this.cart = cart;
	}


	@JsonIgnore
    @OneToMany(cascade = CascadeType.PERSIST,  fetch= FetchType.LAZY)
    @JoinColumn(name="id_customer")
    @org.hibernate.annotations.ForeignKey(name="Fk_customer_promoCode")
    private final Set<PromoCode> promoCode = new HashSet<PromoCode>();
    


    @Column(columnDefinition="Decimal(10,2)")
    private double points;

    
    private boolean phone_point;

	/*public Customer() {
	}
	
	public Customer(String firstname, String lastname, String email, String password, String phone,String address, String social) {
		this.first_name = firstname;
		this.last_name = lastname;
		this.email = email;
		this.password = password;
		this.phone_number = phone;
		this.address = address;
		addSocialAccounts(social);
	}*/
	


	public double getPoint() {
		return points;
	}

	public boolean isPhone_point() {
		return phone_point;
	}

	public void setPhone_point(boolean phone_point) {
		this.phone_point = phone_point;
	}

	public void setPoint(double point) {
		this.points = point;
	}
	
	public List<PaymentMethod> getCardStrip() {
		/*try {
			return new ObjectMapper().readValue(this.card_strip, new TypeReference<List<PaymentMethod>>(){});
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		return new ArrayList<PaymentMethod>();
	}

	public void setCardStrip(List<PaymentMethod> tokenStrip) {
		try {
			this.card_strip = new ObjectMapper().writeValueAsString(tokenStrip);
		} catch (IOException e) {
			this.card_strip = "";
			e.printStackTrace();
		}
	}
	
	public void addCardStrip(PaymentMethod paymentMethod){
			List<PaymentMethod> paymentMethods = getCardStrip();
			paymentMethods.add(paymentMethod);
			setCardStrip(paymentMethods);
	}
	public void deleteCardStrip(String token) {
		List<PaymentMethod> paymentMethods = getCardStrip();
		for (PaymentMethod paymentMethod : paymentMethods) {
			if (paymentMethod.getCardStripe().equals(token)) {
				paymentMethods.remove(paymentMethod);
			}
		}
		setCardStrip(paymentMethods);
	}
	
	
	
	public String getCustomerStripe() {
		return customer_stripe;
	}

	public void setCustomerStripe(String customer_stripe) {
		this.customer_stripe = customer_stripe;
	}
	
	
	public Set<SocialAccounts> getSocialAccounts() {
		return socialAccounts;
	}
	
	public void addSocialAccounts(String social) {
		SocialAccounts acc = new SocialAccounts(social);
		acc.setCreatedBy("acaccsd");
		getSocialAccounts().add(acc);
	}
	
	
	
//	public List<String> getRecentUseAddresses() {
//		try {
//			return new ObjectMapper().readValue(recent_use_addresses, new TypeReference<List<String>>() {});
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return new ArrayList<String>();
//	}
//
//	public void setRecentUseAddresses(List<String> recentUseAddresses) {
//		try {
//			this.recent_use_addresses = new ObjectMapper().writeValueAsString(recentUseAddresses);
//		} catch (IOException e) {
//			this.recent_use_addresses = "";
//			e.printStackTrace();
//		}
//	}
	
	
	public String getValidationCode() {
		return validation_code;
	}

	public void setValidationCode(String validationCode) {
		this.validation_code = validationCode;
	}

	public Date getValidationCodeTime() {
		return validation_code_time;
	}

	public void setValidationCodeTime(Date validationCodeTime) {
		this.validation_code_time = validationCodeTime;
	}
	
	public Set<Order> getOrders() {
		return orders;
	}
	
	public void addCustomerOrder(Order order){
		getOrders().add(order);
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


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


//	public NetworkStatus getStatus() {
//		return status;
//	}
//
//
//
//
//	public void setStatus(NetworkStatus status) {
//		this.status = status;
//	}



	public String getPassword() {
		return password;
	}




	public void setPassword(String password) {
		this.password = password;
	}




	public String getPhoneNumber() {
		return phone_number;
	}




	public void setPhoneNumber(String phoneNumber) {
		this.phone_number = phoneNumber;
	}




	public boolean isPhone_number_valid() {
		return phone_number_valid;
	}

	public void setPhone_number_valid(boolean phone_number_valid) {
		this.phone_number_valid = phone_number_valid;
	}

//	public String getAddress() {
//		return address;
//	}
//
//	public void setAddress(String address) {
//		this.address = address;
//	}

	public Set<AddressBook> getAddressBooks() {
		return addressBooks;
	}
	
	public void addAddressBook(AddressBook addressBook) {
		getAddressBooks().add(addressBook);
	}
	
	
	public String getResetKey() {
		return reset_key;
	}

	public void setResetKey(String resetKey) {
		this.reset_key = resetKey;
	}

	public Date getResetKeyTime() {
		return reset_key_time;
	}

	public void setResetKeyTime(Date resetKeyTime) {
		this.reset_key_time = resetKeyTime;
	}
	
	@Override
	public HashMap<String, Object> toDto() {
		HashMap<String, Object> dto = new LinkedHashMap<String, Object>();
		dto.put("id", this.getId());
		dto.put("email", this.getEmail());
		dto.put("firstName", this.getFirstName());
		dto.put("lastName", this.getLastName());
	//	dto.put("password", this.getPassword());
		dto.put("phoneNumber", this.getPhoneNumber());
	//	dto.put("registeredDate", this.getCreatedDate());
	//	dto.put("address", this.getAddress());
	//	dto.put("recentUseAddresses", this.getRecentUseAddresses());
	//	dto.put("socialAccounts", this.getSocialAccounts());
	//	dto.put("addressBooks", this.getAddressBooks());
		
		return dto;
	}
	
	

}
