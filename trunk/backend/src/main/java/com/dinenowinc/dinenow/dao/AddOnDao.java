package com.dinenowinc.dinenow.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;


import com.dinenowinc.dinenow.model.AddOn;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class AddOnDao extends BaseEntityDAOImpl<AddOn, String> {

  @Inject
  public AddOnDao(Provider<EntityManager> emf) {
    super(emf);
    entityClass = AddOn.class;
  }

  public List<AddOn> getAddonsByRestaurantId(String restaurantId) {
    try {
      List<AddOn> addOns = (ArrayList<AddOn>) getEntityManager().createNativeQuery(
          "SELECT t.* FROM addon t WHERE t.id_restaurant = :value", AddOn.class)
          .setParameter("value", restaurantId).getResultList();
      return addOns;
    }
    catch (NoResultException e) {
      return null;
    }
  }
}
