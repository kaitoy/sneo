/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao;

import com.github.kaitoy.sneo.giane.model.Node;

public interface NodeDao extends BaseDao<Node> {

  public Node findByNameAndNetworkId(String name, Integer networkId);

}
