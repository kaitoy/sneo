/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.impl;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.github.kaitoy.sneo.giane.model.Node;
import com.github.kaitoy.sneo.giane.model.dao.NodeDao;

public class NodeDaoImpl extends AbstractDao<Node> implements NodeDao {

  public Node findByKey(Integer id) {
    return findSingleBy("id", id, Node.class);
  }

  public Node findByNameAndNetworkId(String name, Integer networkId) {
    CriteriaBuilder cb = getCriteriaBuilder();
    CriteriaQuery<Node> cq = cb.createQuery(Node.class);
    Root<Node> r = cq.from(Node.class);
    cq.select(r).where(
      cb.and(
        cb.equal(r.get("name"), name),
        cb.equal(r.get("network"), networkId)
      )
    );

    try {
      return getEntityManager().createQuery(cq).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

}
