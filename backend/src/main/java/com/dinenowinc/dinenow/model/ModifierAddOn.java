package com.dinenowinc.dinenow.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="modifier_addon")
@Audited
@NamedQueries({@NamedQuery(name="ModifierAddOn.GetAll", query = "from ModifierAddOn a")})
@JsonIgnoreProperties(ignoreUnknown = true)

//InfoBase<AddOn>
@JsonAutoDetect
public class ModifierAddOn extends InfoAddOn<AddOn>{

	@Column(nullable=false)
	private AvailabilityStatus availStatus;
	@Column(nullable=false,columnDefinition="Decimal(10,2)")
	private double price;
	@Column(nullable=false)
	private boolean is_default;
	
	
	
	public ModifierAddOn() {
		setCreatedBy("Auto");
		setCreatedDate(new Date());
	}
	
	public ModifierAddOn(AddOn addon) {
		setAddOn(addon);
	}
	
	
	public boolean isDefault() {
		return is_default;
	}

	public void setDefault(boolean isDefault) {
		this.is_default = isDefault;
	}
	
//	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch= FetchType.LAZY)
//    @JoinColumn(name="id_size")	
//	private final Set<Size> size = new HashSet<Size>();
//	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch= FetchType.LAZY)
//    @JoinColumn(name="id_addon")	
//	private final Set<AddOn> addOns = new HashSet<AddOn>();
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public AvailabilityStatus getAvailStatus() {
		return availStatus;
	}

	public void setAvailStatus(AvailabilityStatus availStatus) {
		this.availStatus = availStatus;
	}

	public boolean isIs_default() {
		return is_default;
	}

	public void setIs_default(boolean is_default) {
		this.is_default = is_default;
	}
}
