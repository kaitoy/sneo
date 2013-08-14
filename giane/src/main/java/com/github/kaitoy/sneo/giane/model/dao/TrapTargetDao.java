/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.github.kaitoy.sneo.giane.model.TrapTarget;

public interface TrapTargetDao extends BaseDao<TrapTarget> {

  public TrapTarget findByName(String name);

  public List<TrapTarget> findByCriteriaAndTrapTargetGroupId(
    DetachedCriteria criteria, Integer trapTargetGroupId, boolean included
  );

}
