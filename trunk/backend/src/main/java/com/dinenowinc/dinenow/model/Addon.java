package com.dinenowinc.dinenow.model;

import com.dinenowinc.dinenow.model.helpers.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

@Entity
@Audited
@JsonIgnoreProperties(ignoreUnknown = true)
@NamedQueries({ @NamedQuery(name = "Addon.GetAll", query = "from Addon t") })
@Table(
    uniqueConstraints = @UniqueConstraint(columnNames = { "name", "description" }, name = "addon_uk")
)
public class Addon extends BaseEntity {

  private static final long serialVersionUID = 5478762900596245767L;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false, columnDefinition = "Decimal(10,2)")
  private double price;

  public String getAddonName() {
    return name;
  }

  public void setAddonName(String addOnName) {
    this.name = addOnName;
  }

  public String getAddonDescription() {
    return description;
  }

  public void setAddonDescription(String addOnDescription) {
    this.description = addOnDescription;
  }

  public double getAddonPrice() {
    return price;
  }

  public void setAddonPrice(double price)  {
    this.price = price;
  }

  @Override
  public HashMap<String, Object> toDto() {
    HashMap<String, Object> dto = new LinkedHashMap<>();
    dto.put("id", this.getId());
    dto.put("name", this.getAddonName());
    dto.put("description", this.getAddonDescription());
    dto.put("price", this.getAddonPrice());
    return dto;
  }
}