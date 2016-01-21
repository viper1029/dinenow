package com.dinenowinc.dinenow.model;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass
public class InfoBase<T extends BaseEntity> extends BaseEntity {
	
	
	
	@OneToOne(cascade = CascadeType.PERSIST, fetch= FetchType.LAZY)
	@JsonIgnore
    private T entity = null;
	public T getEntity() {
		return this.entity;
	}
	public void setEntity(T entity) {
		this.entity = entity;
	}
}
