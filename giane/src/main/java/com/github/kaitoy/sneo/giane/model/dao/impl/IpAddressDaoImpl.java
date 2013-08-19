/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.impl;

import com.github.kaitoy.sneo.giane.model.IpAddress;
import com.github.kaitoy.sneo.giane.model.dao.IpAddressDao;

public class IpAddressDaoImpl
extends AbstractDao<IpAddress> implements IpAddressDao {

  public IpAddress findByKey(Integer id) {
    return findSingleBy("id", id, IpAddress.class);
  }

}
