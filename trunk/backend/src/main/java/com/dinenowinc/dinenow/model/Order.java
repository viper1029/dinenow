package com.dinenowinc.dinenow.model;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.dinenowinc.dinenow.model.helpers.AvailabilityStatus;
import com.dinenowinc.dinenow.model.helpers.BaseEntity;
import com.dinenowinc.dinenow.model.helpers.LatLng;
import com.dinenowinc.dinenow.model.helpers.OrderStatus;
import com.dinenowinc.dinenow.model.helpers.OrderType;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "order")
@Audited
@NamedQueries({ @NamedQuery(name = "Order.GetAll", query = "from Order o") })
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order extends BaseEntity {

  private static final long serialVersionUID = 3445637553066282610L;

  @Column(name = "order_number", nullable = false)
  private long orderNumber;

  @Column(name = "order_status")
  private OrderStatus orderStatus;

  @Column(name = "received_time")
  private Date receivedTime;

  @Column(name = "expected_completion_time")
  private Date expectedCompletionTime;

  @Column(name = "completion_time")
  private Date completionTime;

  @Column(name = "payment_time")
  private Date paymentTime;

  @Column(name = "type")
  private OrderType orderType;

  private LatLng location;

  private double discount;

  private double tip;

  private double tax;

  private double total;

  @Column(name = "payment_method")
  private String paymentMethod;

  @Column(name = "address_1", nullable = false, unique = false)
  private String address_1;

  @Column(name = "address_2", nullable = false, unique = false)
  private String address_2;

  @Column(name = "city", nullable = false, unique = false)
  private String city;

  @Column(name = "province", nullable = false, unique = false)
  private String province;

  @Column(name = "country", nullable = false, unique = false)
  private String country;

  @Column(name = "postal_code", nullable = false, unique = false)
  private String postalCode;

  @OneToMany(cascade = { CascadeType.PERSIST }, fetch = FetchType.EAGER)
  @JoinColumn(name = "id_order")
  @ForeignKey(name = "Fk_order_orderDetails")
  private final Set<OrderDetail> orderDetails = new HashSet<>();

  @OneToMany(cascade = { CascadeType.PERSIST }, fetch = FetchType.EAGER)
  @JoinColumn(name = "id_order")
  @ForeignKey(name = "Fk_order_reviews")
  private final Set<Review> reviews = new HashSet<>();

  @Column(precision = 10, scale = 2)
  private double point;

  public Order() {
  }

  public Order(long order_number, Date received_time, Date expected_completion_time, Date completion_time, Date payment_time,
      OrderType orderType, AvailabilityStatus status, double discount, double tip,
      int tax, int total, double payment_method, String address_1, String address_2,
      String city, String province, String country, String postalCode,
      String coupon, String id_resString) {

    this.orderNumber = order_number;
    this.receivedTime = received_time;
    this.expectedCompletionTime = expected_completion_time;
  }

  public double getPoint() {
    return point;
  }

  public void setPoint(double point) {
    this.point = point;
  }

  public PaymentMethod getPaymentMethod() {
    try {
      return (new ObjectMapper().readValue(this.paymentMethod, new TypeReference<PaymentMethod>() {
      }));
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void setPaymentMethod(PaymentMethod paymentMethod) {
    try {
      this.paymentMethod = new ObjectMapper().writeValueAsString(paymentMethod);
    }
    catch (IOException e) {
      this.paymentMethod = null;
      e.printStackTrace();
    }
  }

  public long getOrderNumber() {
    return orderNumber;
  }


  public void setOrderNumber(long orderNumber) {
    this.orderNumber = orderNumber;
  }

  public OrderStatus getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(OrderStatus orderStatus) {
    this.orderStatus = orderStatus;
  }

  public Date getReceivedTime() {
    return receivedTime;
  }

  public void setReceivedTime(Date receivedAt) {
    this.receivedTime = receivedAt;
  }

  public Date getExpectedCompletionTime() {
    return this.expectedCompletionTime;
  }

  public void setExpectedCompletionTime(Date expectedCompletionAt) {
    this.expectedCompletionTime = expectedCompletionAt;
  }

  public Date getCompletionTime() {
    return completionTime;
  }

  public void setCompletionTime(Date completionAt) {
    this.completionTime = completionAt;
  }

  public Date getPaymentTime() {
    return paymentTime;
  }

  public void setPaymentTime(Date paymentAt) {
    this.paymentTime = paymentAt;
  }

  public double getDiscount() {
    return discount;
  }

  public void setDiscount(double discount) {
    this.discount = discount;
  }

  public double getTip() {
    return tip;
  }

  public void setTip(double tip) {
    this.tip = tip;
  }

  public double getTax() {
    return tax;
  }

  public void setTax(double tax) {
    this.tax = tax;
  }

  public double getTotal() {
    return total;
  }

  public void setTotal(double total) {
    this.total = total;
  }

  public String getAddress_1() {
    return address_1;
  }

  public void setAddress_1(String address_1) {
    this.address_1 = address_1;
  }

  public String getAddress_2() {
    return address_2;
  }

  public void setAddress_2(String address_2) {
    this.address_2 = address_2;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postal_code) {
    this.postalCode = postal_code;
  }

  public void setPaymentMethod(String paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public OrderType getOrderType() {
    return orderType;
  }

  public void setOrderType(OrderType orderType) {
    this.orderType = orderType;
  }

  public Set<OrderDetail> getOrderDetails() {
    return orderDetails;
  }

  public void addOrderDetail(OrderDetail orderDetail) {
    getOrderDetails().add(orderDetail);
  }

  public Set<Review> getReviews() {
    return reviews;
  }

  public LatLng getLocation() {
    return location;
  }

  public void setLocation(LatLng location) {
    this.location = location;
  }


  @Override
  public HashMap<String, Object> toDto() {
    HashMap<String, Object> dto = new LinkedHashMap<>();
    dto.put("id", this.getId());
    dto.put("orderNumber", this.getOrderNumber());
    //	dto.put("paymentAt", this.getPaymentAt());
    dto.put("total", this.getTotal());
    dto.put("tip", this.getTip());
    dto.put("tax", this.getTax());
    dto.put("discount", this.getDiscount());
    dto.put("orderStatus", this.getOrderStatus());
    dto.put("orderType", this.getOrderType());
    dto.put("address1", this.getAddress_1());
    dto.put("address2", this.getAddress_2());
    dto.put("city", this.getCity());
    dto.put("country", this.getCountry());
    dto.put("province", this.getProvince());
    dto.put("postalCode", this.getPostalCode());
    dto.put("orderType", this.getOrderType());
    dto.put("receivedAt", this.getReceivedTime());
    dto.put("expectedCompletionAt", this.getExpectedCompletionTime());
    dto.put("completionAt", this.getCompletionTime());
    dto.put("location", this.getLocation() != null ? this.getLocation() : "");
    //	dto.put("status", this.getAvailstatus());
    return dto;
  }

}
