package com.dinenowinc.dinenow.model;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import org.hibernate.annotations.ForeignKey;

import com.fasterxml.jackson.annotation.JsonIgnore;
@MappedSuperclass
public class InfoBaseSizeAddon <Size extends BaseEntity, Addon extends BaseEntity> extends BaseEntity{
	
	@OneToOne(cascade = CascadeType.PERSIST, fetch= FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name="id_size",nullable=false)
	@ForeignKey(name="Fk_size_addOnSize")
    private Size size = null;
	public Size getSize() {
		return this.size;
	}
	public void setSize(Size size) {
		this.size = size;
	}
	
	
	@OneToOne(cascade = CascadeType.PERSIST, fetch= FetchType.LAZY)
	@JsonIgnore
	@JoinColumn (name="id_addon",nullable=false)
	@ForeignKey(name="Fk_addOn_addOnSize")
    private Addon addon = null;
	public Addon getAddon() {
		return this.addon;
	}
	public void setAddon(Addon addon) {
		this.addon = addon;
	}
}
