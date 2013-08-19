/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.impl;

import com.github.kaitoy.sneo.giane.model.IpAddressRelation;
import com.github.kaitoy.sneo.giane.model.dao.IpAddressRelationDao;

public class IpAddressRelationDaoImpl
extends AbstractDao<IpAddressRelation> implements IpAddressRelationDao {

  public IpAddressRelation findByKey(Integer id) {
    return findSingleBy("id", id, IpAddressRelation.class);
  }

}
