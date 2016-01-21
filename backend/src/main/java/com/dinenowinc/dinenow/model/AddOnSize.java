package com.dinenowinc.dinenow.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="addon_size")
@Audited
@NamedQueries({@NamedQuery(name="AddOnSize.GetAll", query = "from AddOnSize a")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddOnSize extends InfoBaseSizeAddon<Size, AddOn> {

	@Column(nullable=false)
	private AvailabilityStatus availstatus;
	@Column(nullable=false,columnDefinition="Decimal(10,2)")
	private double price;
	
	public AddOnSize() {
	}
	
	public AddOnSize(Size size,AddOn addon) {
		setSize(size);
		setAddon(addon);
	}
	
	
//	public boolean isDefault() {
//		return isDefault;
//	}
//
//	public void setDefault(boolean isDefault) {
//		this.isDefault = isDefault;
//	}
//	
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
	
	public AvailabilityStatus getAvailstatus() {
		return availstatus;
	}

	public void setAvailstatus(AvailabilityStatus availstatus) {
		this.availstatus = availstatus;
	}

}
