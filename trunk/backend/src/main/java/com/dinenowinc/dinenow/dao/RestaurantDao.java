package com.dinenowinc.dinenow.dao;

import com.dinenowinc.dinenow.model.Restaurant;
import com.dinenowinc.dinenow.model.Review;
import com.dinenowinc.dinenow.model.helpers.SearchOrderBy;
import com.dinenowinc.dinenow.model.helpers.SearchType;
import com.dinenowinc.dinenow.model.helpers.SortedBY;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.util.GeometricShapeFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RestaurantDao extends BaseEntityDAOImpl<Restaurant, String> {

  @Inject
  public RestaurantDao(Provider<EntityManager> emf) {
    super(emf);
    entityClass = Restaurant.class;
  }

  public Restaurant findByMenuId(String menu_id) {
    try {
      Restaurant restaurant = (Restaurant) getEntityManager().createQuery(
          "SELECT r FROM Restaurant r JOIN r.menus m WHERE m.id = :id").setParameter("id", menu_id).getSingleResult();
      return restaurant;
    }
    catch (NoResultException e) {
      return null;
    }
  }

  public Restaurant findByRestaurantUser(String id) {
    try {
      Restaurant restaurant = (Restaurant) getEntityManager().createQuery(
          "SELECT r FROM Restaurant r JOIN r.users u WHERE u.id = :id").setParameter("id", id).getSingleResult();
      return restaurant;
    }
    catch (NoResultException e) {
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  public List<Restaurant> findDistance(Point location, double distance, SearchOrderBy orderBy) {
    System.out.println("LLLLLLLLL" + distance);

    try {
      List<Restaurant> l = (ArrayList<Restaurant>) getEntityManager()
          .createNativeQuery(
              "SELECT r.*,(((acos(sin(( :value1 *pi()/180)) * sin((X(location)*pi()/180))+cos(( :value2 *pi()/180)) * cos((X(location)*pi()/180)) * cos((( :value3 - Y(location))*pi()/180))))*180/pi())*60*1.1515*1.609344) as distance FROM restaurant r HAVING distance <= :value4 ORDER BY distance",
              Restaurant.class)
          .setParameter("value1", location.getX())
          .setParameter("value2", location.getX())
          .setParameter("value3", location.getY())
          .setParameter("value4", distance).getResultList();
      return l;
    }
    catch (NoResultException e) {
      return null;
    }
  }

  public List<Restaurant> findDistanceNew(SearchType type, final Point location, double distance, String cusine, String sorted) {
    String sql = "Select c from Restaurant c where timezone=:zoneId and status=0 and cuisine LIKE :value";
    try {
      if (type == SearchType.BOTH) {
        sql = sql + " and accept_delivery=" + true + " and accept_takeout=" + true;
      }
      else if (type == SearchType.DELIVERY) {
        sql = sql + " and accept_delivery=" + true;
      }
      else if (type == SearchType.PICKUP) {
        sql = sql + " and accept_takeout=" + true;
      }

      if (cusine == null || cusine.length() == 0) {
        cusine = "[";
      }

      if (sorted != null && sorted.equalsIgnoreCase(SortedBY.DESC.name())) {
        sql = sql + " ORDER BY name DESC";
      }
      else if (sorted != null && sorted.equalsIgnoreCase(SortedBY.RAT.name())) {
        sql = sql + " ORDER BY rating DESC";
      }
      else {
        sql = sql + " ORDER BY name ASC";
      }

      System.out.println(location.getX() + "::::::::" + location.getY());
      System.out.println(distance);
      Geometry filter = createCircle(location.getX(), location.getY(), distance);
      List<Restaurant> l = (ArrayList<Restaurant>) getEntityManager().createQuery(sql, Restaurant.class)
          .setParameter("filter", filter).setParameter("value", "%" + cusine + "%").getResultList();
      System.out.println(l.size() + ">>>>>>>>>>>>>>>>>>>>>>>>>>>");


      if (sorted != null && sorted.equalsIgnoreCase(SortedBY.DIST.name())) {
        Comparator<Restaurant> comparator = new Comparator<Restaurant>() {
          public int compare(Restaurant c1, Restaurant c2) {
            if (getDistance(location, c1) == getDistance(location, c2))
              return 0;
            else if (getDistance(location, c1) > getDistance(location, c2))
              return 1;
            else
              return -1;  // use your logic
          }
        };
        Collections.sort(l, comparator);
      }

      for (Restaurant resturant : l) {
        System.out.println(":::::::::::::DISSISISI" + getDistance(location, resturant));
        resturant.setDistance(getDistance(location, resturant) / 1000);
      }

      return l;
    }
    catch (Exception e) {
      return null;
    }
  }


  public static double getDistance(Point location, Restaurant rs) {
    return (location.distance(rs.getLocation()) * (Math.PI / 180) * 6378137);
  }

  public static double getRating(Restaurant rs) {
    double rate = 0;
    for (Review r : rs.getReviews()) {
      // System.out.println(location.distance(rs.getLocation())* (Math.PI / 180) * 6378137);;// in meteres
      rate = rate + r.getRating();
    }
    System.out.println(rate + "><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
    return rate / rs.getReviews().size();
  }

  private static Geometry createCircle(double x, double y, final double RADIUS) {
    GeometricShapeFactory shapeFactory = new GeometricShapeFactory();
    shapeFactory.setNumPoints(32);
    shapeFactory.setCentre(new Coordinate(x, y)); //there are your coordinates
    shapeFactory.setSize((RADIUS * 2));  //this is how you set the radius
    return shapeFactory.createCircle();
  }


  @SuppressWarnings("unchecked")
  public List<Restaurant> findByLocation(SearchType type, Point location, double distance) {
    try {
      List<Restaurant> restaurants = (List<Restaurant>) getEntityManager().createNativeQuery("CALL findDelivery( :lat, :lng, :distance)", Restaurant.class).setParameter("lat", location.getX()).setParameter("lng", location.getY()).setParameter("distance", distance).getResultList();
      List<Restaurant> result = new ArrayList<>();
      switch (type) {
        case BOTH:
          result = new ArrayList<>();
          result = restaurants;
          break;
        case DELIVERY:
          result = new ArrayList<>();
          for (Restaurant restaurant : restaurants) {
            if (restaurant.isAcceptDelivery()) {
              result.add(restaurant);
            }
          }
          break;
        case PICKUP:
          result = new ArrayList<>();
          for (Restaurant restaurant : restaurants) {
            if (restaurant.isAcceptTakeout()) {
              result.add(restaurant);
            }
          }
          break;

        default:
          result = new ArrayList<>();
          result = restaurants;
          break;
      }
      return result;
    }
    catch (Exception e) {
      return null;
    }
  }

  public List<Restaurant> findDistanceNew(SearchType type, String cusine, String sorted, String zone) {
    String sql = "Select c from Restaurant c where timezone=:zoneId and status=0 and cuisine LIKE :value";
    try {
      if (type == SearchType.BOTH) {
        sql = sql + " and accept_delivery=" + true + " and accept_takeout=" + true;
      }
      else if (type == SearchType.DELIVERY) {
        sql = sql + " and accept_delivery=" + true;
      }
      else if (type == SearchType.PICKUP) {
        sql = sql + " and accept_takeout=" + true;
      }

      if (cusine == null || cusine.length() == 0) {
        cusine = "[";
      }
      //
      if (sorted != null && sorted.equalsIgnoreCase(SortedBY.DESC.name())) {
        sql = sql + "ORDER BY name DESC";
      }
      else if (sorted != null && sorted.equalsIgnoreCase(SortedBY.RAT.name())) {
        sql = sql + " ORDER BY rating DESC";
      }
      else {
        sql = sql + " ORDER BY name ASC";
      }

      List<Restaurant> l = (ArrayList<Restaurant>) getEntityManager().createQuery(sql, Restaurant.class)
          .setParameter("zoneId", zone)
          .setParameter("value", "%" + cusine + "%")
          .getResultList();
      return l;
    }
    catch (NoResultException e) {
      return null;
    }
  }

}
