package com.dinenowinc.dinenow.model;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import org.hibernate.annotations.ForeignKey;

import com.fasterxml.jackson.annotation.JsonIgnore;
@MappedSuperclass
public class InfoBaseItemSize <Item extends BaseEntity, Size extends BaseEntity> extends BaseEntity{
	@OneToOne(cascade = CascadeType.PERSIST, fetch= FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name="id_item",nullable=false)
	@ForeignKey(name="Fk_item_itemSizeInfo")
    private Item item = null;
	public Item getItem() {
		return this.item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	
	
	@OneToOne(cascade = CascadeType.PERSIST, fetch= FetchType.LAZY)
	@JsonIgnore
	@JoinColumn (name="id_size",nullable=false)
	@ForeignKey(name="Fk_size_itemSizeInfo")
    private Size size = null;
	public Size getSize() {
		return this.size;
	}
	public void setSize(Size size) {
		this.size = size;
	}
}
