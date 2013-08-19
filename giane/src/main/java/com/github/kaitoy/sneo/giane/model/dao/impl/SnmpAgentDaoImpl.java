/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.impl;

import com.github.kaitoy.sneo.giane.model.SnmpAgent;
import com.github.kaitoy.sneo.giane.model.dao.SnmpAgentDao;

public class SnmpAgentDaoImpl extends AbstractDao<SnmpAgent> implements SnmpAgentDao {

  public SnmpAgent findByKey(Integer id) {
    return findSingleBy("id", id, SnmpAgent.class);
  }

}
