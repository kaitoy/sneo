/*_##########################################################################
  _##
  _##  Copyright (C) 2013  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Entity
@DiscriminatorValue("PhysicalNetworkInterface")
public class PhysicalNetworkInterfaceIpAddressRelation extends IpAddressRelation {

  /**
   *
   */
  private static final long serialVersionUID = 2419065313534229146L;

  private PhysicalNetworkInterface physicalNetworkInterface;

  @OneToOne(
    mappedBy = "ipAddressRelation",
    optional = false,
    fetch = FetchType.LAZY
  )
  public PhysicalNetworkInterface getPhysicalNetworkInterface() {
    return physicalNetworkInterface;
  }

  public void setPhysicalNetworkInterface(
    PhysicalNetworkInterface physicalNetworkInterface
  ) {
    this.physicalNetworkInterface = physicalNetworkInterface;
  }

}
