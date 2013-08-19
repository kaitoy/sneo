/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.impl;

import java.util.List;
import com.github.kaitoy.sneo.giane.model.TrapTargetGroup;
import com.github.kaitoy.sneo.giane.model.dao.TrapTargetGroupDao;

public class TrapTargetGroupDaoImpl
extends AbstractDao<TrapTargetGroup> implements TrapTargetGroupDao {

  public List<TrapTargetGroup> list() {
    return list(TrapTargetGroup.class);
  }

  public TrapTargetGroup findByKey(Integer id) {
    return findSingleBy("id", id, TrapTargetGroup.class);
  }

  public TrapTargetGroup findByName(String name) {
    return findSingleBy("name", name, TrapTargetGroup.class);
  }

}
