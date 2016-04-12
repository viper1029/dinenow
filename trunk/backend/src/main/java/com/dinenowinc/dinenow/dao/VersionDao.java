package com.dinenowinc.dinenow.dao;


import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.dinenowinc.dinenow.model.Version;
import com.google.inject.Inject;
import com.google.inject.Provider;

import java.util.List;

public class VersionDao extends BaseEntityDAOImpl<Version, String> {

  @Inject
  public VersionDao(Provider<EntityManager> emf) {
    super(emf);
    entityClass = Version.class;
  }

  public List<Version> getVersion() {
    try {
      List<Version> v = getEntityManager().createQuery("SELECT t FROM Version t", Version.class).getResultList();
      return v;
    }
    catch (NoResultException e) {
      return null;
    }
  }
}
