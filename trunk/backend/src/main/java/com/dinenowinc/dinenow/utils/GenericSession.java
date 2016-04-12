/*package com.dinenowinc.dinenow.utils;

import org.hibernate.Session;
import org.hibernate.jpa.HibernateEntityManager;

import com.dinenowinc.dinenow.dao.BaseEntityDAOImpl;
import com.dinenowinc.dinenow.dao.IBaseEntityDAO;
import com.dinenowinc.dinenow.model.helpers.BaseEntity;
import com.dinenowinc.dinenow.model.RestaurantUser;

import javax.persistence.EntityManager;

public class GenericSession extends BaseEntityDAOImpl<RestaurantUser, String> {
	
	HibernateEntityManager hem = getEntityManager().unwrap(HibernateEntityManager.class);
	Session session = hem.getSession();
	
	public <T> List<T> getAll(final Class<T> type){
	    final Session session = sessionFactory.getCurrentSession();
	    final Criteria crit = session.createCriteria(type);
	    return crit.list();
	}

	public <T>  getFieldEq(final Class<T> type, final String propertyName, final Object value){
	    final Session session = sessionFactory.getCurrentSession();
	    final Criteria crit = session.createCriteria(type);
	    crit.add(Restrictions.eq(propertyName, value));
	    return crit.list();
	}

}
*/