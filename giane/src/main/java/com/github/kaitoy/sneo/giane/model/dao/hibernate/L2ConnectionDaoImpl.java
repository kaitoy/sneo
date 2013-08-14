/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.hibernate;

import org.hibernate.criterion.Restrictions;

import com.github.kaitoy.sneo.giane.model.L2Connection;
import com.github.kaitoy.sneo.giane.model.dao.L2ConnectionDao;

public class L2ConnectionDaoImpl
extends AbstractDao<L2Connection> implements L2ConnectionDao {

  public L2Connection findByKey(Integer id) {
    return (L2Connection)getSession().createCriteria(L2Connection.class)
             .add(Restrictions.idEq(id))
             .uniqueResult();
  }

  public L2Connection findByNameAndNetworkId(String name, Integer networkId) {
    return (L2Connection)getSession().createCriteria(L2Connection.class)
             .add(Restrictions.eq("network.id", networkId))
             .add(Restrictions.eq("name", name))
             .uniqueResult();
  }

}
