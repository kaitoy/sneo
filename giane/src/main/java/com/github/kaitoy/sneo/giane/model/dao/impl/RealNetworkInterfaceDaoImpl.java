/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.impl;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.github.kaitoy.sneo.giane.model.RealNetworkInterface;
import com.github.kaitoy.sneo.giane.model.dao.RealNetworkInterfaceDao;

public class RealNetworkInterfaceDaoImpl
extends AbstractDao<RealNetworkInterface> implements RealNetworkInterfaceDao {

  public RealNetworkInterface findByKey(Integer id) {
    return findSingleBy("id", id, RealNetworkInterface.class);
  }

  public RealNetworkInterface findByName(String name) {
    return findSingleBy("name", name, RealNetworkInterface.class);
  }

  public RealNetworkInterface findByNameAndNodeId(String name, Integer nodeId) {
    CriteriaBuilder cb = getCriteriaBuilder();
    CriteriaQuery<RealNetworkInterface> cq = cb.createQuery(RealNetworkInterface.class);
    Root<RealNetworkInterface> r = cq.from(RealNetworkInterface.class);
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
