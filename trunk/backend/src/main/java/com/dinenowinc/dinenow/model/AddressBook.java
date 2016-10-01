package com.dinenowinc.dinenow.model;

import com.dinenowinc.dinenow.model.helpers.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Entity
@Audited
@NamedQueries({ @NamedQuery(name = "AddressBook.GetAll", query = "from AddressBook a") })
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "address_book")
public class AddressBook extends BaseEntity {

  private static final long serialVersionUID = -7491749432942752355L;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "address_1", nullable = false)
  private String address1;

  @Column(name = "address_2")
  private String address2;

  @Column(name = "city", nullable = false)
  private String city;

  @Column(name = "province", nullable = false)
  private String province;

  @Column(name = "country", nullable = false)
  private String country;

  @Column(name = "postal_code", nullable = false)
  private String postalCode;

  @Column(name = "delivery_instructions")
  private String deliveryInstructions;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress1() {
    return address1;
  }

  public void setAddress1(String address_1) {
    this.address1 = address_1;
  }

  public String getAddress2() {
    return address2;
  }

  public void setAddress2(String address_2) {
    this.address2 = address_2;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postal_code) {
    this.postalCode = postal_code;
  }

  public String getDeliveryInstructions() {
    return deliveryInstructions;
  }

  public void setDeliveryInstructions(String delivery_instructions) {
    this.deliveryInstructions = delivery_instructions;
  }

  @Override
  public HashMap<String, Object> toDto() {
    HashMap<String, Object> dto = new LinkedHashMap<>();
    dto.put("id", this.getId());
    dto.put("name", this.getName());
    dto.put("address1", this.getAddress1());
    dto.put("address2", this.getAddress2());
    dto.put("city", this.getCity());
    dto.put("province", this.getProvince());
    dto.put("country", this.getCountry());
    dto.put("postalCode", this.getPostalCode());
    dto.put("deliveryInstructions", this.getDeliveryInstructions());
    return dto;
  }

}
