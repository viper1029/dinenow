package com.dinenowinc.dinenow.model;

import java.util.HashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="\"Cart_Item\"")
@Audited
@NamedQueries({@NamedQuery(name="CartItem.GetAll", query = "from CartItem ci")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartItem extends BaseEntity {

	private double price;
	
	private int quantity;
		
	@Column( name="note")
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String note;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_cart", foreignKey=@ForeignKey(name = "id_cart_cart_FK_cart_item_Fk"))
	private Cart cart;
	
	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

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
