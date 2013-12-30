/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
 */

package com.github.kaitoy.sneo.giane.model.dao.impl;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

abstract class AbstractDao<T> implements EntityManagerInjectee {

  private EntityManager entityManager;

  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  protected EntityManager getEntityManager() {
    return entityManager;
  }

  public CriteriaBuilder getCriteriaBuilder() {
    return entityManager.getCriteriaBuilder();
  }

  public void create(T obj) throws Exception {
    entityManager.persist(obj);
  }

  public void update(T obj) throws Exception {
    entityManager.merge(obj);
  }

  public void update(List<T> objs) throws Exception {
    for (T obj: objs) {
      entityManager.merge(obj);
    }
  }

  public void delete(T obj) throws Exception {
    entityManager.remove(obj);
  }

  public void delete(List<T> objs) throws Exception {
    for (T obj: objs) {
      entityManager.remove(obj);
    }
  }

  public List<T> findByCriteria(CriteriaQuery<T> cq) {
    return entityManager.createQuery(cq).getResultList();
  }

  protected T findSingleBy(String propName, Object propValue, Class<T> modelClass) {
    CriteriaBuilder cb = getCriteriaBuilder();
    CriteriaQuery<T> cq = cb.createQuery(modelClass);
    Root<T> r = cq.from(modelClass);
    cq.select(r).where(cb.equal(r.get(propName), propValue));

    try {
      return getEntityManager().createQuery(cq).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  protected List<T> list(Class<T> modelClass) {
    CriteriaBuilder cb = getCriteriaBuilder();
    CriteriaQuery<T> cq = cb.createQuery(modelClass);
    cq.select(cq.from(modelClass));

    return getEntityManager().createQuery(cq).getResultList();
  }

}
