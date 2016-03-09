package com.dinenowinc.dinenow.dao;

import com.dinenowinc.dinenow.model.ItemPrice;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.persistence.EntityManager;

public class ItemInfoDao extends BaseEntityDAOImpl<ItemPrice, String> {

  @Inject
  public ItemInfoDao(Provider<EntityManager> emf) {
    super(emf);
    entityClass = ItemPrice.class;
  }
}
