/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.impl;

import java.util.List;
import com.github.kaitoy.sneo.giane.model.Network;
import com.github.kaitoy.sneo.giane.model.dao.NetworkDao;

public class NetworkDaoImpl extends AbstractDao<Network> implements NetworkDao {

  public List<Network> list() {
    return list(Network.class);
  }

  public Network findByKey(Integer id) {
    return findSingleBy("id", id, Network.class);
  }

  public Network findByName(String name) {
    return findSingleBy("name", name, Network.class);
  }

}
