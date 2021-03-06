/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao;

import java.util.List;
import javax.persistence.criteria.CriteriaQuery;
import com.github.kaitoy.sneo.giane.model.AdditionalIpV4Route;

public interface AdditionalIpV4RouteDao extends BaseDao<AdditionalIpV4Route> {

  public AdditionalIpV4Route findByName(String name);
  
  public List<AdditionalIpV4Route> findByCriteriaAndAdditionalIpV4RouteGroupId(
    CriteriaQuery<AdditionalIpV4Route> criteria,
    Integer additionalIpV4RouteGroupId,
    boolean included
  );

}
