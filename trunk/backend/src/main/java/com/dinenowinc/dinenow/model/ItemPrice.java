package com.dinenowinc.dinenow.model;

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
@NamedQueries({ @NamedQuery(name = "ItemPrice.GetAll", query = "from ItemPrice c") })
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "item_price"//,
    //uniqueConstraints = @UniqueConstraint(columnNames = { "id_item" }, name = "item_price_uk")
    )
public class ItemPrice extends BaseEntity {

  @JsonIgnore
  @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  private Item item = null;

  @Column(nullable = false, columnDefinition = "Decimal(10,2)")
  private double price;

  @JsonIgnore
  @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  @JoinColumn(name = "id_category_item", nullable = false, foreignKey = @ForeignKey(name = "fk_categoryItem_itemPrice"))
  private CategoryItem categoryItem = null;

  public Item getItem() {
    return this.item;
  }

  public void setItem(Item item) {
    this.item = item;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  @Override
  public HashMap<String, Object> toDto() {
    HashMap<String, Object> dto = new LinkedHashMap<String, Object>();
    dto.put("id", this.getId());
    dto.put("price", this.getPrice());
    dto.put("item", this.item.toDto());
    return dto;
  }

  public CategoryItem getCategoryItem() {
    return categoryItem;
  }

  public void setCategoryItem(final CategoryItem categoryItem) {
    this.categoryItem = categoryItem;
  }
}
