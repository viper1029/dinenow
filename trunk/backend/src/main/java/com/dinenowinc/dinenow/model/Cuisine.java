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
@NamedQueries({ @NamedQuery(name = "Cuisine.GetAll", query = "from Cuisine t") })
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(
    uniqueConstraints = @UniqueConstraint(columnNames = { "cuisine" }, name = "cuisine_uk")
)
public class Cuisine extends BaseEntity {

  @Column(nullable = false, unique = true)
  private String cuisine;

  public String getCuisine() {
    return cuisine;
  }

  public void setCuisine(final String cuisine) {
    this.cuisine = cuisine;
  }

  @Override
  public HashMap<String, Object> toDto() {
    HashMap<String, Object> dto = new LinkedHashMap<>();
    dto.put("id", this.getId());
    dto.put("cuisine", this.getCuisine());
    return dto;
  }
}
