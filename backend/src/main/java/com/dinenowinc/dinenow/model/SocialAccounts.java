package com.dinenowinc.dinenow.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="social_account")
@Audited
@NamedQueries({@NamedQuery(name="SocialAccounts.GetAll", query = "from SocialAccounts s")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class SocialAccounts extends BaseEntity{

	private static final long serialVersionUID = -1249581761414180188L;
	@Column(name="username",nullable=false,unique=true)
	private String userName;
	@Column(name="provider_name",nullable=false,unique=false)
	private String accountType;
	public SocialAccounts() {
	}
	
	public SocialAccounts(String social) {
		this.userName = social;
		this.accountType = "google";
/*		if(social.substring(0, 6).equalsIgnoreCase("google")){
			this.accountType = "google";
		} else {
			this.accountType = "facebook";
		}*/
		System.out.println(this.accountType+":::::::::::::::::::::");
	//	this.accountType = social.substring(0, 6).equalsIgnoreCase("google")?"google":"facebook";
		this.setCreatedDate(new Date());
		//super.setCreatedBy("dffgf");
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	
}
