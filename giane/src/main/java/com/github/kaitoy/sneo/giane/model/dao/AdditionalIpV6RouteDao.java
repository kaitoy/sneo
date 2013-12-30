/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao;

import java.util.List;
import javax.persistence.criteria.CriteriaQuery;
import com.github.kaitoy.sneo.giane.model.AdditionalIpV6Route;

public interface AdditionalIpV6RouteDao extends BaseDao<AdditionalIpV6Route> {

  public AdditionalIpV6Route findByName(String name);

  public List<AdditionalIpV6Route> findByCriteriaAndAdditionalIpV6RouteGroupId(
    CriteriaQuery<AdditionalIpV6Route> criteria,
    Integer additionalIpV6RouteGroupId,
    boolean included
  );

}
