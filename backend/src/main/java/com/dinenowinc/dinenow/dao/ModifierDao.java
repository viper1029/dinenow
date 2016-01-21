package com.dinenowinc.dinenow.dao;

import com.dinenowinc.dinenow.model.Modifier;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class ModifierDao extends BaseEntityDAOImpl<Modifier, String> {

  @Inject
  public ModifierDao(Provider<EntityManager> emf) {
    super(emf);
    entityClass = Modifier.class;
  }

  @SuppressWarnings("unchecked")
  public List<Modifier> getModifiersByRestaurantId(String restaurantId) {
    try {
      List<Modifier> modifiers = (ArrayList<Modifier>) getEntityManager().createQuery(
          "SELECT distinct r.modifiers FROM Restaurant r inner join r.modifiers WHERE r.id = :value")
          .setParameter("value", restaurantId).getResultList();
      return modifiers;
    }
    catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
