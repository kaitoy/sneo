/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.hibernate;

import org.hibernate.criterion.Restrictions;

import com.github.kaitoy.sneo.giane.model.Node;
import com.github.kaitoy.sneo.giane.model.dao.NodeDao;

public class NodeDaoImpl extends AbstractDao<Node> implements NodeDao {

  public Node findByKey(Integer id) {
    return (Node)getSession().createCriteria(Node.class)
             .add(Restrictions.idEq(id))
             .uniqueResult();
  }

  public Node findByNameAndNetworkId(String name, Integer networkId) {
    return (Node)getSession().createCriteria(Node.class)
             .add(Restrictions.eq("network.id", networkId))
             .add(Restrictions.eq("name", name))
             .uniqueResult();
  }

}
