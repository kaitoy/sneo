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

import com.github.kaitoy.sneo.giane.model.AdditionalIpV4Route;
import com.github.kaitoy.sneo.giane.model.AdditionalIpV4RouteGroup;
import com.github.kaitoy.sneo.giane.model.dao.AdditionalIpV4RouteDao;

public class AdditionalIpV4RouteDaoImpl
extends AbstractDao<AdditionalIpV4Route> implements AdditionalIpV4RouteDao {

  public AdditionalIpV4Route findByKey(Integer id) {
    return (AdditionalIpV4Route)getSession()
             .createCriteria(AdditionalIpV4Route.class)
             .add(Restrictions.idEq(id))
             .uniqueResult();
  }

  public AdditionalIpV4Route findByName(String name) {
    return (AdditionalIpV4Route)getSession()
             .createCriteria(AdditionalIpV4Route.class)
             .add(Restrictions.eq("name", name))
             .uniqueResult();
  }
  
  public List<AdditionalIpV4Route> findByCriteriaAndAdditionalIpV4RouteGroupId(
    DetachedCriteria criteria, Integer additionalIpV4RouteGroupId, boolean included
  ) {
    @SuppressWarnings("unchecked")
    List<AdditionalIpV4Route> list
      = criteria.getExecutableCriteria(getSession()).list();

    Iterator<AdditionalIpV4Route> iter = list.iterator();
    while (iter.hasNext()) {
      AdditionalIpV4Route route = iter.next();
      boolean found = false;

      for (AdditionalIpV4RouteGroup routeg: route.getAdditionalIpV4RouteGroups()) {
        if (routeg.getId().equals(additionalIpV4RouteGroupId)) {
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
