package com.dinenowinc.dinenow.model;

import com.dinenowinc.dinenow.model.helpers.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

@Entity
@Audited
@NamedQueries({ @NamedQuery(name = "Customer.GetAll", query = "from Customer c") })
@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer extends BaseEntity {

  private static final long serialVersionUID = -4992650490443491743L;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "first_name", nullable = false, unique = false)
  private String firstName;

  @Column(name = "last_name", nullable = false, unique = false)
  private String lastName;

  @JsonIgnore
  @Column(name = "password", nullable = false, unique = false)
  private String password;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "phone_number_valid")
  private boolean isPhoneNumberValid;

  @JsonIgnore
  @Column(name = "reset_key")
  private String resetKey;

  @Column(name = "recent_key_expiry")
  @JsonIgnore
  private Date resetKeyTime;

  @JsonIgnore
  @Column(name = "validation_code")
  private String validationCode;

  @Column(name = "validation_code_expiry")
  @JsonIgnore
  private Date validationCodeExpiry;

  @Column(name = "customer_stripe")
  private String customerStripe;

  @Column(length = 10000000)
  @Lob
  @Type(type = "org.hibernate.type.TextType")
  private String card_strip = "[]";

  @JsonIgnore
  @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  @JoinColumn(name = "id_customer", foreignKey = @ForeignKey(name = "id_customer_addressbook_fk"))
  private final Set<AddressBook> addressBooks = new HashSet<AddressBook>();

  @JsonIgnore
  @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  @JoinColumn(name = "id_customer", foreignKey = @ForeignKey(name = "Fk_customer_socialAccounts"))
  private final Set<SocialAccounts> socialAccounts = new HashSet<SocialAccounts>();


  @JsonIgnore
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "id_customer", foreignKey = @ForeignKey(name = "Fk_customer_orders"))
  private final Set<Order> orders = new HashSet<Order>();

  @OneToOne(mappedBy = "customer", fetch = FetchType.LAZY)
  private Cart cart;

  @JsonIgnore
  @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  @JoinColumn(name = "id_customer", foreignKey = @ForeignKey(name = "Fk_customer_promoCode"))
  private final Set<PromoCode> promoCode = new HashSet<PromoCode>();

  @Column(columnDefinition = "Decimal(10,2)")
  private double points;

  @Column(name = "phone_point")
  private boolean phonePoint;

  public Cart getCart() {
    return cart;
  }

  public void setCart(Cart cart) {
    this.cart = cart;
  }

  public double getPoint() {
    return points;
  }

  public boolean isPhonePoint() {
    return phonePoint;
  }

  public void setPhonePoint(boolean phone_point) {
    this.phonePoint = phone_point;
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
    }
    catch (IOException e) {
      this.card_strip = "";
      e.printStackTrace();
    }
  }

  public void addCardStrip(PaymentMethod paymentMethod) {
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
    return customerStripe;
  }

  public void setCustomerStripe(String customer_stripe) {
    this.customerStripe = customer_stripe;
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
    return validationCode;
  }

  public void setValidationCode(String validationCode) {
    this.validationCode = validationCode;
  }

  public Date getValidationCodeTime() {
    return validationCodeExpiry;
  }

  public void setValidationCodeTime(Date validationCodeTime) {
    this.validationCodeExpiry = validationCodeTime;
  }

  public Set<Order> getOrders() {
    return orders;
  }

  public void addCustomerOrder(Order order) {
    getOrders().add(order);
  }

  public String getFirstName() {
    return firstName;
  }


  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }


  public String getLastName() {
    return lastName;
  }


  public void setLastName(String lastName) {
    this.lastName = lastName;
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
    return phoneNumber;
  }


  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }


  public boolean getIsPhoneNumberValid() {
    return isPhoneNumberValid;
  }

  public void setIsPhoneNumberValid(boolean phone_number_valid) {
    this.isPhoneNumberValid = phone_number_valid;
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
    return resetKey;
  }

  public void setResetKey(String resetKey) {
    this.resetKey = resetKey;
  }

  public Date getResetKeyTime() {
    return resetKeyTime;
  }

  public void setResetKeyTime(Date resetKeyTime) {
    this.resetKeyTime = resetKeyTime;
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
