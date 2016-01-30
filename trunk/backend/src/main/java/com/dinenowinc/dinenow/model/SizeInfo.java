package com.dinenowinc.dinenow.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="size_info")
@Audited
@NamedQueries({@NamedQuery(name="SizeInfo.GetAll", query = "from SizeInfo a")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class SizeInfo extends InfoSize<Size>{

	private double price;
	
	public SizeInfo() {
		setCreatedBy("Auto");
		setCreatedDate(new Date());
	}
	
	public SizeInfo(Size addon) {
		setSize(addon);
	}
	
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}
