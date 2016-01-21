package com.dinenowinc.dinenow.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.jpa.HibernateEntityManager;

import com.dinenowinc.dinenow.model.BaseEntity;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

public abstract class BaseEntityDAOImpl<E extends BaseEntity, P> implements IBaseEntityDAO<E, P> {

  private final Provider<EntityManager> emf;

  @Inject
  public BaseEntityDAOImpl(Provider<EntityManager> emf) {
    this.emf = emf;
  }

  Class<E> entityClass;

  @Override
  @Transactional
  @SuppressWarnings("unchecked")
  public P save(E entity) {
    entity.setCreatedDate(new Date());
    entity.setModifiedDate(new Date());
    emf.get().persist(entity);

    return (P) entity.getId();
  }

  @Transactional
  public boolean saveAll(List<E> list) {
    for (E e : list) {
      e.setCreatedDate(new Date());
      e.setModifiedDate(new Date());
      emf.get().persist(e);
    }
    return true;
  }

  @Override
  public E findOne(P id) {
    return emf.get().find(getEntityClass(), id);
  }

  @SuppressWarnings("unchecked")
  public E findByCriteria(Criterion con) {
    HibernateEntityManager hem = getEntityManager().unwrap(HibernateEntityManager.class);
    Session session = hem.getSession();
    Criteria cr = session.createCriteria(getEntityClass());
    cr.add(con);
    return (E) cr.uniqueResult();
  }

  @Override
  @Transactional
  public void update(E entity) {
    entity.setModifiedDate(new Date());
    if (entity.getCreatedBy() == null) {
      entity.setCreatedBy("own");
    }
    if (entity.getCreatedDate() == null) {
      entity.setCreatedDate(new Date());
    }
    emf.get().merge(entity);
  }

  @Override
  @Transactional
  public void delete(E entity) {
    emf.get().remove(entity);
  }

  @Override
  @Transactional
  public void changeStatus(E entity) {
    entity.setStatus(1);
    emf.get().merge(entity);
  }

  @Override
  public EntityManager getEntityManager() {
    return emf.get();
  }

  @Override
  @SuppressWarnings("unchecked")
  public Class<E> getEntityClass() {
    if (entityClass == null) {
      Type type = getClass().getGenericSuperclass();
      if (type instanceof ParameterizedType) {
        ParameterizedType paramType = (ParameterizedType) type;

        entityClass = (Class<E>) paramType.getActualTypeArguments()[0];

      }
      else {
        throw new IllegalArgumentException(
            "Could not guess entity class by reflection");
      }
    }
    return entityClass;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<E> findAll() {
    return this.emf.get().createNamedQuery(
        getEntityClass().getSimpleName() + ".GetAll").getResultList();
  }


  @SuppressWarnings("unchecked")
  @Override
  public List<E> findAll(int page, int size) {

    int maxRecords = size;
    int startRow = (page - 1) * size;

    return this.emf.get().createNamedQuery(
        getEntityClass().getSimpleName() + ".GetAll").setFirstResult(startRow).setMaxResults(maxRecords).getResultList();
  }

  public EntityTransaction getTransaction() {
    return getEntityManager().getTransaction();
  }

}
