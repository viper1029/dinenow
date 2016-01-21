package com.dinenowinc.dinenow.model;

import java.util.Date;
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
@Audited
@NamedQueries({@NamedQuery(name="Tax.GetAll", query = "from Tax t")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tax extends BaseEntity {

	@Column(nullable=false,unique=true)
	private String name;

	private String taxeDescription;

	@Column(nullable=false,columnDefinition="Decimal(10,2)", name="value")
	private double amount;
	
	public Tax() {
	}
	
	public Tax(String name, double value,String createdBy,Date createdDate) {
		this.name = name;
//		this.taxeDescription = description;
		this.amount = value;
		this.setCreatedBy(createdBy);
		this.setCreatedDate(createdDate);
	}
	
	
	
	public String getTaxeName() {
		return name;
	}
	public void setTaxeName(String taxeName) {
		this.name = taxeName;
	}
	public String getTaxeDescription() {
		return taxeDescription;
	}
	public void setTaxeDescription(String taxeDescription) {
		this.taxeDescription = taxeDescription;
	}
	public double getTaxeValue() {
		return amount;
	}
	public void setTaxeValue(double taxeValue) {
		this.amount = taxeValue;
	}
	
	
	@Override
	public HashMap<String, Object> toDto() {
		HashMap<String, Object> dto = new LinkedHashMap<String, Object>();
		dto.put("id", this.getId());
		dto.put("name", this.getTaxeName());
		dto.put("description", this.getTaxeDescription());
		dto.put("value", this.getTaxeValue());
		return dto;
	}
	
}
