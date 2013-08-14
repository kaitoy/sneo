/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import com.github.kaitoy.sneo.giane.model.AdditionalIpV4RouteGroup;
import com.github.kaitoy.sneo.giane.model.dao.AdditionalIpV4RouteGroupDao;

public class AdditionalIpV4RouteGroupDaoImpl
extends AbstractDao<AdditionalIpV4RouteGroup> implements AdditionalIpV4RouteGroupDao {

  @SuppressWarnings("unchecked")
  public List<AdditionalIpV4RouteGroup> list() {
    return (List<AdditionalIpV4RouteGroup>)getSession()
             .createCriteria(AdditionalIpV4RouteGroup.class).list();
  }

  public AdditionalIpV4RouteGroup findByKey(Integer id) {
    return (AdditionalIpV4RouteGroup)getSession().createCriteria(AdditionalIpV4RouteGroup.class)
             .add(Restrictions.idEq(id))
             .uniqueResult();
  }

  public AdditionalIpV4RouteGroup findByName(String name) {
    return (AdditionalIpV4RouteGroup)getSession().createCriteria(AdditionalIpV4RouteGroup.class)
             .add(Restrictions.eq("name", name))
             .uniqueResult();
  }

}
