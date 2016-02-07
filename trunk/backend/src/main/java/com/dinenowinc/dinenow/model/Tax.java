package com.dinenowinc.dinenow.model;

import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Audited
@JsonIgnoreProperties(ignoreUnknown = true)
@NamedQueries({ @NamedQuery(name = "Tax.GetAll", query = "from Tax t") })
@Table(
    uniqueConstraints = @UniqueConstraint(columnNames = { "name", "id_restaurant" }, name = "tax_uk")
)
public class Tax extends BaseEntity {

  @Column(nullable = false)
  private String name;

  private String description;

  @Column(nullable = false, columnDefinition = "Decimal(10,2)", name = "value")
  private double value;

  public String getTaxName() {
    return name;
  }

  public void setTaxName(String taxName) {
    this.name = taxName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public double getTaxValue() {
    return value;
  }

  public void setTaxValue(double taxValue) {
    this.value = taxValue;
  }

  @Override
  public HashMap<String, Object> toDto() {
    HashMap<String, Object> dto = new LinkedHashMap<String, Object>();
    dto.put("id", this.getId());
    dto.put("name", this.getTaxName());
    dto.put("description", this.getDescription());
    dto.put("value", this.getTaxValue());
    return dto;
  }
}
