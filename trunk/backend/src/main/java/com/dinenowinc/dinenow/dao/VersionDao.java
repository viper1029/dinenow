package com.dinenowinc.dinenow.dao;


import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.dinenowinc.dinenow.model.Version;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class VersionDao extends BaseEntityDAOImpl<Version, String>{

	@Inject
	public VersionDao(Provider<EntityManager> emf) {
		super(emf);
		entityClass = Version.class;
	}

	public Version getVersion() {

		try {
			Version v = (Version) getEntityManager().createQuery("SELECT t FROM Version t order by t.created_by desc ",
					Version.class).getSingleResult();	
			return v;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	
	

	
	
}
