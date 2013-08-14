/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.hibernate;

import org.hibernate.criterion.Restrictions;

import com.github.kaitoy.sneo.giane.model.FixedIpV4Route;
import com.github.kaitoy.sneo.giane.model.dao.FixedIpV4RouteDao;

public class FixedIpV4RouteDaoImpl
extends AbstractDao<FixedIpV4Route> implements FixedIpV4RouteDao {

  public FixedIpV4Route findByKey(Integer id) {
    return (FixedIpV4Route)getSession().createCriteria(FixedIpV4Route.class)
             .add(Restrictions.idEq(id))
             .uniqueResult();
  }

}
