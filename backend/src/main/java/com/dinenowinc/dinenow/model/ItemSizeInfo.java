package com.dinenowinc.dinenow.model;

import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="item_size_info")
@Audited
@NamedQueries({@NamedQuery(name="ItemSizeInfo.GetAll", query = "from ItemSizeInfo c")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemSizeInfo extends InfoBaseItemSize<Item,Size> {
		
	//@OneToOne(cascade = CascadeType.ALL, fetch= FetchType.LAZY)	
    //private final Item item = new Item();
	
	public ItemSizeInfo() {
		setCreatedBy("Auto");
		setCreatedDate(new Date());
	}
	//sua cho nay
	public ItemSizeInfo(Item item,Size size) {
		setItem(item);
		setSize(size);
	}
}
