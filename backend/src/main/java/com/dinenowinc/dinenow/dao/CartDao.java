package com.dinenowinc.dinenow.dao;

import com.dinenowinc.dinenow.model.Cart;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.persistence.EntityManager;

public class CartDao extends BaseEntityDAOImpl<Cart, String> {

  @Inject
  public CartDao(Provider<EntityManager> emf) {
    super(emf);
    entityClass = Cart.class;
  }
}
