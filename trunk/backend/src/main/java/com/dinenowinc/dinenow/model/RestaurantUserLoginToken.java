package com.dinenowinc.dinenow.model;

import java.io.Serializable;
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
@Table(name="restaurant_user_login_token")
//@Audited
@NamedQueries({@NamedQuery(name="RestaurantUserLoginToken.GetAll", query = "from RestaurantUserLoginToken r")})
@JsonIgnoreProperties(ignoreUnknown = true)

public class RestaurantUserLoginToken extends BaseEntity{

	
	private static final long serialVersionUID = 691091407346434736L;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="id_restaurant_user")
	@ForeignKey(name="id_restaurant_user_RestaurantUser_FK")
	private RestaurantUser restaurantUser; 
	
	@Column(nullable=false)
	private String token;
	
	@Column(nullable=false)
	private String duration;
	
	public RestaurantUser getRestaurantUser() {
		return restaurantUser;
	}

	public void setRestaurantUser(RestaurantUser restaurantUser) {
		this.restaurantUser = restaurantUser;
	}

	@Column(nullable=false)
	private boolean prasent;
	
	@Column(nullable=false)
	private Date created;
	
	@Column(nullable=false)
	private Date expires;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean isPrasent() {
		return prasent;
	}

	public void setPrasent(boolean prasent) {
		this.prasent = prasent;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
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
