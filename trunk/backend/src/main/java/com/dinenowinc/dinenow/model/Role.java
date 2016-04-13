package com.dinenowinc.dinenow.model;

import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.persistence.Entity;

import com.dinenowinc.dinenow.model.helpers.BaseEntity;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.envers.Audited;

@Audited
@Entity
@NamedQueries({ @NamedQuery(name = "Role.GetAll", query = "from Role r") })
@JsonIgnoreProperties(ignoreUnknown = true)
public class Role extends BaseEntity {

  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public HashMap<String, Object> toDto() {
    HashMap<String, Object> dto = new LinkedHashMap<String, Object>();
    dto.put("id", this.getId());
    dto.put("name", this.getName());
    return dto;
  }

}
