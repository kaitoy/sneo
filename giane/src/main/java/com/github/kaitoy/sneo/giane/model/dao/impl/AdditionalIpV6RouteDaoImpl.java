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
import com.github.kaitoy.sneo.giane.model.AdditionalIpV6Route;
import com.github.kaitoy.sneo.giane.model.AdditionalIpV6RouteGroup;
import com.github.kaitoy.sneo.giane.model.dao.AdditionalIpV6RouteDao;

public class AdditionalIpV6RouteDaoImpl
  extends AbstractDao<AdditionalIpV6Route> implements AdditionalIpV6RouteDao {

  public AdditionalIpV6Route findByKey(Integer id) {
    return findSingleBy("id", id, AdditionalIpV6Route.class);
  }

  public AdditionalIpV6Route findByName(String name) {
    return findSingleBy("name", name, AdditionalIpV6Route.class);
  }

  public List<AdditionalIpV6Route> findByCriteriaAndAdditionalIpV6RouteGroupId(
    CriteriaQuery<AdditionalIpV6Route> criteria,
    Integer additionalIpV6RouteGroupId,
    boolean included
  ) {
    List<AdditionalIpV6Route> list = findByCriteria(criteria);

    Iterator<AdditionalIpV6Route> iter = list.iterator();
    while (iter.hasNext()) {
      AdditionalIpV6Route route = iter.next();
      boolean found = false;

      for (AdditionalIpV6RouteGroup routeg: route.getAdditionalIpV6RouteGroups()) {
        if (routeg.getId().equals(additionalIpV6RouteGroupId)) {
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
