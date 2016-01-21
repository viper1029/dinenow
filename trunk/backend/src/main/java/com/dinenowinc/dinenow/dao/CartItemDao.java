package com.dinenowinc.dinenow.dao;

import com.dinenowinc.dinenow.model.CartItem;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.persistence.EntityManager;

public class CartItemDao extends BaseEntityDAOImpl<CartItem, String> {

  @Inject
  public CartItemDao(Provider<EntityManager> emf) {
    super(emf);
    entityClass = CartItem.class;
  }
}
