/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao;

import java.util.List;
import javax.persistence.criteria.CriteriaQuery;
import com.github.kaitoy.sneo.giane.model.VlanMember;

public interface VlanMemberDao extends BaseDao<VlanMember> {

  public List<VlanMember> findByCriteriaAndNodeIdAndVlanId(
    CriteriaQuery<VlanMember> criteria,
    Integer nodeId,
    Integer vlanId,
    boolean included
  );

}
