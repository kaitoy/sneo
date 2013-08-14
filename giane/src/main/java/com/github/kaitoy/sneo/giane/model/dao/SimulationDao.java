/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao;

import com.github.kaitoy.sneo.giane.model.Simulation;

public interface SimulationDao
extends BaseDao<Simulation> {

  public Simulation findByName(String name);

}
