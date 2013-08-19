/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.impl;

import java.util.List;
import com.github.kaitoy.sneo.giane.model.RealNetworkInterfaceConfiguration;
import com.github.kaitoy.sneo.giane.model.dao.RealNetworkInterfaceConfigurationDao;

public class RealNetworkInterfaceConfigurationDaoImpl
extends AbstractDao<RealNetworkInterfaceConfiguration>
implements RealNetworkInterfaceConfigurationDao {

  public List<RealNetworkInterfaceConfiguration> list() {
    return list(RealNetworkInterfaceConfiguration.class);
  }

  public RealNetworkInterfaceConfiguration findByKey(Integer id) {
    return findSingleBy("id", id, RealNetworkInterfaceConfiguration.class);
  }

  public RealNetworkInterfaceConfiguration findByName(String name) {
    return findSingleBy("name", name, RealNetworkInterfaceConfiguration.class);
  }

}
