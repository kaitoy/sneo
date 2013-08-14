/*_##########################################################################
  _##
  _##  Copyright (C) 2012  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dto;

import com.github.kaitoy.sneo.giane.model.PhysicalNetworkInterface;

public class PhysicalNetworkInterfaceDto extends VlanMemberDto {

  /**
   *
   */
  private static final long serialVersionUID = 5714251990296939462L;

  private String nodeName;

  public PhysicalNetworkInterfaceDto(PhysicalNetworkInterface model) {
    super(model);
    this.nodeName = model.getNode().getName();
  }

  public String getNodeName() {
    return nodeName;
  }

  public void setNodeName(String nodeName) {
    this.nodeName = nodeName;
  }

}
