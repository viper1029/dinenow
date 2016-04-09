package com.dinenowinc.dinenow.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;


import com.dinenowinc.dinenow.model.Addon;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class AddonDao extends BaseEntityDAOImpl<Addon, String> {

  @Inject
  public AddonDao(Provider<EntityManager> emf) {
    super(emf);
    entityClass = Addon.class;
  }

  public List<Addon> getAddonsByRestaurantId(String restaurantId) {
    try {
      List<Addon> addons = (ArrayList<Addon>) getEntityManager().createNativeQuery(
          "SELECT t.* FROM addon t WHERE t.id_restaurant = :value", Addon.class)
          .setParameter("value", restaurantId).getResultList();
      return addons;
    }
    catch (NoResultException e) {
      return null;
    }
  }
}
