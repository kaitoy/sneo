/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.hibernate;

import org.hibernate.criterion.Restrictions;

import com.github.kaitoy.sneo.giane.model.RealNetworkInterface;
import com.github.kaitoy.sneo.giane.model.dao.RealNetworkInterfaceDao;

public class RealNetworkInterfaceDaoImpl
extends AbstractDao<RealNetworkInterface> implements RealNetworkInterfaceDao {

  public RealNetworkInterface findByKey(Integer id) {
    return (RealNetworkInterface)getSession().createCriteria(RealNetworkInterface.class)
             .add(Restrictions.idEq(id))
             .uniqueResult();
  }

  public RealNetworkInterface findByName(String name) {
    return (RealNetworkInterface)getSession().createCriteria(RealNetworkInterface.class)
             .add(Restrictions.eq("name", name))
             .uniqueResult();
  }

  public RealNetworkInterface findByNameAndNodeId(String name, Integer nodeId) {
    return (RealNetworkInterface)getSession().createCriteria(RealNetworkInterface.class)
             .add(Restrictions.eq("node.id", nodeId))
             .add(Restrictions.eq("name", name)).uniqueResult();
  }

}
