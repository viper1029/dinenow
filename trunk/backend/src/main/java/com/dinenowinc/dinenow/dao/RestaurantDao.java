package com.dinenowinc.dinenow.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.dinenowinc.dinenow.model.Restaurant;
import com.dinenowinc.dinenow.model.Review;
import com.dinenowinc.dinenow.model.SearchOrderBy;
import com.dinenowinc.dinenow.model.SearchType;
import com.dinenowinc.dinenow.model.SortedBY;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.util.GeometricShapeFactory;

public class RestaurantDao extends BaseEntityDAOImpl<Restaurant, String> {

	@Inject
	public RestaurantDao(Provider<EntityManager> emf) {
		super(emf);
		entityClass = Restaurant.class;
	}

	public Restaurant findByItemId(String item_id){
		try {
			Restaurant l = (Restaurant) getEntityManager().createQuery("SELECT r FROM Restaurant r JOIN r.items u WHERE u.id = :id")
					.setParameter("id",item_id).getSingleResult(); 

			return l;
		} catch (NoResultException e) {
			return null;
		}
	}


	public Restaurant findBySizeId(String size_id){
		try {
			Restaurant l = (Restaurant) getEntityManager().createQuery("SELECT r FROM Restaurant r JOIN r.sizes s WHERE s.id = :id")
					.setParameter("id",size_id).getSingleResult(); 

			return l;
		} catch (NoResultException e) {
			return null;
		}
	}

	public Restaurant findByCategoryId(String category_id){
		try {
			Restaurant l = (Restaurant) getEntityManager().createQuery("SELECT r FROM Restaurant r JOIN r.categories c WHERE c.id = :id")
					.setParameter("id",category_id).getSingleResult(); 

			return l;
		} catch (NoResultException e) {
			return null;
		}
	}


	public Restaurant findByMenuId(String menu_id){
		try {
			Restaurant l = (Restaurant) getEntityManager().createQuery("SELECT r FROM Restaurant r JOIN r.menus m WHERE m.id = :id")
					.setParameter("id",menu_id).getSingleResult(); 

			return l;
		} catch (NoResultException e) {
			return null;
		}
	}



	public int getOrdersSize(){
		int empSize = (int) getEntityManager().createQuery("SELECT SIZE(d.orders) FROM Restaurant d").getSingleResult();   
		return empSize;
	}

	public Restaurant findByKeyword(String keyword){
		try { 
			Restaurant l = (Restaurant) getEntityManager().createQuery("SELECT r FROM Restaurant r WHERE r.keyword = :keyword")
					.setParameter("keyword",keyword).getSingleResult(); 

			return l;
		} catch (NoResultException e) {
			return null;
		}
	}

	public Restaurant findByExactUserName(String name) {
		try {
			/*Restaurant l = (Restaurant) getEntityManager()
					.createNativeQuery(
							"SELECT * FROM Restaurant t where lower(t.name) = :value ",
							Restaurant.class).setParameter("value", name)
					.getSingleResult();*/
			Restaurant l = (Restaurant) getEntityManager().createQuery("SELECT t FROM Restaurant t where lower(t.name) = :value ",
					Restaurant.class).setParameter("value", name).getSingleResult();
			return l;
		} catch (NoResultException e) {
			return null;
		}
	}

	public Restaurant findByIDUser(String id) {
		try {

			Restaurant l = (Restaurant) getEntityManager().createQuery("SELECT r FROM Restaurant r JOIN r.users u WHERE u.id = :id")
					.setParameter("id",id).getSingleResult(); 

			/*Restaurant l = (Restaurant) getEntityManager()
					.createNativeQuery(
							"SELECT r.* FROM restaurant r,restaurantuser ru where r.id = ru.id_restaurant and ru.id = :value",
							Restaurant.class).setParameter("value", id)
					.getSingleResult();*/	
			return l;
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Restaurant> findBy(SearchType type, Point location,double distance) {
		System.out.println("LLLLLLLLLLLLLLLLL>>>>>"+type);
		try {
			boolean delivery = false;
			boolean takeout = false;
			if (distance < 0) {
				distance = 0;
			}
			if (type == SearchType.BOTH) {
				delivery = true;
				takeout = true;
				List<Restaurant> l = (ArrayList<Restaurant>) getEntityManager()
						.createNativeQuery("SELECT r.*,(((acos(sin(( :lat *pi()/180)) * sin((X(location)*pi()/180))+cos(( :lat2 *pi()/180)) * cos((X(location)*pi()/180)) * cos((( :lng - Y(location))*pi()/180))))*180/pi())*60*1.1515*1.609344) as distance FROM restaurant r,deliveryzone dz WHERE r.id = dz.id_restaurant AND ST_Contains(dz.deliveryZoneCoords, POINT( :lat3, :lng2)) AND (r.acceptDeliveryOrders = :delivery OR r.acceptTakeOutOrders = :takeout) HAVING distance <= :distance ORDER BY distance", Restaurant.class)
						.setParameter("lat", location.getX())
						.setParameter("lat2", location.getX())
						.setParameter("lng", location.getY())
						.setParameter("lat3", location.getX())
						.setParameter("lng2", location.getY())
						.setParameter("delivery", delivery)
						.setParameter("takeout", takeout)
						.setParameter("distance", distance).getResultList();
				return l;
			}else {
				if (type == SearchType.DELIVERY) {
					delivery = true;
					List<Restaurant> l = (ArrayList<Restaurant>) getEntityManager()
							.createNativeQuery("SELECT r.*,(((acos(sin(( :lat *pi()/180)) * sin((X(location)*pi()/180))+cos(( :lat2 *pi()/180)) * cos((X(location)*pi()/180)) * cos((( :lng - Y(location))*pi()/180))))*180/pi())*60*1.1515*1.609344) as distance FROM restaurant r,deliveryzone dz WHERE r.id = dz.id_restaurant AND ST_Contains(dz.deliveryZoneCoords, POINT( :lat3, :lng2)) AND r.acceptDeliveryOrders = :delivery HAVING distance <= :distance ORDER BY distance", Restaurant.class)
							.setParameter("lat", location.getX())
							.setParameter("lat2", location.getX())
							.setParameter("lng", location.getY())
							.setParameter("lat3", location.getX())
							.setParameter("lng2", location.getY())
							.setParameter("delivery", delivery)
							.setParameter("distance", distance).getResultList();
					return l;
				}else {
					takeout = true;
					List<Restaurant> l = (ArrayList<Restaurant>) getEntityManager()
							.createNativeQuery("SELECT r.*,(((acos(sin(( :lat *pi()/180)) * sin((X(location)*pi()/180))+cos(( :lat2 *pi()/180)) * cos((X(location)*pi()/180)) * cos((( :lng - Y(location))*pi()/180))))*180/pi())*60*1.1515*1.609344) as distance FROM restaurant r,deliveryzone dz WHERE r.id = dz.id_restaurant AND ST_Contains(dz.deliveryZoneCoords, POINT( :lat3, :lng2)) AND r.acceptTakeOutOrders = :takeout HAVING distance <= :distance ORDER BY distance", Restaurant.class)
							.setParameter("lat", location.getX())
							.setParameter("lat2", location.getX())
							.setParameter("lng", location.getY())
							.setParameter("lat3", location.getX())
							.setParameter("lng2", location.getY())
							.setParameter("takeout", takeout)
							.setParameter("distance", distance).getResultList();
					return l;
				}
			}
		} catch (NoResultException e) {
			return null;
		}
	}


	@SuppressWarnings("unchecked")
	public List<Restaurant> findDistance(Point location, double distance,SearchOrderBy orderBy) {
		System.out.println("LLLLLLLLL"+distance);

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
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Restaurant> findDistanceNew(SearchType type,final Point location, double distance,String cusine ,String sorted) {
		String sql = "Select c from Restaurant c where timezoneId=:zoneId and status=0 and cuisine LIKE :value";
		try {
			if (type == SearchType.BOTH) {
				sql =  sql + " and accept_delivery="+true+" and accept_takeout="+true;
			}else if (type == SearchType.DELIVERY) {
				sql = sql +" and accept_delivery="+true;
			}else if (type == SearchType.PICKUP) {
				sql = sql +" and accept_takeout="+true;
			} 
			
			if(cusine == null || cusine.length()==0){
				cusine = "[";
			}

			if(sorted !=null && sorted.equalsIgnoreCase(SortedBY.DESC.name())){
				sql = sql + " ORDER BY name DESC";
			} else 

				if(sorted !=null && sorted.equalsIgnoreCase(SortedBY.RAT.name())) {
					sql = sql + " ORDER BY rating DESC";
				}else {
					sql = sql + " ORDER BY name ASC";
				}

			System.out.println(location.getX() +"::::::::"+location.getY());
			System.out.println(distance);
			Geometry filter = createCircle(location.getX(), location.getY(), distance);
			List<Restaurant> l =  (ArrayList<Restaurant>) getEntityManager().createQuery(sql ,Restaurant.class)
					.setParameter("filter", filter).setParameter("value","%"+cusine+"%").getResultList();
			System.out.println(l.size()+">>>>>>>>>>>>>>>>>>>>>>>>>>>");


			if(sorted !=null && sorted.equalsIgnoreCase(SortedBY.DIST.name())) {
				Comparator<Restaurant> comparator = new Comparator<Restaurant>() {
					public int compare(Restaurant c1, Restaurant c2) {
						if(getDistance(location,c1)==getDistance(location,c2))  
							return 0;  
						else if(getDistance(location,c1)>getDistance(location,c2))  
							return 1;  
						else  
							return -1;  // use your logic
					}
				};
				Collections.sort(l, comparator);
			}

			for(Restaurant resturant : l){
				System.out.println(":::::::::::::DISSISISI"+getDistance(location,resturant));
				resturant.setDistance(getDistance(location,resturant)/1000);
			}

			return l;
		} catch (Exception e) {
			return null;
		}
	}



	public static double getDistance(Point location,Restaurant rs){
		return (location.distance(rs.getLocation())* (Math.PI / 180) * 6378137);
	}

	public static double getRating(Restaurant rs){
		double rate = 0 ;
		for(Review r : rs.getReviews()){
			// System.out.println(location.distance(rs.getLocation())* (Math.PI / 180) * 6378137);;// in meteres
			rate = rate + r.getRating();
		}
		System.out.println(rate+"><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		return rate/rs.getReviews().size();
	}

	/*	@SuppressWarnings("unchecked")
	public List<Restaurant> findDistanceNew(SearchType type,Point location, double distance,String cusine) {
		boolean delivery = false;
		boolean takeout = false;
		String sql = null;
		try {
			//FullTextSession fullTextSession = Search.getFullTextSession(getEntityManager().getEntityManagerFactory().unwrap(Session.class));
			//org.hibernate.Transaction tx = fullTextSession.beginTransaction();

			FullTextEntityManager fullTextEntityManager =
				    org.hibernate.search.jpa.Search.getFullTextEntityManager(getEntityManager());

			org.hibernate.search.query.dsl.QueryBuilder builder = fullTextEntityManager.getSearchFactory()
					  .buildQueryBuilder().forEntity( Restaurant.class ).get();

			org.apache.lucene.search.Query luceneQuery = builder.spatial()
					  .onDefaultCoordinates()
					  .within( 10, Unit.KM )
					  .ofLatitude( location.getX() )
					  .andLongitude( location.getY() )
					  .createQuery();

			// wrap Lucene query in a javax.persistence.Query
			javax.persistence.Query jpaQuery =
			    fullTextEntityManager.createFullTextQuery(luceneQuery, Restaurant.class);
			// execute search
			List<Restaurant> l = jpaQuery.getResultList();

			System.out.println(l.size()+">>>>>>>>>>>>>>>>>>>>>>>>>>>");
			return l;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	 */
	@SuppressWarnings("unchecked")
	public List<Restaurant> findDistanceType(SearchType type) {
		try {

			List<Restaurant> l =  (ArrayList<Restaurant>) getEntityManager().createQuery("" ,Restaurant.class)
					.getResultList();
			System.out.println(l.size()+">>>>>>>>>>>>>>>>>>>>>>>>>>>");
			return l;
		} catch (NoResultException e) {
			return null;
		}
	}
	private static Geometry createCircle(double x, double y, final double RADIUS) {
		GeometricShapeFactory shapeFactory = new GeometricShapeFactory();
		shapeFactory.setNumPoints(32);
		shapeFactory.setCentre(new Coordinate(x, y)); //there are your coordinates
		shapeFactory.setSize( (RADIUS * 2));  //this is how you set the radius
		return shapeFactory.createCircle();
	}


	@SuppressWarnings("unchecked")
	public List<Restaurant> findByLocation(SearchType type, Point location,double distance) {
		try {
			List<Restaurant> restaurants = (List<Restaurant>) getEntityManager().createNativeQuery("CALL findDelivery( :lat, :lng, :distance)",Restaurant.class).setParameter("lat", location.getX()).setParameter("lng", location.getY()).setParameter("distance", distance).getResultList();
			List<Restaurant> result = new ArrayList<Restaurant>();
			switch (type) {
			case BOTH:
				result = new ArrayList<Restaurant>();
				result = restaurants;
				break;
			case DELIVERY:
				result = new ArrayList<Restaurant>();
				for (Restaurant restaurant : restaurants) {
					if (restaurant.isAcceptDelivery()) {
						result.add(restaurant);
					}
				}
				break;
			case PICKUP:
				result = new ArrayList<Restaurant>();
				for (Restaurant restaurant : restaurants) {
					if (restaurant.isAcceptTakeout()) {
						result.add(restaurant);
					}
				}
				break;

			default:
				result = new ArrayList<Restaurant>();
				result = restaurants;
				break;
			}
			return result;
		} catch (Exception e) {
			return null;
		}
	}

	public List<Restaurant> findDistanceNew(SearchType type, String cusine , String sorted, String zone) {
		String sql = "Select c from Restaurant c where timezoneId=:zoneId and status=0 and cuisine LIKE :value";
		try {
			if (type == SearchType.BOTH) {
				sql =  sql + " and accept_delivery="+true+" and accept_takeout="+true;
			}else if (type == SearchType.DELIVERY) {
				sql = sql +" and accept_delivery="+true;
			}else if (type == SearchType.PICKUP) {
				sql = sql +" and accept_takeout="+true;
			} 
			
			if(cusine == null || cusine.length()==0){
				cusine = "[";
			}
			//	
			if(sorted !=null && sorted.equalsIgnoreCase(SortedBY.DESC.name())){
				sql = sql + "ORDER BY name DESC";
			}  else 

				if(sorted !=null && sorted.equalsIgnoreCase(SortedBY.RAT.name())) {
					sql = sql + " ORDER BY rating DESC";
				}else {
					sql = sql + " ORDER BY name ASC";
				}

			List<Restaurant> l =  (ArrayList<Restaurant>) getEntityManager().createQuery(sql ,Restaurant.class)
					.setParameter("zoneId", zone)
					.setParameter("value","%"+cusine+"%")
					.getResultList();


			/*			System.out.println(l);
			if(sorted !=null && sorted.equalsIgnoreCase(SortedBY.RAT.name())) {
				System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
			Comparator<Restaurant> comparator = new Comparator<Restaurant>() {
			    public int compare(Restaurant c1, Restaurant c2) {
			    	if(getRating(c1)==getRating(c2))  
			    		return 0;  
			    		else if(getRating(c1)>getRating(c2))  
			    		return 1;  
			    		else  
			    		return -1;  // use your logic
			    }
			};
			Collections.sort(l, comparator);
			}
			System.out.println(l);*/
			return l;
		} catch(NoResultException e){
			return null;
		}
	}

}
