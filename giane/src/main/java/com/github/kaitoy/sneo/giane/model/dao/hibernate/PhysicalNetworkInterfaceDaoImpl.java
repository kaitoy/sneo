/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.hibernate;

import java.util.Iterator;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.github.kaitoy.sneo.giane.model.PhysicalNetworkInterface;
import com.github.kaitoy.sneo.giane.model.Vlan;
import com.github.kaitoy.sneo.giane.model.dao.PhysicalNetworkInterfaceDao;

public class PhysicalNetworkInterfaceDaoImpl
extends AbstractDao<PhysicalNetworkInterface> implements PhysicalNetworkInterfaceDao {

  public PhysicalNetworkInterface findByKey(Integer id) {
    return (PhysicalNetworkInterface)getSession().createCriteria(PhysicalNetworkInterface.class)
             .add(Restrictions.idEq(id))
             .uniqueResult();
  }

  public List<PhysicalNetworkInterface> findByCriteriaAndVlanId(
    DetachedCriteria criteria, Integer vlanId, boolean included
  ) {
    @SuppressWarnings("unchecked")
    List<PhysicalNetworkInterface> list
      = criteria.getExecutableCriteria(getSession()).list();

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

  @SuppressWarnings("unchecked")
  public List<PhysicalNetworkInterface> listSingles() {
    return getSession().createCriteria(PhysicalNetworkInterface.class)
             .add(Restrictions.isNull("l2Connection")).list();
  }

  public PhysicalNetworkInterface findByNameAndNodeId(String name, Integer nodeId) {
    return (PhysicalNetworkInterface)getSession().createCriteria(PhysicalNetworkInterface.class)
             .add(Restrictions.eq("node.id", nodeId))
             .add(Restrictions.eq("name", name)).uniqueResult();
  }

}
