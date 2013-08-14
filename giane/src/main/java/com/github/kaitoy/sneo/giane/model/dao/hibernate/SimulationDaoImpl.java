/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.hibernate;

import org.hibernate.criterion.Restrictions;

import com.github.kaitoy.sneo.giane.model.Simulation;
import com.github.kaitoy.sneo.giane.model.dao.SimulationDao;

public class SimulationDaoImpl extends AbstractDao<Simulation> implements SimulationDao {

  public Simulation findByKey(Integer id) {
    return (Simulation)getSession().createCriteria(Simulation.class)
             .add(Restrictions.idEq(id))
             .uniqueResult();
  }

  public Simulation findByName(String name) {
    return (Simulation)getSession().createCriteria(Simulation.class)
             .add(Restrictions.eq("name", name))
             .uniqueResult();
  }

}
