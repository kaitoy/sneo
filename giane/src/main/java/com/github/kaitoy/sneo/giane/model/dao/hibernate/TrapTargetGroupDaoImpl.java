/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import com.github.kaitoy.sneo.giane.model.TrapTargetGroup;
import com.github.kaitoy.sneo.giane.model.dao.TrapTargetGroupDao;

public class TrapTargetGroupDaoImpl
extends AbstractDao<TrapTargetGroup> implements TrapTargetGroupDao {

  @SuppressWarnings("unchecked")
  public List<TrapTargetGroup> list() {
    return (List<TrapTargetGroup>)getSession()
             .createCriteria(TrapTargetGroup.class).list();
  }

  public TrapTargetGroup findByKey(Integer id) {
    return (TrapTargetGroup)getSession().createCriteria(TrapTargetGroup.class)
             .add(Restrictions.idEq(id))
             .uniqueResult();
  }

  public TrapTargetGroup findByName(String name) {
    return (TrapTargetGroup)getSession().createCriteria(TrapTargetGroup.class)
             .add(Restrictions.eq("name", name))
             .uniqueResult();
  }

}
