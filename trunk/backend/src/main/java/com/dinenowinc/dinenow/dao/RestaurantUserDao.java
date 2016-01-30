package com.dinenowinc.dinenow.dao;


import com.dinenowinc.dinenow.model.RestaurantUser;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
public class RestaurantUserDao extends BaseEntityDAOImpl<RestaurantUser, String> {

  @Inject
  public RestaurantUserDao(Provider<EntityManager> emf) {
    super(emf);
    entityClass = RestaurantUser.class;
  }

  public RestaurantUser getRestaurantUserByEmail(final String email) {
    try {
      RestaurantUser restaurantUser = getEntityManager().createQuery(
          "SELECT t FROM RestaurantUser t where lower(t.email) = :email and t.status=0", RestaurantUser.class)
          .setParameter("email", email.toLowerCase()).getSingleResult();
      return restaurantUser;
    }
    catch (NoResultException e) {
      return null;
    }
  }

  public RestaurantUser getRestaurantUserByPhoneNumber(final String phoneNumber) {
    try {
      RestaurantUser restaurantUser = getEntityManager().createQuery(
          "SELECT u FROM RestaurantUser u where u.phone_number= :phone_number", RestaurantUser.class)
          .setParameter("phone_number", phoneNumber).getSingleResult();
      return restaurantUser;
    }
    catch (NoResultException e) {
      return null;
    }
  }


  public RestaurantUser getRestaurantUserByEmailAndPassword(final String email, final String password) {
    try {
      RestaurantUser restaurantUser = getEntityManager().createQuery(
          "SELECT t FROM RestaurantUser t where lower(t.email) = :email and t.password = :pass", RestaurantUser.class)
          .setParameter("email", email.toLowerCase()).setParameter("pass", password).getSingleResult();
      return restaurantUser;
    }
    catch (NoResultException e) {
      return null;
    }
  }

  public RestaurantUser getRestaurantUserByRestaurantId(final String restaurantId) {
    try {
      RestaurantUser restaurantUser = getEntityManager().createQuery(
          "SELECT ru FROM Restaurant r, RestaurantUser ru WHERE r.id = :value and ru.status=0", RestaurantUser.class)
          .setParameter("value", restaurantId).getSingleResult();
      return restaurantUser;
    }
    catch (NoResultException e) {
      return null;
    }
  }

  public RestaurantUser getRestaurantUserById(final String id) {
    try {
      RestaurantUser restaurantUser = getEntityManager().createQuery(
          "SELECT t FROM RestaurantUser t where lower(t.id) = :value and t.status=0", RestaurantUser.class)
          .setParameter("value", id.toLowerCase()).getSingleResult();
      return restaurantUser;
    }
    catch (NoResultException e) {
      return null;
    }
  }

  // TODO: Should not need this, what if there are 2 same resetKey.
  public RestaurantUser findByResetKey(String resetKey) {
    try {
      RestaurantUser c = (RestaurantUser) getEntityManager().createQuery("SELECT c FROM RestaurantUser c where c.reset_key = :value and c.status=0", RestaurantUser.class).setParameter("value", resetKey).getSingleResult();
      return c;
    }
    catch (NoResultException e) {
      return null;
    }
  }

  // TODO
  public List<RestaurantUser> getListByRestaurant(String res_id) {//SELECT r.tax FROM Restaurant r inner join r.tax WHERE r.id = :value
    try {
      List<RestaurantUser> listMenu = (ArrayList<RestaurantUser>) getEntityManager().createQuery("Select distinct ru  from Restaurant r inner join r.users ru where r.id = :value and ru.status=0").setParameter("value", res_id).getResultList();
      //	List<RestaurantUser> listMenu = (ArrayList<RestaurantUser>)getEntityManager().createQuery("SELECT ru from RestaurantUser ru inner join ru.restaurants r where r.id = :value", RestaurantUser.class).setParameter("value", res_id).getResultList();
      System.out.println(listMenu.size());
      return listMenu;
    }
    catch (NoResultException e) {
      return null;
    }
  }
}
