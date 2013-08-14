/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import com.github.kaitoy.sneo.network.dto.RealNetworkInterfaceDto;

@Entity
@PrimaryKeyJoinColumn(name = "VLAN_MEMBER_ID")
@Table(name = "REAL_NETWORK_INTERFACE")
public class RealNetworkInterface extends VlanMember {

  /**
   *
   */
  private static final long serialVersionUID = 6328799197497162416L;

  private Node node;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "NODE_ID", nullable = false)
  public Node getNode() {
    return node;
  }

  public void setNode(Node node) {
    this.node = node;
  }

  public RealNetworkInterfaceDto toDto() {
    RealNetworkInterfaceDto dto = new RealNetworkInterfaceDto();
    dto.setId(getId());
    dto.setName(getName());
    return dto;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) { return true; }
    if (!this.getClass().isInstance(obj)) { return false; }
    return this.getId() == ((RealNetworkInterface)obj).getId();
  }

  @Override
  public int hashCode() {
    return getId();
  }

}
