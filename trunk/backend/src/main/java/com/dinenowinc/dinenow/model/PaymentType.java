package com.dinenowinc.dinenow.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.envers.Audited;

@Entity
@Table(name="payment_type")
@Audited
@NamedQueries({@NamedQuery(name="PaymentType.GetAll", query = "from PaymentType p")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentType extends BaseEntity {

	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public HashMap<String, Object> toDto() {
		HashMap<String, Object> dto = new LinkedHashMap<String, Object>();
		dto.put("id", this.getId());
		dto.put("name", this.getName());	
		return dto;
	}
	
}
