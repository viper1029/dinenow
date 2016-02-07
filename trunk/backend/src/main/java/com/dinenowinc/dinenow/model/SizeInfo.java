package com.dinenowinc.dinenow.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "size_info")
@Audited
@NamedQueries({ @NamedQuery(name = "SizeInfo.GetAll", query = "from SizeInfo a") })
@JsonIgnoreProperties(ignoreUnknown = true)
public class SizeInfo extends BaseEntity {

  @Column(nullable = false, columnDefinition = "Decimal(10,2)")
  private double price;

  @JsonIgnore
  @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  @JoinColumn(name = "id_size", foreignKey = @ForeignKey(name = "id_size_size_size_info_Fk"))
  private Size size = null;

  public SizeInfo() {
    setCreatedBy("Auto");
    setCreatedDate(new Date());
  }

  public SizeInfo(Size addon) {
    setSize(addon);
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public Size getSize() {
    return this.size;
  }

  public void setSize(Size size) {
    this.size = size;
  }
}
