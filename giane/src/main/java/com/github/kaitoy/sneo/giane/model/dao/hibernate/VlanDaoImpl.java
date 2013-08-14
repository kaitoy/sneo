/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.hibernate;

import org.hibernate.criterion.Restrictions;

import com.github.kaitoy.sneo.giane.model.Vlan;
import com.github.kaitoy.sneo.giane.model.dao.VlanDao;

public class VlanDaoImpl
extends AbstractDao<Vlan> implements VlanDao {

  public Vlan findByKey(Integer id) {
    return (Vlan)getSession().createCriteria(Vlan.class)
             .add(Restrictions.idEq(id))
             .uniqueResult();
  }

  public Vlan findByNameAndNodeId(String name, Integer nodeId) {
    return (Vlan)getSession().createCriteria(Vlan.class)
             .add(Restrictions.eq("node.id", nodeId))
             .add(Restrictions.eq("name", name)).uniqueResult();
  }

}
