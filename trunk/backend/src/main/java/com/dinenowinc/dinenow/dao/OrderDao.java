package com.dinenowinc.dinenow.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import com.dinenowinc.dinenow.model.Order;
import com.dinenowinc.dinenow.model.Item;
import com.dinenowinc.dinenow.model.OrderStatus;
import com.dinenowinc.dinenow.model.Restaurant;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class OrderDao extends BaseEntityDAOImpl<Order, String>{

	@Inject
	public OrderDao(Provider<EntityManager> emf) {
		super(emf);
		entityClass = Order.class;
	}
	                                                 
	public List<Order> findByCustomer(String idCustomer) { 
		try {
			List<Order> l = (ArrayList<Order>) getEntityManager().createQuery("SELECT o FROM Customer c JOIN c.orders o WHERE c.id = :value", Order.class).setParameter("value", idCustomer).getResultList();
			return l;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Order findByCustomerAndOrder(String idCustomer,String orderId) { 
		try {
			 Order order =  getEntityManager().createQuery("SELECT o FROM Customer c JOIN c.orders o WHERE c.id = :value AND o.id = :value2", Order.class)
					 .setParameter("value", idCustomer)
					 .setParameter("value2", orderId)
					 .getSingleResult();
			return order;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//
	@SuppressWarnings("unchecked")
	public List<Order> findByOwner(String restaurant_user_id) {
		try {
			List<Order> l = (ArrayList<Order>) getEntityManager().createNativeQuery("SELECT co.* FROM restaurant r,restaurantuser ru, item i, orderdetail od, order co WHERE r.id = ru.id_restaurant AND r.id = i.id_restaurant AND od.id_item = i.id AND od.id_order = co.id AND ru.id= :value", Order.class).setParameter("value", restaurant_user_id).getResultList();
			return l;
		} catch (Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Order> getListByRestaurant(String restaurant_id, int page, int size){
		int maxRecords = size;
		int startRow = (page-1)*size;
		
		try {
			//List<Order> listItem = (ArrayList<Order>)getEntityManager().createNativeQuery("SELECT co.* FROM restaurant r, item i, orderdetail od, order co WHERE r.id = i.id_restaurant AND od.id_item = i.id AND od.id_order = co.id AND r.id= :value GROUP BY co.id ORDER BY co.receivedAt ASC", Order.class).setParameter("value", restaurant_id).setFirstResult(startRow).setMaxResults(maxRecords).getResultList();
			List<Order> listItem = getEntityManager().createQuery("SELECT distinct r.orders FROM Restaurant r left join r.orders WHERE r.id = :value").setParameter("value", restaurant_id).getResultList();
			System.out.println("88888888888888888888888888888888888888888888"+listItem.size());
			return listItem;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Order> getListByRestaurant(String restaurant_id, Date from, Date to, int page, int size){
		System.out.println(from+":::::::::::::::::::"+to);
		int maxRecords = size;
		int startRow = (page-1)*size;
		try {
			//List<Order> listItem = (ArrayList<Order>)getEntityManager().createNativeQuery("SELECT co.* FROM restaurant r, item i, orderdetail od, order co WHERE r.id = i.id_restaurant AND od.id_item = i.id AND od.id_order = co.id AND r.id= :value AND co.receivedAt BETWEEN :value3 and :value4 GROUP BY co.id ORDER BY co.receivedAt ASC", Order.class).setParameter("value", restaurant_id).setParameter("value3", from).setParameter("value4", to).setFirstResult(startRow).setMaxResults(maxRecords).getResultList();
			List<Order> listItem = getEntityManager().createQuery("SELECT distinct co FROM Restaurant r left join r.orders co WHERE r.id = :value AND co.receivedAt BETWEEN :from AND :to")
					.setParameter("value", restaurant_id)
					.setParameter("from", from)
					.setParameter("to", to)
					.getResultList();
			return listItem;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}    
	}
	
	@SuppressWarnings("unchecked")
	public List<Order> getListByRestaurant(String restaurant_id, OrderStatus status, int page, int size){
		int maxRecords = size;
		int startRow = (page-1)*size;
		try {
			//List<Order> listItem = (ArrayList<Order>)getEntityManager().createNativeQuery("SELECT co.* FROM restaurant r, item i, orderdetail od, order co WHERE r.id = i.id_restaurant AND od.id_item = i.id AND od.id_order = co.id AND r.id= :value AND co.orderStatus = :value2 GROUP BY co.id ORDER BY co.receivedAt ASC", Order.class).setParameter("value", restaurant_id).setParameter("value2", status.ordinal()).setFirstResult(startRow).setMaxResults(maxRecords).getResultList();
			List<Order> listItem = (ArrayList<Order>)getEntityManager().createQuery("SELECT distinct co FROM Restaurant r left join r.orders co WHERE r.id = :value AND co.orderStatus = :value2 GROUP BY co.id ORDER BY co.receivedAt ASC")
					.setParameter("value", restaurant_id)
					.setParameter("value2", status)
					.setFirstResult(startRow).setMaxResults(maxRecords)
					.getResultList();
			System.out.println(listItem.size());
			return listItem;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	

	
	@SuppressWarnings("unchecked")
	public List<Order> getListByRestaurant(String restaurant_id, OrderStatus status, Date from, Date to, int page, int size){
		int maxRecords = size;
		int startRow = (page-1)*size;
		try {
		//	List<Order> listItem = (ArrayList<Order>)getEntityManager().createNativeQuery("SELECT co.* FROM restaurant r, item i, orderdetail od, order co WHERE r.id = i.id_restaurant AND od.id_item = i.id AND od.id_order = co.id AND r.id= :value AND co.orderStatus = :value2 AND co.receivedAt BETWEEN :value3 and :value4 GROUP BY co.id ORDER BY co.receivedAt ASC", Order.class).setParameter("value", restaurant_id).setParameter("value2", status.ordinal()).setParameter("value3", from).setParameter("value4", to).setFirstResult(startRow).setMaxResults(maxRecords).getResultList();
			List<Order> listItem = (ArrayList<Order>)getEntityManager().createQuery("SELECT distinct co FROM Restaurant r left join r.orders co WHERE r.id = :value AND co.orderStatus = :value2 AND co.receivedAt BETWEEN :from AND :to GROUP BY co.id ORDER BY co.receivedAt ASC")
					.setParameter("value", restaurant_id)
					.setParameter("value2", status)
					.setParameter("from", from)
					.setParameter("to", to)
					.setFirstResult(startRow).setMaxResults(maxRecords)
					.getResultList();
			return listItem;
		} catch (Exception e) {
			return null;
		}
	}
	
	

	//SELECT co.* FROM customerorder co, customer c WHERE c.id =co.id_customer AND (co.orderNumber LIKE '%1427%' OR c.firstName LIKE '%1427%' OR c.lastName LIKE '%1427%' OR c.address LIKE '%1427%' OR c.phoneNumber LIKE '%1427%')
	@SuppressWarnings("unchecked")
	public List<Order> searchByOrder(String restaurant_id, String name){ //SELECT r.tax FROM Restaurant r inner join r.tax WHERE r.id = :value
		try {
			//List<Order> listItem = (ArrayList<Order>)getEntityManager().createNativeQuery("SELECT co.* FROM restaurant r, item i, orderdetail od, order co, customer c WHERE r.id = i.id_restaurant AND od.id_item = i.id AND od.id_order = co.id AND r.id= :value AND (co.orderNumber LIKE :value2 OR c.firstName LIKE :value2  OR c.lastName LIKE :value2 OR c.address LIKE :value2 OR c.phoneNumber LIKE :value2) GROUP BY co.id ORDER BY co.receivedAt ASC", Order.class).setParameter("value", restaurant_id).setParameter("value2", "%" + query + "%").getResultList();
			List<Order> listItem = (ArrayList<Order>)getEntityManager().createQuery("SELECT ro FROM Restaurant r,Customer c inner join r.orders ro inner join c.orders co WHERE co.id=ro.id And r.id= :value  AND (c.first_name LIKE :value2  OR c.last_name LIKE :value2 OR str(ro.orderNumber) LIKE :value2 OR c.phone_number LIKE :value2) GROUP BY ro.id ORDER BY ro.receivedAt ASC")
					.setParameter("value", restaurant_id)
					.setParameter("value2","%"+name+"%")
					.getResultList();
			System.out.println(listItem.size());
			return listItem;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Order> searchByOrderAndCustomerPhone(String restaurant_id, String phone){ //SELECT r.tax FROM Restaurant r inner join r.tax WHERE r.id = :value
		try {
			//List<Order> listItem = (ArrayList<Order>)getEntityManager().createNativeQuery("SELECT co.* FROM restaurant r, item i, orderdetail od, order co, customer c WHERE r.id = i.id_restaurant AND od.id_item = i.id AND od.id_order = co.id AND r.id= :value AND (co.orderNumber LIKE :value2 OR c.firstName LIKE :value2  OR c.lastName LIKE :value2 OR c.address LIKE :value2 OR c.phoneNumber LIKE :value2) GROUP BY co.id ORDER BY co.receivedAt ASC", Order.class).setParameter("value", restaurant_id).setParameter("value2", "%" + query + "%").getResultList();
			List<Order> listItem = (ArrayList<Order>)getEntityManager().createQuery("SELECT ro FROM Restaurant r,Customer c inner join r.orders ro inner join c.orders co WHERE co.id=ro.id And r.id= :value  AND (str(ro.orderNumber) LIKE :value2 OR c.phone_number LIKE :value2) GROUP BY ro.id ORDER BY ro.receivedAt ASC")
					.setParameter("value", restaurant_id)
					.setParameter("value2","%"+phone+"%")
					.getResultList();
			System.out.println(listItem.size());
			return listItem;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Order getLastOrder() {
		try {
			Order v = (Order) getEntityManager().createQuery("Select o from Order as o order by o.orderNumber desc",Order.class).setMaxResults(1).getSingleResult();
			return v;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Order> searchByOrderNo(String restaurant_id, long o_no) {
		try {
			//List<Order> listItem = (ArrayList<Order>)getEntityManager().createNativeQuery("SELECT co.* FROM restaurant r, item i, orderdetail od, order co, customer c WHERE r.id = i.id_restaurant AND od.id_item = i.id AND od.id_order = co.id AND r.id= :value AND (co.orderNumber LIKE :value2 OR c.firstName LIKE :value2  OR c.lastName LIKE :value2 OR c.address LIKE :value2 OR c.phoneNumber LIKE :value2) GROUP BY co.id ORDER BY co.receivedAt ASC", Order.class).setParameter("value", restaurant_id).setParameter("value2", "%" + query + "%").getResultList();
			List<Order> listItem = (ArrayList<Order>)getEntityManager().createQuery("SELECT ro FROM Restaurant r inner join r.orders ro  WHERE r.id= :value  AND (ro.orderNumber LIKE :value2) GROUP BY ro.id ORDER BY ro.receivedAt ASC")
					.setParameter("value", restaurant_id)
					.setParameter("value2", o_no)
					.getResultList();
			System.out.println(listItem.size());
			return listItem;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
