package com.dinenowinc.dinenow.dao;

import com.dinenowinc.dinenow.model.Size;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

public class SizeDao extends BaseEntityDAOImpl<Size, String> {

  @Inject
  public SizeDao(Provider<EntityManager> emf) {
    super(emf);
    entityClass = Size.class;
  }

  @SuppressWarnings("unchecked")
  public List<Size> getSizesByRestaurantId(String restaurantId) {
    try {
      List<Size> sizes = (ArrayList<Size>) getEntityManager().createNativeQuery(
          "SELECT s.* FROM size s WHERE s.id_restaurant = :value", Size.class)
          .setParameter("value", restaurantId).getResultList();
      return sizes;
    }
    catch (NoResultException e) {
      return null;
    }
  }
}
