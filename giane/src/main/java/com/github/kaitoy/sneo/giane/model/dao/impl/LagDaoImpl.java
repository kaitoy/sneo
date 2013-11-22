/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.impl;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.github.kaitoy.sneo.giane.model.Lag;
import com.github.kaitoy.sneo.giane.model.dao.LagDao;

public class LagDaoImpl
extends AbstractDao<Lag> implements LagDao {

  public Lag findByKey(Integer id) {
    return findSingleBy("id", id, Lag.class);
  }

  public Lag findByNameAndNodeId(String name, Integer nodeId) {
    CriteriaBuilder cb = getCriteriaBuilder();
    CriteriaQuery<Lag> cq = cb.createQuery(Lag.class);
    Root<Lag> r = cq.from(Lag.class);
    cq.select(r).where(
      cb.and(
        cb.equal(r.get("name"), name),
        cb.equal(r.get("node"), nodeId)
      )
    );

    try {
      return getEntityManager().createQuery(cq).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

}
