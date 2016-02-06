package com.dinenowinc.dinenow.dao;

import com.dinenowinc.dinenow.model.BaseEntity;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

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
  public E get(P id) {
    return emf.get().find(getEntityClass(), id);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<E> getAll() {
    return this.emf.get().createNamedQuery(getEntityClass().getSimpleName() + ".GetAll").getResultList();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<E> getByPage(int page, int size) {

    int maxRecords = size;
    int startRow = (page - 1) * size;

    return this.emf.get().createNamedQuery(
        getEntityClass().getSimpleName() + ".GetAll").setFirstResult(startRow).setMaxResults(maxRecords).getResultList();
  }

  @Override
  @Transactional
  public void update(E entity) {
    entity.setModifiedDate(new Date());
    if (entity.getCreatedBy() == null) {
      entity.setCreatedBy("system");
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

  public EntityTransaction getTransaction() {
    return getEntityManager().getTransaction();
  }
}
