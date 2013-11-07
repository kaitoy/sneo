/*_##########################################################################
  _##
  _##  Copyright (C) 2012  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dto;

import java.io.Serializable;
import com.github.kaitoy.sneo.giane.model.AdditionalIpV4RouteGroup;
import com.github.kaitoy.sneo.giane.model.Node;

public class NodeWithAdditionalIpV4RouteGroupDto implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -5103147767597714532L;

  private Integer id;
  private String name;
  private String AdditionalIpV4RouteGroup;

  public NodeWithAdditionalIpV4RouteGroupDto(
    Node node, AdditionalIpV4RouteGroup additionalIpV4RouteGroup
  ) {
    this.id = node.getId();
    this.name = node.getName();
    if (additionalIpV4RouteGroup != null) {
      this.AdditionalIpV4RouteGroup = additionalIpV4RouteGroup.getName();
    }
    else {
      this.AdditionalIpV4RouteGroup = null;
    }
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAdditionalIpV4RouteGroup() {
    return AdditionalIpV4RouteGroup;
  }

  public void setAdditionalIpV4RouteGroup(String additionalIpV4RouteGroup) {
    this.AdditionalIpV4RouteGroup = additionalIpV4RouteGroup;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) { return true; }
    if (!this.getClass().isInstance(obj)) { return false; }
    return this.id.equals(((NodeWithAdditionalIpV4RouteGroupDto)obj).getId());
  }

  @Override
  public int hashCode() {
    return id;
  }

}
