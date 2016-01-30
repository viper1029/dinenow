package com.dinenowinc.dinenow.dao;

import com.dinenowinc.dinenow.model.Review;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

public class ReviewDao extends BaseEntityDAOImpl<Review, String> {

  @Inject
  public ReviewDao(Provider<EntityManager> emf) {
    super(emf);
    entityClass = Review.class;
  }

  //TODO: check
  @SuppressWarnings("unchecked")
  public List<Review> getReviewsByRestaurantId(String restaurantId) {
    try {
      List<Review> reviews = (ArrayList<Review>) getEntityManager().createQuery(
          "SELECT distinct r.reviews FROM Restaurant r inner join r.reviews WHERE r.id = :value")
          .setParameter("value", restaurantId).getResultList();
      return reviews;
    }
    catch (NoResultException e) {
      return null;
    }
  }
}
