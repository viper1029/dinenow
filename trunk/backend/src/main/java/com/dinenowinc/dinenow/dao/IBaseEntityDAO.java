package com.dinenowinc.dinenow.dao;

import com.dinenowinc.dinenow.model.helpers.BaseEntity;

import javax.persistence.EntityManager;
import java.util.List;


public interface IBaseEntityDAO<E extends BaseEntity, P> {

  P save(E entity);

  E get(P id);

  List<E> getAll();

  List<E> getByPage(int page, int size);

  void update(E entity);

  void delete(E entity);

  Class<E> getEntityClass();

  EntityManager getEntityManager();

  void changeStatus(E entity);
}
