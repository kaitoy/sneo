/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.impl;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.github.kaitoy.sneo.giane.model.L2Connection;
import com.github.kaitoy.sneo.giane.model.dao.L2ConnectionDao;

public class L2ConnectionDaoImpl
extends AbstractDao<L2Connection> implements L2ConnectionDao {

  public L2Connection findByKey(Integer id) {
    return findSingleBy("id", id, L2Connection.class);
  }

  public L2Connection findByNameAndNetworkId(String name, Integer networkId) {
    CriteriaBuilder cb = getCriteriaBuilder();
    CriteriaQuery<L2Connection> cq = cb.createQuery(L2Connection.class);
    Root<L2Connection> r = cq.from(L2Connection.class);
    cq.select(r).where(
      cb.and(
        cb.equal(r.get("name"), name),
        cb.equal(r.get("network"), networkId)
      )
    );

    try {
      return getEntityManager().createQuery(cq).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

}
