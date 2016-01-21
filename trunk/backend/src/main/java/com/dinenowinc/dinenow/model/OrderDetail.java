package com.dinenowinc.dinenow.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name="order_detail")
@Audited
@NamedQueries({@NamedQuery(name="OrderDetail.GetAll", query = "from OrderDetail o")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDetail extends BaseEntity{

	private double price;
	
	private int quantity;
	
	@Column( name="note")
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String note;
	

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public HashMap<String, Object> toDto() {
		HashMap<String, Object> dto = new HashMap<String, Object>();
		dto.put("id", this.getId());
		dto.put("price", this.getPrice());
		dto.put("quantity", this.getQuantity());
		dto.put("notes", this.getNote());
		return dto;
	}
}
