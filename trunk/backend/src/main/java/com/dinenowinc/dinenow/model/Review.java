package com.dinenowinc.dinenow.model;

import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.persistence.Entity;

import com.dinenowinc.dinenow.model.helpers.BaseEntity;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Audited
@NamedQueries({@NamedQuery(name="Review.GetAll", query = "from Review r")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Review extends BaseEntity {

	private String customerName;

	private int rating;
	
	private String comment;

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String reviews) {
		this.comment = reviews;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@Override
	public HashMap<String, Object> toDto() {
		HashMap<String, Object> dto = new LinkedHashMap<>();;
		dto.put("id", this.getId());
		dto.put("rating", this.getRating());	
		dto.put("comment", this.getComment());
		dto.put("customerName", this.getCustomerName());
		dto.put("createdDate", this.getCreatedDate());
		return dto;
	}
}
