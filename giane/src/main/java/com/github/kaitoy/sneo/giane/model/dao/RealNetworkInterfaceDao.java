/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao;

import com.github.kaitoy.sneo.giane.model.RealNetworkInterface;

public interface RealNetworkInterfaceDao
extends BaseDao<RealNetworkInterface> {

  public RealNetworkInterface findByName(String name);

  public RealNetworkInterface findByNameAndNodeId(
    String name, Integer nodeId
  );

}
