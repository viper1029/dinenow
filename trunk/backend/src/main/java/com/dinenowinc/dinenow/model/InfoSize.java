package com.dinenowinc.dinenow.model;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import org.hibernate.annotations.ForeignKey;

import com.fasterxml.jackson.annotation.JsonIgnore;
@MappedSuperclass
public class InfoSize <Size extends BaseEntity> extends BaseEntity {
	
	
	
	@OneToOne(cascade = CascadeType.PERSIST, fetch= FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name="id_size")
	@ForeignKey(name="id_size_size_size_info_Fk")
    private Size size = null;
	public Size getSize() {
		return this.size;
	}
	public void setSize(Size size) {
		this.size = size;
	}
}

