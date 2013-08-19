/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.impl;

import java.util.List;
import com.github.kaitoy.sneo.giane.model.AdditionalIpV4RouteGroup;
import com.github.kaitoy.sneo.giane.model.dao.AdditionalIpV4RouteGroupDao;

public class AdditionalIpV4RouteGroupDaoImpl
extends AbstractDao<AdditionalIpV4RouteGroup> implements AdditionalIpV4RouteGroupDao {

  public List<AdditionalIpV4RouteGroup> list() {
    return list(AdditionalIpV4RouteGroup.class);
  }

  public AdditionalIpV4RouteGroup findByKey(Integer id) {
    return findSingleBy("id", id, AdditionalIpV4RouteGroup.class);
  }

  public AdditionalIpV4RouteGroup findByName(String name) {
    return findSingleBy("name", name, AdditionalIpV4RouteGroup.class);
  }

}
