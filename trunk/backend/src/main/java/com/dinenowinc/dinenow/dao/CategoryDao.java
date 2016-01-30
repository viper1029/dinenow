package com.dinenowinc.dinenow.dao;

import com.dinenowinc.dinenow.model.Category;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao extends BaseEntityDAOImpl<Category, String> {

  @Inject
  public CategoryDao(Provider<EntityManager> emf) {
    super(emf);
    entityClass = Category.class;
  }

  @SuppressWarnings("unchecked")
  public List<Category> getCategoriesByRestaurantId(String restaurantId) {
    try {
      List<Category> categories = (ArrayList<Category>) getEntityManager().createNativeQuery(
          "SELECT c.* FROM category c WHERE c.id_restaurant = :value", Category.class)
          .setParameter("value", restaurantId).getResultList();
      return categories;
    }
    catch (NoResultException e) {
      return null;
    }
  }
}
