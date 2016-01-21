package com.dinenowinc.dinenow.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="item_size_price")
@Audited
@NamedQueries({@NamedQuery(name="ItemSizePrice.GetAll", query = "from ItemSizePrice c")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemSizePrice extends InfoBaseItemSize<Item,Size>{
	@Column(nullable=false)
	private String itemStatus;
	@Column(nullable=false,columnDefinition="Decimal(10,2)")
	private double price;

	public double getPrice()
	{
		return price;
	}
	public void setPrice(double price)
	{
		this.price=price;
	}
	
	public String getItemStatus() {
		return itemStatus;
	}
	public void setItemStatus(String itemStatus) {
		this.itemStatus = itemStatus;
	}
	public ItemSizePrice() {
	}
	
	public ItemSizePrice(Item item,Size size) {
		setItem(item);
		setSize(size);
	}
}
