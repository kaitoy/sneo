/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao;

import java.util.List;

import com.github.kaitoy.sneo.giane.model.TrapTargetGroup;

public interface TrapTargetGroupDao extends BaseDao<TrapTargetGroup> {

  public List<TrapTargetGroup> list();

  public TrapTargetGroup findByName(String name);

}
