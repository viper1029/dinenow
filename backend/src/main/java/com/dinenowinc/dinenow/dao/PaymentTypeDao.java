package com.dinenowinc.dinenow.dao;

import javax.persistence.EntityManager;

import com.dinenowinc.dinenow.model.PaymentType;
import com.dinenowinc.dinenow.model.Restaurant;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class PaymentTypeDao extends BaseEntityDAOImpl<PaymentType, String>{

	@Inject
	public PaymentTypeDao(Provider<EntityManager> emf) {
		super(emf);
		entityClass = PaymentType.class;
	}	
	
	public PaymentType findByName(String name) {
		try {
			PaymentType l = (PaymentType) getEntityManager().createQuery("SELECT t FROM PaymentType t where lower(t.name) = :value ",
					PaymentType.class).setParameter("value", name).getSingleResult();
			return l;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
