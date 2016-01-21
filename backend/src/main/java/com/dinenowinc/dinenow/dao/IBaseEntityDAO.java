package com.dinenowinc.dinenow.dao;

import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;

import com.dinenowinc.dinenow.model.BaseEntity;
import com.dinenowinc.dinenow.model.Version;


public interface IBaseEntityDAO<E extends BaseEntity, P> {
	 /**
	  * Persist the indicated entity to database
	  * @param entity
	  * @return the primary key
	  */
	 P save(E entity);
	  
	 /**
	  * Retrieve an object using indicated ID
	  * @param id
	  * @return
	  */
	 E findOne(P id);
	 
	 /**
	  * Update indicated entity to database
	  * @param entity
	  */
	 void update(E entity);
	  
	 /**
	  * Delete indicated entity from database
	  * @param entity
	  */
	 void delete(E entity);
	  
	 /**
	  * Return the entity class
	  * @return
	  */
	 Class<E> getEntityClass();
	  
	 /**
	  * Get the entity manager
	  * @return
	  */
	 EntityManager getEntityManager();
	  
	 /**
	  *
	  * @return
	  */
	 List<E> findAll();
	 
	 List<E> findAll(int page, int size);
	 
	 /**
	  * Change Status indicated entity from database
	  * @param entity
	  */
	void changeStatus(E entity);
}
