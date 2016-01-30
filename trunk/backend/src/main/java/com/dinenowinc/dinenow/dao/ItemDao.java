package com.dinenowinc.dinenow.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;


import com.dinenowinc.dinenow.model.Item;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ItemDao extends BaseEntityDAOImpl<Item, String>{

	@Inject
	public ItemDao(Provider<EntityManager> emf) {
		super(emf);
		entityClass = Item.class;
	}

	@SuppressWarnings("unchecked")
	public List<Item> getListItemByOrderDetails(String id_orderdatail){//SELECT c FROM Customer c JOIN c.customerOrders co WHERE co.id = :value
		try {  //SELECT o FROM Customer c ,Order o JOIN c.customerOrders o WHERE c.id = :value
			// List<Item> listItem = (ArrayList<Item>)getEntityManager().createNativeQuery("SELECT i.* FROM orderdetail od, item i WHERE od.id_item = i.id AND od.id= :value", Item.class).setParameter("value", id_orderdatail).getResultList();
			List<Item> listItem = (ArrayList<Item>)getEntityManager().createQuery("SELECT i FROM Item i JOIN i.orderDetails od WHERE od.id= :value", Item.class).setParameter("value", id_orderdatail).getResultList();
			return listItem;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Item getListItemByCartItems(String cartitem_id){
		try {  
			 Item item = getEntityManager().createQuery("SELECT i FROM Item i JOIN i.cartItems ci WHERE ci.id= :value", Item.class).setParameter("value", cartitem_id).getSingleResult();
			return item;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	//SELECT * FROM item i WHERE i.id_restaurant = 'de1ff90f-f125-4295-a511-7d7bf4475154'
	@SuppressWarnings("unchecked")
	public List<Item> getListByRestaurant(String restaurant_id){
		try {
			List<Item> listItem = (ArrayList<Item>)getEntityManager().createNativeQuery("SELECT i.* FROM item i WHERE i.id_restaurant = :value", Item.class).setParameter("value", restaurant_id).getResultList();
			return listItem;
		} catch (NoResultException e) {
			return null;
		}
	}


	public Item findOne(String item_id, String restaurant_id) {//SELECT ru FROM Restaurant r, RestaurantUser ru JOIN r.users ru WHERE r.id = :value and ru.status=0
		try {           
			
			System.out.println(item_id+"))))))))))))))))))))"+restaurant_id);
			 Item item = (Item) getEntityManager().createQuery("SELECT i FROM Restaurant r, Item i inner join r.items i where i.id = :value and r.id = :value2", Item.class)
					                              .setParameter("value", item_id)
					                              .setParameter("value2", restaurant_id)
					                              .getSingleResult();
			 System.out.println("(((((((((((((((((((((("+item);
			return item;
		} catch (NoResultException e) {
			return null;
		}
	}

	
	
	
}
