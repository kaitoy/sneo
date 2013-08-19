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
import com.github.kaitoy.sneo.giane.model.AdditionalIpV4Route;
import com.github.kaitoy.sneo.giane.model.AdditionalIpV4RouteGroup;
import com.github.kaitoy.sneo.giane.model.dao.AdditionalIpV4RouteDao;

public class AdditionalIpV4RouteDaoImpl
  extends AbstractDao<AdditionalIpV4Route> implements AdditionalIpV4RouteDao {

  public AdditionalIpV4Route findByKey(Integer id) {
    return findSingleBy("id", id, AdditionalIpV4Route.class);
  }

  public AdditionalIpV4Route findByName(String name) {
    return findSingleBy("name", name, AdditionalIpV4Route.class);
  }

  public List<AdditionalIpV4Route> findByCriteriaAndAdditionalIpV4RouteGroupId(
    CriteriaQuery<AdditionalIpV4Route> criteria,
    Integer additionalIpV4RouteGroupId,
    boolean included
  ) {
    List<AdditionalIpV4Route> list = findByCriteria(criteria);

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
