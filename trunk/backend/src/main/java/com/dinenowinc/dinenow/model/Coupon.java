package com.dinenowinc.dinenow.model;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.dinenowinc.dinenow.model.helpers.BaseEntity;
import com.dinenowinc.dinenow.model.helpers.CouponType;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@NamedQueries({ @NamedQuery(name = "Coupon.GetAll", query = "from Coupon c") })
@JsonIgnoreProperties(ignoreUnknown = true)
public class Coupon extends BaseEntity {

  @Column(name = "minimum_value")
  private int minimumValue;

  @Column(name = "maximum_value")
  private int maximumValue;

  @Column(name = "discount_value")
  private int discountValue;

  @Column(name = "start_date")
  private Date startDate;

  @Column(name = "end_date")
  private Date endDate;

  @Column(name = "coupon_type")
  private CouponType couponType;

  private int points;

  private CouponType active;

  public int getMinimumValue() {
    return minimumValue;
  }

  public void setMinimumValue(int minimum_value) {
    this.minimumValue = minimum_value;
  }

  public int getMaximumValue() {
    return maximumValue;
  }

  public void setMaximumValue(int maximum_value) {
    this.maximumValue = maximum_value;
  }

  public int getDiscountValue() {
    return discountValue;
  }

  public void setDiscountValue(int discount_value) {
    this.discountValue = discount_value;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date start_date) {
    this.startDate = start_date;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date end_date) {
    this.endDate = end_date;
  }

  public CouponType getCouponType() {
    return couponType;
  }

  public void setCouponType(CouponType coupon_type) {
    this.couponType = coupon_type;
  }

  @Override
  public HashMap<String, Object> toDto() {
    HashMap<String, Object> dto = new LinkedHashMap<>();
    dto.put("id", this.getId());
    dto.put("couponType", this.getCouponType());
    dto.put("discountValue", this.getDiscountValue());
    dto.put("startDate", this.getStartDate());
    dto.put("endDate", this.getEndDate());
    dto.put("maximumValue", this.getMaximumValue());
    dto.put("manimumValue", this.getMinimumValue());
    return dto;
  }

  public long getPoints() {
    return points;
  }

  public void setPoints(final int points) {
    this.points = points;
  }
}
