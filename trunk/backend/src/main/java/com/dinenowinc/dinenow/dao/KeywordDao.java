package com.dinenowinc.dinenow.dao;

import com.dinenowinc.dinenow.model.Keyword;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.persistence.EntityManager;

public class KeywordDao extends BaseEntityDAOImpl<Keyword, String> {

  @Inject
  public KeywordDao(Provider<EntityManager> emf) {
    super(emf);
    entityClass = Keyword.class;
  }
}
