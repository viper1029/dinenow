package com.dinenowinc.dinenow.dao;

import com.dinenowinc.dinenow.model.Customer;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

public class CustomerDao extends BaseEntityDAOImpl<Customer, String> {

  @Inject
  public CustomerDao(Provider<EntityManager> emf) {
    super(emf);
    entityClass = Customer.class;
  }

  public Customer getCustomerByPhoneNumber(final String phone) {
    Customer customer = null;
    try {
      customer = getEntityManager().createQuery(
          "SELECT c FROM Customer c where c.phoneNumber = :value", Customer.class)
          .setParameter("value", phone).getSingleResult();
    }
    catch (NoResultException e) {
    }
    return customer;
  }

  public Customer getCustomerByEmail(final String email) {
    Customer customer = null;
    try {
      customer = getEntityManager().createQuery(
          "SELECT c FROM Customer c where lower(c.email) = :value ", Customer.class)
          .setParameter("value", email).getSingleResult();
    }
    catch (NoResultException e) {
    }
    return customer;
  }

  public Customer getCustomerByID(final String id) {
    try {
      Customer customer = getEntityManager().createQuery(
          "SELECT t FROM Customer t where lower(t.id) = :value ", Customer.class)
          .setParameter("value", id.toLowerCase()).getSingleResult();
      return customer;
    }
    catch (NoResultException e) {
      return null;
    }
  }

  //TODO: Check why we need to do this, should not need to do this
  public Customer findByResetKey(final String resetKey) {
    try {
      Customer customer = (Customer) getEntityManager().createNativeQuery("SELECT * FROM Customer c WHERE c.reset_key = :value", Customer.class).setParameter("value", resetKey).getSingleResult();
      return customer;
    }
    catch (NoResultException e) {
      return null;
    }
  }

  public Customer findBySocial(final String social) {
    try {
      Customer customer = getEntityManager().createQuery(
          "SELECT c FROM Customer c JOIN c.socialAccounts sa where sa.userName = :value", Customer.class)
          .setParameter("value", social).getSingleResult();
      return customer;
    }
    catch (NoResultException e) {
      return null;
    }
  }

  public Customer getCustomerByEmailAndPassword(final String email, final String password) {
    try {
      Customer customer = (Customer) getEntityManager().createNativeQuery(
          "SELECT * FROM Customer t WHERE lower(t.email) = :value AND t.password = :value2", Customer.class)
          .setParameter("value", email).setParameter("value2", password).getSingleResult();
      return customer;
    }
    catch (NoResultException e) {
      return null;
    }
  }

  //TODO: Check why we need to get customer by password
  public Customer checkByPassword(final String password) {
    try {
      Customer customer = (Customer) getEntityManager().createNativeQuery("SELECT * FROM Customer t WHERE t.password = :value2", Customer.class).setParameter("value2", password).getSingleResult();
      return customer;
    }
    catch (Exception e) {
      return null;
    }
  }

  //
  public Customer findByOrder(final String orderId) { //SELECT ru FROM Restaurant r, RestaurantUser ru JOIN r.users ru WHERE r.id
    try {
      //old query - SELECT c.* FROM customer c, customerorder co WHERE c.id = co.id_customer AND co.id
      Customer customer = (Customer) getEntityManager().createQuery("SELECT c FROM Customer c JOIN c.orders co WHERE co.id = :value", Customer.class).setParameter("value", orderId).getSingleResult();
      return customer;
    }
    catch (NoResultException e) {
      return null;
    }
  }


}
