package com.dinenowinc.dinenow.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="modifier_info")
@Audited
@NamedQueries({@NamedQuery(name="ModifierInfo.GetAll", query = "from ModifierInfo a")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModifierInfo extends InfoModifier<Modifier>{

	@Column(name="availability_status")
	private AvailabilityStatus availabilityStatus;
	
	public ModifierInfo() {
	}
	
	public ModifierInfo(Modifier modifier) {
		setModifier(modifier);
	}
	
	public AvailabilityStatus getAvailabilityStatus() {
		return availabilityStatus;
	}

	public void setAvailabilityStatus(AvailabilityStatus status) {
		this.availabilityStatus = status;
	}
}
