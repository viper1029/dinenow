package com.dinenowinc.dinenow.model;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import org.hibernate.annotations.ForeignKey;

import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass
public class InfoModifier <Modifier extends BaseEntity> extends BaseEntity {
	
	
	
	@OneToOne(cascade = CascadeType.PERSIST, fetch= FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name="id_modifier",nullable=false)
	@ForeignKey(name="Fk_modifier_modifierInfo")
    private Modifier modifier = null;
	public Modifier getModifier() {
		return this.modifier;
	}
	public void setModifier(Modifier modifier) {
		this.modifier = modifier;
	}
}
