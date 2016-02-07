package com.dinenowinc.dinenow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Audited
@NamedQueries({ @NamedQuery(name = "AddOnSize.GetAll", query = "from AddOnSize a") })
@Table(name = "addon_size",
    uniqueConstraints = @UniqueConstraint(columnNames = { "id_size", "id_addon" }, name = "addon_size_uk")
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddOnSize extends BaseEntity {

  @JsonIgnore
  @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  @JoinColumn(name = "id_size", nullable = false, foreignKey = @ForeignKey(name = "Fk_size_addOnSize"))
  private Size size = null;

  @JsonIgnore
  @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  @JoinColumn(name = "id_addon", nullable = false, foreignKey = @ForeignKey(name = "Fk_addOn_addOnSize"))
  private AddOn addon = null;

  @Column(nullable = false)
  private AvailabilityStatus availabilityStatus;

  @Column(nullable = false, columnDefinition = "Decimal(10,2)")
  private double price;

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public AvailabilityStatus getAvailabilityStatus() {
    return availabilityStatus;
  }

  public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) {
    this.availabilityStatus = availabilityStatus;
  }

  public Size getSize() {
    return this.size;
  }

  public void setSize(Size size) {
    this.size = size;
  }

  public AddOn getAddon() {
    return addon;
  }

  public void setAddon(AddOn addon) {
    this.addon = addon;
  }
}
