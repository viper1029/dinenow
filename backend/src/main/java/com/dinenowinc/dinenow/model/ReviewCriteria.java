package com.dinenowinc.dinenow.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name="review_criteria")
@Audited
@NamedQueries({@NamedQuery(name="ReviewCriteria.GetAll", query = "from ReviewCriteria rc")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewCriteria extends BaseEntity{

	@Column(nullable=false,unique=true)
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
