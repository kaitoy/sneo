/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2015  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.github.kaitoy.sneo.network.dto.IpAddressDto;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
  name = "IP_ADDRESS_HOST",
  discriminatorType = DiscriminatorType.STRING,
  length = 50
)
@Table(name = "IP_ADDRESS_RELATION")
public abstract class IpAddressRelation extends AbstractModel implements FormMessage {

  /**
   *
   */
  private static final long serialVersionUID = 3417490643140866725L;

  private List<IpAddress> ipAddresses;

  @OneToMany(
    mappedBy = "ipAddressRelation",
    fetch = FetchType.LAZY,
    orphanRemoval = true,
    cascade = {
      CascadeType.REMOVE
    }
  )
  public List<IpAddress> getIpAddresses() { return ipAddresses; }

  public void setIpAddresses(List<IpAddress> ipAddresses) {
    this.ipAddresses = ipAddresses;
  }

  @Transient
  public List<IpAddressDto> getIpAddressDtos() {
    List<IpAddressDto> ipAddressDtos = new ArrayList<IpAddressDto>();
    for (IpAddress ipAddress: ipAddresses) {
      ipAddressDtos.add(ipAddress.toDto());
    }
    return ipAddressDtos;
  }

}
