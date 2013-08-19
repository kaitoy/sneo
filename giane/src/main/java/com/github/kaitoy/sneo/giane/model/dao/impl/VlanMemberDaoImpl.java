/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.impl;

import java.util.Iterator;
import java.util.List;
import javax.persistence.criteria.CriteriaQuery;
import com.github.kaitoy.sneo.giane.model.Node;
import com.github.kaitoy.sneo.giane.model.PhysicalNetworkInterface;
import com.github.kaitoy.sneo.giane.model.RealNetworkInterface;
import com.github.kaitoy.sneo.giane.model.Vlan;
import com.github.kaitoy.sneo.giane.model.VlanMember;
import com.github.kaitoy.sneo.giane.model.dao.VlanMemberDao;

public class VlanMemberDaoImpl
extends AbstractDao<VlanMember> implements VlanMemberDao {

  public VlanMember findByKey(Integer id) {
    return findSingleBy("id", id, VlanMember.class);
  }

  public List<VlanMember> findByCriteriaAndNodeIdAndVlanId(
    CriteriaQuery<VlanMember> criteria,
    Integer nodeId,
    Integer vlanId,
    boolean included
  ) {
    List<VlanMember> list = findByCriteria(criteria);

    Iterator<VlanMember> iter = list.iterator();
    while (iter.hasNext()) {
      VlanMember member = iter.next();
      if (member instanceof PhysicalNetworkInterface) {
        Node node = ((PhysicalNetworkInterface)member).getNode();
        if (!node.getId().equals(nodeId)) {
          iter.remove();
          continue;
        }
      }
      else if (member instanceof RealNetworkInterface) {
        Node node = ((RealNetworkInterface)member).getNode();
        if (!node.getId().equals(nodeId)) {
          iter.remove();
          continue;
        }
      }

      boolean found = false;
      for (Vlan vlan: member.getVlans()) {
        if (vlan.getId().equals(vlanId)) {
          found = true;
          break;
        }
      }

      if ((found && !included) || (!found && included)) {
        iter.remove();
      }
    }

    return list;
  }

}
