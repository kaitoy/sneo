/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.github.kaitoy.sneo.giane.model.VlanMember;

public interface VlanMemberDao extends BaseDao<VlanMember> {

  public List<VlanMember> findByCriteriaAndNodeIdAndVlanId(
    DetachedCriteria criteria, Integer nodeId, Integer vlanId, boolean included
  );

}
