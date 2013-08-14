/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.hibernate;

import org.hibernate.criterion.Restrictions;

import com.github.kaitoy.sneo.giane.model.IpAddress;
import com.github.kaitoy.sneo.giane.model.dao.IpAddressDao;

public class IpAddressDaoImpl
extends AbstractDao<IpAddress> implements IpAddressDao {

  public IpAddress findByKey(Integer id) {
    return (IpAddress)getSession().createCriteria(IpAddress.class)
             .add(Restrictions.idEq(id))
             .uniqueResult();
  }

}
