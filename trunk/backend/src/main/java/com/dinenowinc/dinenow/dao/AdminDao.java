package com.dinenowinc.dinenow.dao;

import com.dinenowinc.dinenow.model.Admin;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.persistence.EntityManager;

public class AdminDao extends BaseEntityDAOImpl<Admin, String> {

  @Inject
  public AdminDao(Provider<EntityManager> emf) {
    super(emf);
    entityClass = Admin.class;
  }

  public Admin getAdminByEmail(String email) {
    try {
      Admin admin = (Admin) getEntityManager().createNativeQuery(
          "SELECT * FROM admin t WHERE lower(t.email) = :value ", Admin.class)
          .setParameter("value", email).getSingleResult();
      return admin;
    }
    catch (Exception e) {
      return null;
    }
  }

  public Admin getAdminByEmailAndPassword(String email, String password) {
    try {
      Admin admin = (Admin) getEntityManager().createNativeQuery(
          "SELECT * FROM admin t WHERE lower(t.email) = :value AND t.password = :value2", Admin.class)
          .setParameter("value", email.toLowerCase()).setParameter("value2", password).getSingleResult();
      return admin;
    }
    catch (Exception e) {
      return null;
    }
  }

  public Admin getAdminById(String id) {
    try {
      Admin admin = (Admin) getEntityManager().createNativeQuery(
          "SELECT * FROM admin t WHERE lower(t.id) = :value ", Admin.class)
          .setParameter("value", id).getSingleResult();
      return admin;
    }
    catch (Exception e) {
      return null;
    }
  }
}

