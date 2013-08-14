/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao;

import com.github.kaitoy.sneo.giane.model.L2Connection;

public interface L2ConnectionDao extends BaseDao<L2Connection> {

  public L2Connection findByNameAndNetworkId(String name, Integer networkId);

}
