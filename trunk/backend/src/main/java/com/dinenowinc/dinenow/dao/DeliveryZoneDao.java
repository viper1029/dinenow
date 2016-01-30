package com.dinenowinc.dinenow.dao;

import com.dinenowinc.dinenow.model.DeliveryZone;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

public class DeliveryZoneDao extends BaseEntityDAOImpl<DeliveryZone, String> {

  @Inject
  public DeliveryZoneDao(Provider<EntityManager> emf) {
    super(emf);
    entityClass = DeliveryZone.class;
  }

  @SuppressWarnings("unchecked")
  public List<DeliveryZone> getAllDeliveryZonesByRestaurantId(String restaurantId) {
    try {
      ArrayList<DeliveryZone> deliveryZones = (ArrayList<DeliveryZone>) getEntityManager().createQuery(
          "SELECT r.deliveryZone from Restaurant r inner join  r.deliveryZone where r.id = :value")
          .setParameter("value", restaurantId).getResultList();
      return deliveryZones;
    }
    catch (NoResultException e) {
      return null;
    }
  }
}
