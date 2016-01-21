package com.dinenowinc.dinenow.dao;

import com.dinenowinc.dinenow.model.Role;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.persistence.EntityManager;

public class RoleDao extends BaseEntityDAOImpl<Role, String> {

  @Inject
  public RoleDao(Provider<EntityManager> emf) {
    super(emf);
    entityClass = Role.class;
  }
}
