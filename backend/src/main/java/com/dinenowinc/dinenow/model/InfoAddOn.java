package com.dinenowinc.dinenow.model;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import org.hibernate.annotations.ForeignKey;

import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass
public class InfoAddOn <AddOn extends BaseEntity>extends BaseEntity{
	@OneToOne(cascade = CascadeType.PERSIST, fetch= FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name="id_addon")
	@ForeignKey(name="Fk_addOn_addOnInfo")
    private AddOn addon = null;
	public AddOn getAddOn() {
		return this.addon;
	}
	public void setAddOn(AddOn addon) {
		this.addon = addon;
	}
}
