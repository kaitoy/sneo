/*_##########################################################################
  _##
  _##  Copyright (C) 2012  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dto;

import java.io.Serializable;
import com.github.kaitoy.sneo.giane.model.SnmpAgent;
import com.github.kaitoy.sneo.giane.model.TrapTargetGroup;

public class SnmpAgentWithTrapTargetGroupDto implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -5103147767597714532L;

  private Integer id;
  private String address;
  private String hostNode;
  private String trapTargetGroup;

  public SnmpAgentWithTrapTargetGroupDto(
    SnmpAgent model, TrapTargetGroup trapTargetGroup
  ) {
    this.id = model.getId();
    this.address = model.getAddress();
    this.hostNode = model.getNode().getName();
    if (trapTargetGroup != null) {
      this.trapTargetGroup = trapTargetGroup.getName();
    }
    else {
      this.trapTargetGroup = null;
    }
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }


  public String getHostNode() {
    return hostNode;
  }

  public void setHostNode(String hostNode) {
    this.hostNode = hostNode;
  }

  public String getTrapTargetGroup() {
    return trapTargetGroup;
  }

  public void setTrapTargetGroup(String trapTargetGroup) {
    this.trapTargetGroup = trapTargetGroup;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) { return true; }
    if (!this.getClass().isInstance(obj)) { return false; }
    return this.id.equals(((SnmpAgentWithTrapTargetGroupDto)obj).getId());
  }

  @Override
  public int hashCode() {
    return id;
  }

}
