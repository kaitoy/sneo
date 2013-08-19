/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao;

import java.util.List;
import javax.persistence.criteria.CriteriaQuery;
import com.github.kaitoy.sneo.giane.model.TrapTarget;

public interface TrapTargetDao extends BaseDao<TrapTarget> {

  public TrapTarget findByName(String name);

  public List<TrapTarget> findByCriteriaAndTrapTargetGroupId(
    CriteriaQuery<TrapTarget> criteria,
    Integer trapTargetGroupId, 
    boolean included
  );

}
