/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import com.github.kaitoy.sneo.giane.model.RealNetworkInterfaceConfiguration;
import com.github.kaitoy.sneo.giane.model.dao.RealNetworkInterfaceConfigurationDao;

public class RealNetworkInterfaceConfigurationDaoImpl
extends AbstractDao<RealNetworkInterfaceConfiguration>
implements RealNetworkInterfaceConfigurationDao {

  @SuppressWarnings("unchecked")
  public List<RealNetworkInterfaceConfiguration> list() {
    return (List<RealNetworkInterfaceConfiguration>)getSession()
             .createCriteria(RealNetworkInterfaceConfiguration.class).list();
  }

  public RealNetworkInterfaceConfiguration findByKey(Integer id) {
    return (RealNetworkInterfaceConfiguration)getSession()
             .createCriteria(RealNetworkInterfaceConfiguration.class)
             .add(Restrictions.idEq(id))
             .uniqueResult();
  }

  public RealNetworkInterfaceConfiguration findByName(String name) {
    return (RealNetworkInterfaceConfiguration)getSession()
             .createCriteria(RealNetworkInterfaceConfiguration.class)
             .add(Restrictions.eq("name", name))
             .uniqueResult();
  }

}
