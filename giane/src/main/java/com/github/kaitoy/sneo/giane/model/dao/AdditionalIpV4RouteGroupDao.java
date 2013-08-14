/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao;

import java.util.List;

import com.github.kaitoy.sneo.giane.model.AdditionalIpV4RouteGroup;

public interface AdditionalIpV4RouteGroupDao extends BaseDao<AdditionalIpV4RouteGroup> {

  public List<AdditionalIpV4RouteGroup> list();

  public AdditionalIpV4RouteGroup findByName(String name);

}
