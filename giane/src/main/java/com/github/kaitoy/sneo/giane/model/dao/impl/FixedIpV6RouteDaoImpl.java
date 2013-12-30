/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.impl;

import com.github.kaitoy.sneo.giane.model.FixedIpV6Route;
import com.github.kaitoy.sneo.giane.model.dao.FixedIpV6RouteDao;

public class FixedIpV6RouteDaoImpl
extends AbstractDao<FixedIpV6Route> implements FixedIpV6RouteDao {

  public FixedIpV6Route findByKey(Integer id) {
    return findSingleBy("id", id, FixedIpV6Route.class);
  }

}
