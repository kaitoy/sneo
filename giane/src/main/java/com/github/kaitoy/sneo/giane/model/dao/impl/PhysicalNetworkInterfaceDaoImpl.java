/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.impl;

import java.util.Iterator;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.github.kaitoy.sneo.giane.model.PhysicalNetworkInterface;
import com.github.kaitoy.sneo.giane.model.Vlan;
import com.github.kaitoy.sneo.giane.model.dao.PhysicalNetworkInterfaceDao;

public class PhysicalNetworkInterfaceDaoImpl
extends AbstractDao<PhysicalNetworkInterface> implements PhysicalNetworkInterfaceDao {

  public PhysicalNetworkInterface findByKey(Integer id) {
    return findSingleBy("id", id, PhysicalNetworkInterface.class);
  }

  public List<PhysicalNetworkInterface> findByCriteriaAndVlanId(
    CriteriaQuery<PhysicalNetworkInterface> criteria,
    Integer vlanId,
    boolean included
  ) {
    List<PhysicalNetworkInterface> list = findByCriteria(criteria);

    Iterator<PhysicalNetworkInterface> iter = list.iterator();
    while (iter.hasNext()) {
      PhysicalNetworkInterface nif = iter.next();
      boolean found = false;

      for (Vlan vlan: nif.getVlans()) {
        if (vlan.getId().equals(vlanId)) {
          found = true;
          break;
        }
      }

      if ((found && !included) || (!found && included)) {
        iter.remove();
      }
    }

    return list;
  }

  public List<PhysicalNetworkInterface> listSingles() {
    CriteriaBuilder cb = getCriteriaBuilder();
    CriteriaQuery<PhysicalNetworkInterface> cq
      = cb.createQuery(PhysicalNetworkInterface.class);
    Root<PhysicalNetworkInterface> r = cq.from(PhysicalNetworkInterface.class);
    cq.select(r).where(cb.isNull(r.get("l2Connection")));

    return getEntityManager().createQuery(cq).getResultList();
  }

  public PhysicalNetworkInterface findByNameAndNodeId(String name, Integer nodeId) {
    CriteriaBuilder cb = getCriteriaBuilder();
    CriteriaQuery<PhysicalNetworkInterface> cq = cb.createQuery(PhysicalNetworkInterface.class);
    Root<PhysicalNetworkInterface> r = cq.from(PhysicalNetworkInterface.class);
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
