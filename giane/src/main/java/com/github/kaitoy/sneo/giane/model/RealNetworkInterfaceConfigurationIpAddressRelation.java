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
@DiscriminatorValue("RealNetworkInterfaceConfiguration")
public class RealNetworkInterfaceConfigurationIpAddressRelation
extends IpAddressRelation {

  /**
   *
   */
  private static final long serialVersionUID = -7118655406385708463L;

  private RealNetworkInterfaceConfiguration realNetworkInterfaceConfiguration;

  @OneToOne(
    mappedBy = "ipAddressRelation",
    optional = false,
    fetch = FetchType.LAZY
  )
  public RealNetworkInterfaceConfiguration
  getRealNetworkInterfaceConfiguration() {
    return realNetworkInterfaceConfiguration;
  }

  public void setRealNetworkInterfaceConfiguration(
    RealNetworkInterfaceConfiguration realNetworkInterfaceConfiguration
  ) {
    this.realNetworkInterfaceConfiguration = realNetworkInterfaceConfiguration;
  }

}
