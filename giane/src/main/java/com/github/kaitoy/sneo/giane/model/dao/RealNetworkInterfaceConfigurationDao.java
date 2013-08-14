/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao;

import java.util.List;

import com.github.kaitoy.sneo.giane.model.RealNetworkInterfaceConfiguration;

public interface RealNetworkInterfaceConfigurationDao
extends BaseDao<RealNetworkInterfaceConfiguration> {

  public List<RealNetworkInterfaceConfiguration> list();

  public RealNetworkInterfaceConfiguration findByName(String name);

}
