/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao;

import java.util.List;

import com.github.kaitoy.sneo.giane.model.Network;

public interface NetworkDao extends BaseDao<Network> {

  public List<Network> list();

  public Network findByName(String name);

}
