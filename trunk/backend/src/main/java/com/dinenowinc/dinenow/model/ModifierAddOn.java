package com.dinenowinc.dinenow.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.dinenowinc.dinenow.model.helpers.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.LinkedHashMap;

@Entity
@Table(name="modifier_addon")
@Audited
@NamedQueries({@NamedQuery(name="ModifierAddon.GetAll", query = "from ModifierAddon a")})
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect
public class ModifierAddon extends BaseEntity {

	@Column(nullable=false,columnDefinition="Decimal(10,2)")
	private double price;

	@Column(nullable=false, name="is_default")
	private boolean isDefault;

  @JsonIgnore
  @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  @JoinColumn(name = "id_addon", nullable = false, foreignKey = @ForeignKey(name = "Fk_modifieraddon_addon"))
  private Addon addon = null;

  @JsonIgnore
  @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  @JoinColumn(name = "id_modifier", nullable = false, foreignKey = @ForeignKey(name = "Fk_modifieraddon_modifier"))
  private Modifier modifier = null;

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

  public Addon getAddon() {
    return addon;
  }

  public void setAddon(final Addon addon) {
    this.addon = addon;
  }

  public Modifier getModifier() {
    return modifier;
  }

  public void setModifier(final Modifier modifier) {
    this.modifier = modifier;
  }

  @Override
  public HashMap<String, Object> toDto() {
    HashMap<String, Object> dto = new LinkedHashMap<>();
    dto.put("id", this.getId());
    dto.put("price", this.getPrice());
    dto.put("isDefault", this.isDefault);
    dto.put("addon", this.getAddon().toDto());
    return dto;
  }
}
