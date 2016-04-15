package com.dinenowinc.dinenow.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.dinenowinc.dinenow.model.helpers.AvailabilityStatus;
import com.dinenowinc.dinenow.model.helpers.BaseEntity;
import com.dinenowinc.dinenow.model.helpers.OrderStatus;
import com.dinenowinc.dinenow.model.helpers.OrderType;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Audited
@JsonIgnoreProperties(ignoreUnknown = true)
@NamedQueries({ @NamedQuery(name = "Cart.GetAll", query = "from Cart c") })

public class Cart extends BaseEntity {

  private static final long serialVersionUID = 5478738900596245767L;

  @Column(name = "order_status")
  private OrderStatus orderStatus;

  @Column(name = "type")
  private OrderType orderType;

  @OneToOne
  @JoinColumn(name = "id_customer", unique = true)
  private Customer customer;

  @OneToMany(mappedBy = "cart", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, orphanRemoval = true)
  private final Set<CartItem> cartItems = new HashSet<>();

  private BigDecimal discount;

  private BigDecimal tax;

  private BigDecimal total;

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public OrderStatus getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(OrderStatus orderStatus) {
    this.orderStatus = orderStatus;
  }

  public BigDecimal getDiscount() {
    return discount;
  }

  public void setDiscount(BigDecimal discount) {
    this.discount = discount;
  }

  public BigDecimal getTax() {
    return tax;
  }

  public void setTax(BigDecimal tax) {
    this.tax = tax;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public OrderType getOrderType() {
    return orderType;
  }

  public void setOrderType(OrderType orderType) {
    this.orderType = orderType;
  }

  public Set<CartItem> getCartItems() {
    return cartItems;
  }

  public void addCartItem(CartItem item) {
    getCartItems().add(item);
  }

  @Override
  public HashMap<String, Object> toDto() {
    HashMap<String, Object> dto = new LinkedHashMap<>();
    dto.put("id", this.getId());
    dto.put("orderType", this.getOrderType());
    dto.put("orderStatus", this.getOrderStatus());
    dto.put("discount", this.getDiscount());
    dto.put("tax", this.getTax());
    dto.put("total", this.getTotal());
    return dto;
  }

}
