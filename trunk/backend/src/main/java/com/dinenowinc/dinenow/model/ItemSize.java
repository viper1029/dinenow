package com.dinenowinc.dinenow.model;

import com.dinenowinc.dinenow.model.helpers.AvailabilityEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Entity
@Audited
@NamedQueries({ @NamedQuery(name = "ItemSize.GetAll", query = "from ItemSize a") })
@Table(name = "item_size",
    uniqueConstraints = @UniqueConstraint(columnNames = { "id_size", "id_item" }, name = "item_size_uk")
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemSize extends AvailabilityEntity {

  @JsonIgnore
  @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  @JoinColumn(name = "id_size", foreignKey = @ForeignKey(name = "id_size_size_size_info_Fk"))
  private Size size = null;

  @JsonIgnore
  @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  @JoinColumn(name = "id_item", nullable = false, foreignKey = @ForeignKey(name = "Fk_item_itemSize"))
  private Item item = null;

  @Column(nullable = false, columnDefinition = "Decimal(10,2)")
  private double price;

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public Size getSize() {
    return this.size;
  }

  public void setSize(Size size) {
    this.size = size;
  }

  public Item getItem() {
    return item;
  }

  public void setItem(final Item item) {
    this.item = item;
  }

  @Override
  public HashMap<String, Object> toDto() {
    HashMap<String, Object> dto = new LinkedHashMap<String, Object>();
    dto.put("id", this.getId());
    dto.put("price", this.getPrice());
    dto.put("availabilityStatus", this.getAvailabilityStatus());
    dto.put("size", this.getSize().toDto());
    return dto;
  }
}
