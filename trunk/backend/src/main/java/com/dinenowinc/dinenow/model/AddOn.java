package com.dinenowinc.dinenow.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Audited
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(
    uniqueConstraints = @UniqueConstraint(columnNames = { "name", "description" }, name = "addon_uk")
)
public class AddOn extends AvailabilityEntity {

  private static final long serialVersionUID = 5478762900596245767L;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String description;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "id_addon", foreignKey = @ForeignKey(name = "id_addon_addOn_size_info_Fk_2"))
  private final Set<AddOnSize> sizes = new HashSet<AddOnSize>();

  public Set<AddOnSize> getSizes() {
    return sizes;
  }

  public void addAllSize(ArrayList<AddOnSize> sizeList) {
    sizes.clear();
    sizes.addAll(sizeList);
  }

  public String getAddOnName() {
    return name;
  }

  public void setAddOnName(String addOnName) {
    this.name = addOnName;
  }

  public String getAddOnDescription() {
    return description;
  }

  public void setAddOnDescription(String addOnDescription) {
    this.description = addOnDescription;
  }

  @Override
  public HashMap<String, Object> toDto() {
    HashMap<String, Object> dto = new LinkedHashMap<String, Object>();
    dto.put("id", this.getId());
    dto.put("name", this.getAddOnName());
    dto.put("description", this.getAddOnDescription());
    dto.put("availabilityStatus", this.getAvailabilityStatus());
    return dto;
  }
}