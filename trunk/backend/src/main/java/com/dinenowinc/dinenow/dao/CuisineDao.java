package com.dinenowinc.dinenow.dao;

import com.dinenowinc.dinenow.model.Cuisine;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.persistence.EntityManager;

public class CuisineDao extends BaseEntityDAOImpl<Cuisine, String> {

  @Inject
  public CuisineDao(Provider<EntityManager> emf) {
    super(emf);
    entityClass = Cuisine.class;
  }
}
