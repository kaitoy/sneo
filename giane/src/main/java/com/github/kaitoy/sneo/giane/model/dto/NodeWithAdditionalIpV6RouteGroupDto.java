/*_##########################################################################
  _##
  _##  Copyright (C) 2012  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dto;

import java.io.Serializable;
import com.github.kaitoy.sneo.giane.model.AdditionalIpV6RouteGroup;
import com.github.kaitoy.sneo.giane.model.Node;

public class NodeWithAdditionalIpV6RouteGroupDto implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 8012038373728677142L;

  private Integer id;
  private String name;
  private String AdditionalIpV6RouteGroup;

  public NodeWithAdditionalIpV6RouteGroupDto(
    Node node, AdditionalIpV6RouteGroup additionalIpV6RouteGroup
  ) {
    this.id = node.getId();
    this.name = node.getName();
    if (additionalIpV6RouteGroup != null) {
      this.AdditionalIpV6RouteGroup = additionalIpV6RouteGroup.getName();
    }
    else {
      this.AdditionalIpV6RouteGroup = null;
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

  public String getAdditionalIpV6RouteGroup() {
    return AdditionalIpV6RouteGroup;
  }

  public void setAdditionalIpV6RouteGroup(String additionalIpV6RouteGroup) {
    this.AdditionalIpV6RouteGroup = additionalIpV6RouteGroup;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) { return true; }
    if (!this.getClass().isInstance(obj)) { return false; }
    return this.id.equals(((NodeWithAdditionalIpV6RouteGroupDto)obj).getId());
  }

  @Override
  public int hashCode() {
    return id;
  }

}
