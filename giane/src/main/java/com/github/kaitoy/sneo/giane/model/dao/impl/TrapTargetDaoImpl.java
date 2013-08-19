/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.impl;

import java.util.Iterator;
import java.util.List;
import javax.persistence.criteria.CriteriaQuery;
import com.github.kaitoy.sneo.giane.model.TrapTarget;
import com.github.kaitoy.sneo.giane.model.TrapTargetGroup;
import com.github.kaitoy.sneo.giane.model.dao.TrapTargetDao;

public class TrapTargetDaoImpl
extends AbstractDao<TrapTarget> implements TrapTargetDao {

  public TrapTarget findByKey(Integer id) {
    return findSingleBy("id", id, TrapTarget.class);
  }

  public TrapTarget findByName(String name) {
    return findSingleBy("name", name, TrapTarget.class);
  }

  public List<TrapTarget> findByCriteriaAndTrapTargetGroupId(
    CriteriaQuery<TrapTarget> criteria,
    Integer trapTargetGroupId,
    boolean included
  ) {
    List<TrapTarget> list = findByCriteria(criteria);

    Iterator<TrapTarget> iter = list.iterator();
    while (iter.hasNext()) {
      TrapTarget tt = iter.next();
      boolean found = false;

      for (TrapTargetGroup ttg: tt.getTrapTargetGroups()) {
        if (ttg.getId().equals(trapTargetGroupId)) {
          found = true;
          break;
        }
      }

      if ((found && !included) || (!found && included)) {
        iter.remove();
      }
    }

    return list;
  }

}
