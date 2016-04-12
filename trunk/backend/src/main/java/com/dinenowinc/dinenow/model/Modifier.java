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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@NamedQueries({ @NamedQuery(name = "Modifier.GetAll", query = "from Modifier m") })
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(
    uniqueConstraints = @UniqueConstraint(columnNames = { "name", "description" }, name = "modifier_uk")
)
public class Modifier extends BaseEntity {

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false, name = "multiple_selection")
  private boolean isMultipleSelection;

  @Column(nullable = false, name = "min_selection")
  private int minSelection;

  @Column(nullable = false, name = "max_selection")
  private int maxSelection;

  @OneToMany(mappedBy = "modifier", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  private final Set<ModifierAddon> modifierAddons = new HashSet<>();

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "id_item", nullable = true, foreignKey = @javax.persistence.ForeignKey(name = "Fk_modifier_items"))
  private final Item item = null;

  public Set<ModifierAddon> getModifierAddons() {
    return modifierAddons;
  }

  public void addAddOn(ModifierAddon info) {
    modifierAddons.add(info);
  }

  public void addAllModifierAddon(ArrayList<ModifierAddon> modifierAddonList) {
    this.modifierAddons.addAll(modifierAddonList);
  }

  public Item getItem() {
    return item;
  }

  public String getName() {
    return name;
  }

  public void setName(String modifierName) {
    this.name = modifierName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String modifierDescription) {
    this.description = modifierDescription;
  }

  public boolean isMultipleSelection() {
    return isMultipleSelection;
  }

  public void setMultipleSelection(boolean isSelectMultiple) {
    this.isMultipleSelection = isSelectMultiple;
  }

  public int getMinSelection() {
    return minSelection;
  }

  public void setMinSelection(int minSelection) {
    this.minSelection = minSelection;
  }

  public int getMaxSelection() {
    return maxSelection;
  }

  public void setMaxSelection(int maxSelection) {
    this.maxSelection = maxSelection;
  }

  @Override
  public HashMap<String, Object> toDto() {
    HashMap<String, Object> dto = new LinkedHashMap<String, Object>();
    dto.put("id", this.getId());
    dto.put("name", this.getName());
    dto.put("description", this.getDescription());
    dto.put("isMultipleSelection", this.isMultipleSelection());
    dto.put("minSelection", this.getMinSelection());
    dto.put("maxSelection", this.getMaxSelection());
    return dto;
  }
}
