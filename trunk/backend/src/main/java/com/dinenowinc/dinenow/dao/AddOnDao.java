package com.dinenowinc.dinenow.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;


import com.dinenowinc.dinenow.model.AccessToken;
import com.dinenowinc.dinenow.model.AddOn;
import com.dinenowinc.dinenow.model.Item;
import com.dinenowinc.dinenow.model.Size;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

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
    catch (Exception e) {
      return null;
    }
  }
}
