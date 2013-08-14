/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao;

import com.github.kaitoy.sneo.giane.model.Vlan;

public interface VlanDao extends BaseDao<Vlan> {

  public Vlan findByNameAndNodeId(String name, Integer nodeId);

}
