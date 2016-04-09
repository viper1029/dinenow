package com.dinenowinc.dinenow.model;

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

  @OneToMany(mappedBy = "addon", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  //@JoinColumn(foreignKey = @ForeignKey(name = "id_addon_addOn_size_info_Fk_2"))
  private final Set<AddonSize> addonSizes = new HashSet<AddonSize>();

  public Set<AddonSize> getAddonSize() {
    return addonSizes;
  }

  public void addAllSize(ArrayList<AddonSize> sizeList) {
    addonSizes.addAll(sizeList);
  }

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

  @Override
  public HashMap<String, Object> toDto() {
    HashMap<String, Object> dto = new LinkedHashMap<String, Object>();
    dto.put("id", this.getId());
    dto.put("name", this.getAddonName());
    dto.put("description", this.getAddonDescription());
    return dto;
  }
}