package com.dinenowinc.dinenow.model;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass
public class InfoBaseTwo<T extends BaseEntity, K extends BaseEntity> extends BaseEntity {
	
	@OneToOne(cascade = CascadeType.PERSIST, fetch= FetchType.LAZY)
	@JsonIgnore
	@JoinColumn
    private T entity1 = null;
	public T getEntity1() {
		return this.entity1;
	}
	public void setEntity1(T entity1) {
		this.entity1 = entity1;
	}
	
	
	@OneToOne(cascade = CascadeType.PERSIST, fetch= FetchType.LAZY)
	@JsonIgnore
	@JoinColumn
    private K entity2 = null;
	public K getEntity2() {
		return this.entity2;
	}
	public void setEntity2(K entity2) {
		this.entity2 = entity2;
	}
	
}
