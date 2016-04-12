package com.dinenowinc.dinenow.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.dinenowinc.dinenow.model.helpers.BaseEntity;
import com.dinenowinc.dinenow.model.helpers.NetworkStatus;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name="promo_code")
@Audited
@NamedQueries({@NamedQuery(name="PromoCode.GetAll", query = "from PromoCode pc")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class PromoCode extends BaseEntity {

	private String promo_name;
	private Date expiry;
	private int value;
	
	@Column( name="code_type")
	private CodeType codeType;
	private NetworkStatus networkStatus;
	
	@OneToMany(cascade = {CascadeType.PERSIST}, fetch= FetchType.LAZY)
    @JoinColumn(name="id_promo_code",nullable=false)
	@ForeignKey(name="id_promo_code_promoCodeRestaurant_promoCode_FK")
    private final Set<PromoCodeRestaurant> promoCodeRestaurant = new HashSet<PromoCodeRestaurant>();
	
	public String getPromo_name() {
		return promo_name;
	}
	public void setPromo_name(String promo_name) {
		this.promo_name = promo_name;
	}
	public Date getExpiry() {
		return expiry;
	}
	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public CodeType getCodeType() {
		return codeType;
	}
	public void setCodeType(CodeType codeType) {
		this.codeType = codeType;
	}
	public NetworkStatus getNetworkStatus() {
		return networkStatus;
	}
	public void setNetworkStatus(NetworkStatus networkStatus) {
		this.networkStatus = networkStatus;
	}
	
	public Set<PromoCodeRestaurant> getPromoCodeRestaurant() {
		return promoCodeRestaurant;
	}
	
	
}
