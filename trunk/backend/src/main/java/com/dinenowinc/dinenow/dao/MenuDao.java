package com.dinenowinc.dinenow.dao;

import com.dinenowinc.dinenow.model.Menu;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

public class MenuDao extends BaseEntityDAOImpl<Menu, String> {

  @Inject
  public MenuDao(Provider<EntityManager> emf) {
    super(emf);
    entityClass = Menu.class;
  }

  @SuppressWarnings("unchecked")
  public List<Menu> getMenusByRestaurantId(String restaurantId) {
    try {
      List<Menu> menus = (ArrayList<Menu>) getEntityManager().createNativeQuery(
          "SELECT t.* FROM menu t WHERE t.id_restaurant = :value", Menu.class)
          .setParameter("value", restaurantId).getResultList();
      return menus;
    }
    catch (NoResultException e) {
      return null;
    }
  }
}
