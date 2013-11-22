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
@DiscriminatorValue("Lag")
public class LagIpAddressRelation extends IpAddressRelation {

  /**
   *
   */
  private static final long serialVersionUID = -263512329551812002L;

  private Lag lag;

  @OneToOne(
    mappedBy = "ipAddressRelation",
    optional = false,
    fetch = FetchType.LAZY
  )
  public Lag getLag() {
    return lag;
  }

  public void setLag(Lag lag) {
    this.lag = lag;
  }

}
