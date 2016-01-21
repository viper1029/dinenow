package com.dinenowinc.dinenow.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="item_info")
@Audited
@NamedQueries({@NamedQuery(name="ItemInfo.GetAll", query = "from ItemInfo c")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemInfo extends InfoItem<Item> {
		
	//@OneToOne(cascade = CascadeType.ALL, fetch= FetchType.LAZY)	
    //private final Item item = new Item();
	
	@Column(nullable=false,columnDefinition="Decimal(10,2)")
	private double price;
	
	
	public ItemInfo() {
		setCreatedBy("Auto");
		setCreatedDate(new Date());
	}
	
	public ItemInfo(Item item,String createdBy,Date createdDate) {
		setItem(item);
		this.setCreatedBy(createdBy);
		this.setCreatedDate(createdDate);
	}
	
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}
