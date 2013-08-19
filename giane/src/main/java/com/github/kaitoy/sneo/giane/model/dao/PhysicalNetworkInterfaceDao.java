/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao;

import java.util.List;
import javax.persistence.criteria.CriteriaQuery;
import com.github.kaitoy.sneo.giane.model.PhysicalNetworkInterface;

public interface PhysicalNetworkInterfaceDao
extends BaseDao<PhysicalNetworkInterface> {

  public List<PhysicalNetworkInterface> findByCriteriaAndVlanId(
    CriteriaQuery<PhysicalNetworkInterface> criteria,
    Integer vlanId,
    boolean included
  );

  public List<PhysicalNetworkInterface> listSingles();

  public PhysicalNetworkInterface findByNameAndNodeId(
    String name, Integer nodeId
  );

}
