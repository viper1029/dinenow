package com.dinenowinc.dinenow.dao;

import com.dinenowinc.dinenow.model.Size;
import com.dinenowinc.dinenow.model.Tax;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

public class TaxDao extends BaseEntityDAOImpl<Tax, String> {

  @Inject
  public TaxDao(Provider<EntityManager> emf) {
    super(emf);
    entityClass = Tax.class;
  }

  @SuppressWarnings("unchecked")
  public List<Tax> getTaxesByRestaurantId(String restaurantId) {
    try {
      List<Tax> taxes = (ArrayList<Tax>) getEntityManager().createNativeQuery(
          "SELECT s.* FROM tax s WHERE s.id_restaurant = :value", Tax.class)
          .setParameter("value", restaurantId).getResultList();
      return taxes;
    }
    catch (NoResultException e) {
      return null;
    }
  }
}
