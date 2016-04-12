package com.dinenowinc.dinenow.model;

import com.dinenowinc.dinenow.model.helpers.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Audited
@NamedQueries({ @NamedQuery(name = "ItemModifier.GetAll", query = "from ItemModifier a") })
@Table(name = "item_modifier"
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemModifier extends BaseEntity {
/*
  @JsonIgnore
  @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  @JoinColumn(name = "id_modifier", nullable = false, foreignKey = @javax.persistence.ForeignKey(name = "Fk_addon_itemModifier"))
  private Modifier modifier = null;

  @JsonIgnore
  @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  @JoinColumn(name = "id_item", nullable = false, foreignKey = @javax.persistence.ForeignKey(name = "Fk_item_itemSize"))
  private Item item = null;
  */
}
