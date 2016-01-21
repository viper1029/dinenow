package com.dinenowinc.dinenow.model;

import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="address_book")
@Audited
@NamedQueries({ @NamedQuery(name = "AddressBook.GetAll", query = "from AddressBook a") })
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressBook extends BaseEntity {

	private static final long serialVersionUID = -7491749432942752355L;
	

	@Column(name="name",nullable=false,unique=false)
	private String name;
	@Column(name="address_1", nullable=false, unique=false)
	private String address_1;
	@Column(name="address_2",nullable=false,unique=false)
	private String address_2;
	@Column(name="city",nullable=false,unique=false)
	private String city;
	@Column(name="province",nullable=false,unique=false)
	private String province;
	@Column(name="country",nullable=false,unique=false)
	private String country;
	@Column(name="postal_code",nullable=false,unique=false)
	private String postal_code;
	@Column(name="delivery_instructions",nullable=false,unique=false)
	private String delivery_instructions;

	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public String getAddress1()
	{
		return address_1;
	}
	public void setAddress1(String address_1)
	{
		this.address_1=address_1;
	}
	public String getAddress2()
	{
		return address_2;
	}
	public void setAddress2(String address_2)
	{
		this.address_2=address_2;
	}
	public String getCity()
	{
		return city;
	}
	public void setCity(String city)
	{
		this.city=city;
	}
	public String getProvince()
	{
		return province;
	}
	public void setProvince(String province)
	{
		this.province=province;
	}
	public String getCountry()
	{
		return country;
	}
	public void setCountry(String country)
	{
		this.country=country;
	}
	
	public String getPostalCode() {
		return postal_code;
	}

	public void setPostalCode(String postal_code) {
		this.postal_code = postal_code;
	}

	public String getDeliveryInstructions() {
		return delivery_instructions;
	}

	public void setDeliveryInstructions(String delivery_instructions) {
		this.delivery_instructions = delivery_instructions;
	}

	@Override
	public HashMap<String, Object> toDto() {
		HashMap<String, Object> dto = new LinkedHashMap<String, Object>();
		dto.put("id", this.getId());
		dto.put("name", this.getName());
		dto.put("address1", this.getAddress1());
		dto.put("address2",this.getAddress2());
		dto.put("city", this.getCity());
		dto.put("province", this.getProvince());
		dto.put("country", this.getCountry());
		dto.put("postalCode", this.getPostalCode());
		dto.put("deliveryInstructions", this.getDeliveryInstructions());
		
		return dto;
	}

}
