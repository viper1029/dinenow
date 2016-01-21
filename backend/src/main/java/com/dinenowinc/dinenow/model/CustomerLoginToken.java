package com.dinenowinc.dinenow.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="customer_login_token")
@Audited
@NamedQueries({@NamedQuery(name="CustomerLoginToken.GetAll", query = "from CustomerLoginToken c")})
@JsonIgnoreProperties(ignoreUnknown = true)

public class CustomerLoginToken extends BaseEntity {
	
	private static final long serialVersionUID = 691091407346434736L;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="id_customer")
	@ForeignKey(name="id_customer_Customer_FK")
	private Customer customer; 
	
	@Column(nullable=false)
	private String token;
	
	@Column(nullable=false)
	private String duration;
	
	@Column(nullable=false)
	private boolean prasent;
	
	@Column(nullable=false)
	private Date created;
	
	@Column(nullable=false)
	private Date expires;
	
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public boolean isPrasent() {
		return prasent;
	}

	public void setPrasent(boolean prasent) {
		this.prasent = prasent;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getExpires() {
		return expires;
	}

	public void setExpires(Date expires) {
		this.expires = expires;
	}
}
