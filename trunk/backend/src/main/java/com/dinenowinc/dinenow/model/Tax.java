package com.dinenowinc.dinenow.model;

import com.dinenowinc.dinenow.model.helpers.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Entity
@Audited
@NamedQueries({ @NamedQuery(name = "Tax.GetAll", query = "from Tax t") })
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(
    uniqueConstraints = @UniqueConstraint(columnNames = { "name", "id_restaurant" }, name = "tax_uk")
)
public class Tax extends BaseEntity {

  @Column(nullable = false)
  private String name;

  private String description;

  @Column(nullable = false, columnDefinition = "Decimal(10,2)", name = "value")
  private double value;

  public String getName() {
    return name;
  }

  public void setName(String taxName) {
    this.name = taxName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public double getValue() {
    return value;
  }

  public void setValue(double taxValue) {
    this.value = taxValue;
  }

  @Override
  public HashMap<String, Object> toDto() {
    HashMap<String, Object> dto = new LinkedHashMap<>();
    dto.put("id", this.getId());
    dto.put("name", this.getName());
    dto.put("description", this.getDescription());
    dto.put("value", this.getValue());
    return dto;
  }
}
