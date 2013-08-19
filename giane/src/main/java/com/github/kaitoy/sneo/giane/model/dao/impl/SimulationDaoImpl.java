/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.impl;

import com.github.kaitoy.sneo.giane.model.Simulation;
import com.github.kaitoy.sneo.giane.model.dao.SimulationDao;

public class SimulationDaoImpl extends AbstractDao<Simulation> implements SimulationDao {

  public Simulation findByKey(Integer id) {
    return findSingleBy("id", id, Simulation.class);
  }

  public Simulation findByName(String name) {
    return findSingleBy("name", name, Simulation.class);
  }

}
