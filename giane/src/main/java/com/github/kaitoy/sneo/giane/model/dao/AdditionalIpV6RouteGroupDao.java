/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao;

import java.util.List;
import com.github.kaitoy.sneo.giane.model.AdditionalIpV6RouteGroup;

public interface AdditionalIpV6RouteGroupDao extends BaseDao<AdditionalIpV6RouteGroup> {

  public List<AdditionalIpV6RouteGroup> list();

  public AdditionalIpV6RouteGroup findByName(String name);

}
