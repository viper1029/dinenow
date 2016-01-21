package com.dinenowinc.dinenow.dao;

import com.dinenowinc.dinenow.model.Coupon;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.persistence.EntityManager;

public class CouponDao extends BaseEntityDAOImpl<Coupon, String> {

  @Inject
  public CouponDao(Provider<EntityManager> emf) {
    super(emf);
    entityClass = Coupon.class;
  }
}
