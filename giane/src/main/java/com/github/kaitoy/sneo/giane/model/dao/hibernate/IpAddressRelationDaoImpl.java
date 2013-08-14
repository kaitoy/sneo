/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.hibernate;

import org.hibernate.criterion.Restrictions;

import com.github.kaitoy.sneo.giane.model.IpAddressRelation;
import com.github.kaitoy.sneo.giane.model.dao.IpAddressRelationDao;

public class IpAddressRelationDaoImpl
extends AbstractDao<IpAddressRelation> implements IpAddressRelationDao {

  public IpAddressRelation findByKey(Integer id) {
    return (IpAddressRelation)getSession()
             .createCriteria(IpAddressRelation.class)
             .add(Restrictions.idEq(id))
             .uniqueResult();
  }

}
