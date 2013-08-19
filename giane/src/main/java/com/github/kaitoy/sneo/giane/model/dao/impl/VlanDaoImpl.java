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
import com.github.kaitoy.sneo.giane.model.Vlan;
import com.github.kaitoy.sneo.giane.model.dao.VlanDao;

public class VlanDaoImpl
extends AbstractDao<Vlan> implements VlanDao {

  public Vlan findByKey(Integer id) {
    return findSingleBy("id", id, Vlan.class);
  }

  public Vlan findByNameAndNodeId(String name, Integer nodeId) {
    CriteriaBuilder cb = getCriteriaBuilder();
    CriteriaQuery<Vlan> cq = cb.createQuery(Vlan.class);
    Root<Vlan> r = cq.from(Vlan.class);
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
