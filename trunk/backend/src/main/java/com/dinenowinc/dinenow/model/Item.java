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
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

@Entity
@Audited
@NamedQueries({ @NamedQuery(name = "Item.GetAll", query = "from Item m") })
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item extends BaseEntity {

  private static final long serialVersionUID = -8397322080318233174L;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private String notes;

  @Column(nullable = false)
  private String image;

  @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  private final Set<ItemSize> itemSizes = new HashSet<>();

  @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  private final Set<Modifier> modifiers = new HashSet<>();

  public Set<ItemSize> getItemSizes() {
    return itemSizes;
  }

  public void addAllItemSize(ArrayList<ItemSize> sizes) {
    itemSizes.addAll(sizes);
  }

  public void addAllModifier(ArrayList<Modifier> modifiersList) {
    modifiers.addAll(modifiersList);
  }

  public Set<Modifier> getModifiers() {
    return modifiers;
  }


//  public Set<OrderDetail> getOrderDetails() {
//    return orderDetails;
//  }
//
//  public void addOrderDetails(OrderDetail order) {
//    getOrderDetails().add(order);
//  }

//  public Set<CartItem> getCartItems() {
//    return cartItems;
//  }
//
//  public void addCartItems(CartItem cartItem) {
//    getCartItems().add(cartItem);
//  }

  public String getName() {
    return name;
  }

  public void setName(String itemName) {
    this.name = itemName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String linkImage) {
    this.image = linkImage;
  }

  @Override
  public HashMap<String, Object> toDto() {
    HashMap<String, Object> dto = new LinkedHashMap<String, Object>();
    dto.put("id", this.getId());
    dto.put("name", this.getName());
    dto.put("description", this.getDescription());
    dto.put("notes", this.getNotes());
    dto.put("linkImage", this.getImage());
    return dto;
  }
}
