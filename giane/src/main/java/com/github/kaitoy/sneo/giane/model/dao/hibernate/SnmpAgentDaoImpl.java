/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.hibernate;

import org.hibernate.criterion.Restrictions;

import com.github.kaitoy.sneo.giane.model.SnmpAgent;
import com.github.kaitoy.sneo.giane.model.dao.SnmpAgentDao;

public class SnmpAgentDaoImpl extends AbstractDao<SnmpAgent> implements SnmpAgentDao {

  public SnmpAgent findByKey(Integer id) {
    return (SnmpAgent)getSession().createCriteria(SnmpAgent.class)
             .add(Restrictions.idEq(id))
             .uniqueResult();
  }

}
