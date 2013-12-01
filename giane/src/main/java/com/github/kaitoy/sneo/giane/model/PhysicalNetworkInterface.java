/*_##########################################################################
  _##
  _##  Copyright (C) 2012  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import com.github.kaitoy.sneo.network.dto.PhysicalNetworkInterfaceDto;

@Entity
@PrimaryKeyJoinColumn(name = "VLAN_MEMBER_ID")
@Table(name = "PHYSICAL_NETWORK_INTERFACE")
public class PhysicalNetworkInterface extends VlanMember {

  private static final long serialVersionUID = 8899617859341578415L;


  private L2Connection l2Connection;
  private PhysicalNetworkInterfaceIpAddressRelation ipAddressRelation;
  private Lag lag;
  private Node node;

  @ManyToOne(optional = true, fetch = FetchType.LAZY)
  @JoinColumn(name = "L2_CONNECTION_ID", nullable = true)
  public L2Connection getL2Connection() {
    return l2Connection;
  }

  public void setL2Connection(L2Connection l2Connection) {
    this.l2Connection = l2Connection;
  }

  @OneToOne(
    fetch = FetchType.LAZY,
    orphanRemoval = true,
    cascade = {
      CascadeType.REMOVE
    },
    optional = false
  )
  @JoinColumn(
    name = "IP_ADDRESS_RELATION_ID",
    nullable = false,
    unique = true
  )
  public PhysicalNetworkInterfaceIpAddressRelation getIpAddressRelation() {
    return ipAddressRelation;
  }

  public void setIpAddressRelation(
    PhysicalNetworkInterfaceIpAddressRelation ipAddressRelation
  ) {
    this.ipAddressRelation = ipAddressRelation;
  }

  @ManyToOne(optional = true, fetch = FetchType.LAZY)
  @JoinColumn(name = "LAG_ID", nullable = true)
  public Lag getLag() {
    return lag;
  }

  public void setLag(Lag lag) {
    this.lag = lag;
  }

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "NODE_ID", nullable = false)
  public Node getNode() {
    return node;
  }

  public void setNode(Node node) {
    this.node = node;
  }

  @Override
  public PhysicalNetworkInterfaceDto toDto() {
    PhysicalNetworkInterfaceDto dto = new PhysicalNetworkInterfaceDto();
    dto.setId(getId());
    dto.setName(getName());
    dto.setTrunk(isTrunk());
    if (getLag() != null) {
      dto.setAggregatorName(lag.getName());
    }
    dto.setIpAddresses(ipAddressRelation.getIpAddressDtos());
    return dto;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) { return true; }
    if (!this.getClass().isInstance(obj)) { return false; }
    return this.getId().equals(((PhysicalNetworkInterface)obj).getId());
  }

  @Override
  public int hashCode() {
    return getId();
  }

}
