package com.dinenowinc.dinenow.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import com.dinenowinc.dinenow.model.AccessToken;
import com.dinenowinc.dinenow.model.SubMenu;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class SubMenuDao extends BaseEntityDAOImpl<SubMenu, String>{

	@Inject
	public SubMenuDao(Provider<EntityManager> emf) {
		super(emf);
		entityClass = SubMenu.class;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<SubMenu> getListByUser(AccessToken accToken){
    	try {
    		List<SubMenu> listSubmenu = (ArrayList<SubMenu>)getEntityManager().createNativeQuery("SELECT sm.* FROM restaurantuser ru,restaurant r, submenu sm where ru.id_restaurant = r.id and sm.id_restaurant = r.id and ru.id = :value", SubMenu.class).setParameter("value", accToken.getId().toString()).getResultList();
            return listSubmenu;
		} catch (Exception e) {
			return null;
		}
	}
	
	//SELECT sm.* FROM submenu sm WHERE sm.id_restaurant = 'de1ff90f-f125-4295-a511-7d7bf4475154'
	@SuppressWarnings("unchecked")
	public List<SubMenu> getListByRestaurant(String restaurant_id){
    	try {
    		List<SubMenu> listSubmenu = (ArrayList<SubMenu>)getEntityManager().createNativeQuery("SELECT sm.* FROM submenu sm WHERE sm.id_restaurant = :value", SubMenu.class).setParameter("value", restaurant_id).getResultList();
            return listSubmenu;
		} catch (Exception e) {
			return null;
		}
	}
	
}
