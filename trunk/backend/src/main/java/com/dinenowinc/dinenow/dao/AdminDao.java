package com.dinenowinc.dinenow.dao;

import com.dinenowinc.dinenow.model.Admin;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

public class AdminDao extends BaseEntityDAOImpl<Admin, String> {

  @Inject
  public AdminDao(Provider<EntityManager> emf) {
    super(emf);
    entityClass = Admin.class;
  }

  public Admin getAdminByEmail(final String email) {
    try {
      Admin admin = (Admin) getEntityManager().createNativeQuery(
          "SELECT * FROM admin t WHERE lower(t.email) = :value ", Admin.class)
          .setParameter("value", email).getSingleResult();
      return admin;
    }
    catch (NoResultException e) {
      return null;
    }
  }

  public Admin getAdminByEmailAndPassword(final String email, final String password) {
    try {
      Admin admin = (Admin) getEntityManager().createNativeQuery(
          "SELECT * FROM admin t WHERE lower(t.email) = :value AND t.password = :value2", Admin.class)
          .setParameter("value", email.toLowerCase()).setParameter("value2", password).getSingleResult();
      return admin;
    }
    catch (NoResultException e) {
      return null;
    }
  }

  public Admin getAdminById(final String id) {
    try {
      Admin admin = (Admin) getEntityManager().createNativeQuery(
          "SELECT * FROM admin t WHERE lower(t.id) = :value ", Admin.class)
          .setParameter("value", id).getSingleResult();
      return admin;
    }
    catch (NoResultException e) {
      return null;
    }
  }
}

