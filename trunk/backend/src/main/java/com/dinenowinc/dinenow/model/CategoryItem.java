package com.dinenowinc.dinenow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

@Entity
@Audited
@NamedQueries({ @NamedQuery(name = "CategoryItem.GetAll", query = "from CategoryItem c") })
@Table(name = "category_item")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryItem extends BaseEntity {

  @OneToMany(mappedBy = "categoryItem", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  private final Set<ItemPrice> itemPrices = new HashSet<>();

  @JsonIgnore
  @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  @JoinColumn(name = "id_category", foreignKey = @ForeignKey(name = "Fk_category_categoryitem"))
  private Category category = null;

  @JsonIgnore
  @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  @JoinColumn(name = "id_menu", nullable = false, foreignKey = @ForeignKey(name = "Fk_menu_categoryitem"))
  private Menu menu = null;

  public Set<ItemPrice> getItemPrices() {
    return itemPrices;
  }

  public Category getCategory() {
    return this.category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public void addItemPrice(ItemPrice item) {
    itemPrices.add(item);
  }

  public void addAllItemPrice(ArrayList<ItemPrice> items) {
    items.addAll(items);
  }

  public Menu getMenu() {
    return menu;
  }

  public void setMenu(final Menu menu) {
    this.menu = menu;
  }

}
