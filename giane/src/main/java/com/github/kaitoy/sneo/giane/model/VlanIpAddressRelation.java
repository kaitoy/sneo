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
@DiscriminatorValue("Vlan")
public class VlanIpAddressRelation extends IpAddressRelation {

  /**
   *
   */
  private static final long serialVersionUID = 7781935395785610224L;

  private Vlan vlan;

  @OneToOne(
    mappedBy = "ipAddressRelation",
    optional = false,
    fetch = FetchType.LAZY
  )
  public Vlan getVlan() {
    return vlan;
  }

  public void setVlan(Vlan vlan) {
    this.vlan = vlan;
  }

}
