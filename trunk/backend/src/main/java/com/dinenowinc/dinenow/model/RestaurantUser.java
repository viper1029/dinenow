package com.dinenowinc.dinenow.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.dinenowinc.dinenow.model.helpers.BaseEntity;
import com.dinenowinc.dinenow.model.helpers.UserRole;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "restaurant_user")
@Audited
@NamedQueries({ @NamedQuery(name = "RestaurantUser.GetAll", query = "from RestaurantUser u where status=0") })
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestaurantUser extends BaseEntity {

  private static final long serialVersionUID = 2228118945733901398L;

  @Column(updatable = false, nullable = false, unique = true)
  private String email;

  @Column(name = "name", nullable = false)
  private String name;

  @NotNull
  @JsonIgnore
  private String password;

  @Column(name = "phone_number", nullable = false)
  private String phoneNumber;

  @Column(name = "registered_date", nullable = false)
  private Date registeredDate;

  @JsonIgnore
  private UserRole role;

  @JsonIgnore
  private String reset_key;

  @Transient
  private String cardStrip = "";

  @Transient
  private String customerStripe;

  @Transient
  private String planStripe;

  @Transient
  private String subscriptionStripe;

  public List<PaymentMethod> getCardStrip() {
    try {
      return new ObjectMapper().readValue(this.cardStrip, new TypeReference<List<PaymentMethod>>() {
      });
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return new ArrayList<>();
  }

  public void setCardStrip(List<PaymentMethod> tokenStrip) {
    try {
      this.cardStrip = new ObjectMapper().writeValueAsString(tokenStrip);
    }
    catch (IOException e) {
      this.cardStrip = "[]";
      e.printStackTrace();
    }
  }

  public void addCardStrip(PaymentMethod paymentMethod) {
    List<PaymentMethod> paymentMethods = getCardStrip();
    if (!isExistToken(paymentMethod)) {
      paymentMethods.add(paymentMethod);
      setCardStrip(paymentMethods);
    }
  }

  private boolean isExistToken(PaymentMethod method) {
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

  public String getSubscriptionStripe() {
    return subscriptionStripe;
  }

  public void setSubscriptionStripe(String subscriptionStripe) {
    this.subscriptionStripe = subscriptionStripe;
  }

  public String getPlanStripe() {
    return planStripe;
  }

  public void setPlanStripe(String planStripe) {
    this.planStripe = planStripe;
  }

  public Date getRegisteredDate() {
    return registeredDate;
  }

  public void setRegisteredDate(Date registeredDate) {
    this.registeredDate = registeredDate;
  }

  public String getPhone() {
    return phoneNumber;
  }

  public void setPhone(String phone) {
    this.phoneNumber = phone;
  }


  public String getName() {
    return name;
  }

  public void setName(String fullName) {
    this.name = fullName;
  }

  public String getCustomerStripe() {
    return customerStripe;
  }

  public void setCustomerStripe(String customerStripe) {
    this.customerStripe = customerStripe;
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

  @Override
  public HashMap<String, Object> toDto() {
    HashMap<String, Object> dto = new LinkedHashMap<>();
    dto.put("id", this.getId());
    dto.put("email", this.getEmail());
    dto.put("role", this.getRole());
    dto.put("fullName", this.getName());
    dto.put("phone", this.getPhone());
    return dto;
  }


}
