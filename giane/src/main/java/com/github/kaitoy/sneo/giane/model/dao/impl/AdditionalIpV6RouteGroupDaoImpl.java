/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.impl;

import java.util.List;
import com.github.kaitoy.sneo.giane.model.AdditionalIpV6RouteGroup;
import com.github.kaitoy.sneo.giane.model.dao.AdditionalIpV6RouteGroupDao;

public class AdditionalIpV6RouteGroupDaoImpl
extends AbstractDao<AdditionalIpV6RouteGroup> implements AdditionalIpV6RouteGroupDao {

  public List<AdditionalIpV6RouteGroup> list() {
    return list(AdditionalIpV6RouteGroup.class);
  }

  public AdditionalIpV6RouteGroup findByKey(Integer id) {
    return findSingleBy("id", id, AdditionalIpV6RouteGroup.class);
  }

  public AdditionalIpV6RouteGroup findByName(String name) {
    return findSingleBy("name", name, AdditionalIpV6RouteGroup.class);
  }

}
