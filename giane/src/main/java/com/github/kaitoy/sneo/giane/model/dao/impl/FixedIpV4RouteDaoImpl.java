/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.impl;

import com.github.kaitoy.sneo.giane.model.FixedIpV4Route;
import com.github.kaitoy.sneo.giane.model.dao.FixedIpV4RouteDao;

public class FixedIpV4RouteDaoImpl
extends AbstractDao<FixedIpV4Route> implements FixedIpV4RouteDao {

  public FixedIpV4Route findByKey(Integer id) {
    return findSingleBy("id", id, FixedIpV4Route.class);
  }

}
