package com.dinenowinc.dinenow.model;

import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.dinenowinc.dinenow.model.helpers.BaseEntity;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Audited
@NamedQueries({@NamedQuery(name="Review.GetAll", query = "from Review r")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Review extends BaseEntity {

	private int rating;
	
	private String reviews;
	
	private String customerName;

	@ManyToOne(cascade = {CascadeType.PERSIST},  fetch= FetchType.LAZY)
    @JoinColumn(name="id_review_criteria")
	@ForeignKey(name="Fk_review_reviewCriteria")
    private ReviewCriteria reviewCriteria;

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getReviews() {
		return reviews;
	}

	public void setReviews(String reviews) {
		this.reviews = reviews;
	}

	public ReviewCriteria getReviewCriteria() {
		return reviewCriteria;
	}

	public void setReviewCriteria(ReviewCriteria reviewCriteria) {
		this.reviewCriteria = reviewCriteria;
	}
	

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	

	@Override
	public HashMap<String, Object> toDto() {
		HashMap<String, Object> dto = new LinkedHashMap<String, Object>();;
		dto.put("id", this.getId());
		dto.put("rating", this.getRating());	
		dto.put("reviews", this.getReviews());	
		dto.put("customerName", this.getCustomerName());
		dto.put("createdDate", this.getCreatedDate());
		return dto;
	}
}
