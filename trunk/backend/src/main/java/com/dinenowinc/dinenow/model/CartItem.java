package com.dinenowinc.dinenow.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.dinenowinc.dinenow.model.helpers.BaseEntity;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Audited
@JsonIgnoreProperties(ignoreUnknown = true)
@NamedQueries({@NamedQuery(name="CartItem.GetAll", query = "from CartItem ci")})
@Table(name="cart_item")

public class CartItem extends BaseEntity {

	private BigDecimal price;
	
	private int quantity;

	private String note;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "id_item", foreignKey = @ForeignKey(name = "fk_id_item_ccart_item_item"))
  private Item item;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_cart", foreignKey=@ForeignKey(name = "id_cart_cart_FK_cart_item_Fk"))
	private Cart cart;
	
	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
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

  public Item getItem() {
    return item;
  }

  public void setItem(Item item) {
    this.item = item;
  }

	@Override
	public HashMap<String, Object> toDto() {
		HashMap<String, Object> dto = new LinkedHashMap<String, Object>();
		dto.put("id", this.getId());
		dto.put("price", this.getPrice());
		dto.put("quantity", this.getQuantity());
		dto.put("notes", this.getNote());
		return dto;
	}
}
