package com.dinenowinc.dinenow.dao;

import com.dinenowinc.dinenow.model.OrderDetail;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.persistence.EntityManager;

public class OrderDetailsDao extends BaseEntityDAOImpl<OrderDetail, String> {

  @Inject
  public OrderDetailsDao(Provider<EntityManager> emf) {
    super(emf);
    entityClass = OrderDetail.class;
  }
}
