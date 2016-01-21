package com.dinenowinc.dinenow.dao;

import com.dinenowinc.dinenow.model.Tax;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.persistence.EntityManager;
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
      List<Tax> taxes = getEntityManager().createQuery(
          "SELECT t FROM Tax t WHERE t.id = :value", Tax.class)
          .setParameter("value", restaurantId).getResultList();
      return taxes;
    }
    catch (Exception e) {
      return null;
    }
  }
}
