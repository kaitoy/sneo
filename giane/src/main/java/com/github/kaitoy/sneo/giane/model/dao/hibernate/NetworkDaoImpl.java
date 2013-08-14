/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import com.github.kaitoy.sneo.giane.model.Network;
import com.github.kaitoy.sneo.giane.model.dao.NetworkDao;

public class NetworkDaoImpl extends AbstractDao<Network> implements NetworkDao {

  @SuppressWarnings("unchecked")
  public List<Network> list() {
    return (List<Network>)getSession().createCriteria(Network.class).list();
  }

  public Network findByKey(Integer id) {
    return (Network)getSession().createCriteria(Network.class)
             .add(Restrictions.idEq(id))
             .uniqueResult();
  }

  public Network findByName(String name) {
    return (Network)getSession().createCriteria(Network.class)
             .add(Restrictions.eq("name", name))
             .uniqueResult();
  }

}
