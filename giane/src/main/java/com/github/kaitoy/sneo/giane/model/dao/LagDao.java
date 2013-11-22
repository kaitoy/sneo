/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao;

import com.github.kaitoy.sneo.giane.model.Lag;

public interface LagDao extends BaseDao<Lag> {

  public Lag findByNameAndNodeId(String name, Integer nodeId);

}
