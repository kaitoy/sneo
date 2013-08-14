/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.hibernate;

import java.util.Iterator;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.github.kaitoy.sneo.giane.model.TrapTarget;
import com.github.kaitoy.sneo.giane.model.TrapTargetGroup;
import com.github.kaitoy.sneo.giane.model.dao.TrapTargetDao;

public class TrapTargetDaoImpl
extends AbstractDao<TrapTarget> implements TrapTargetDao {

  public TrapTarget findByKey(Integer id) {
    return (TrapTarget)getSession().createCriteria(TrapTarget.class)
             .add(Restrictions.idEq(id))
             .uniqueResult();
  }

  public TrapTarget findByName(String name) {
    return (TrapTarget)getSession().createCriteria(TrapTarget.class)
             .add(Restrictions.eq("name", name))
             .uniqueResult();
  }

  public List<TrapTarget> findByCriteriaAndTrapTargetGroupId(
    DetachedCriteria criteria, Integer trapTargetGroupId, boolean included
  ) {
    @SuppressWarnings("unchecked")
    List<TrapTarget> list
      = criteria.getExecutableCriteria(getSession()).list();

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
