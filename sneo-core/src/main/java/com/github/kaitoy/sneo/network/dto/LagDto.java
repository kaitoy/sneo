/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.network.dto;

import java.io.Serializable;
import java.util.List;

public class LagDto implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -1272808890618220921L;

  private Integer id;
  private String name;
  private Integer channelGroupNumber;
  private List<IpAddressDto> ipAddresses;
  private List<PhysicalNetworkInterfaceDto> physicalNetworkInterfaces;

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


  public Integer getChannelGroupNumber() {
    return channelGroupNumber;
  }

  public List<IpAddressDto> getIpAddresses() {
    return ipAddresses;
  }

  public void setIpAddresses(List<IpAddressDto> ipAddresses) {
    this.ipAddresses = ipAddresses;
  }

  public void setChannelGroupNumber(Integer channelGroupNumber) {
    this.channelGroupNumber = channelGroupNumber;
  }

  public List<PhysicalNetworkInterfaceDto> getPhysicalNetworkInterfaces() {
    return physicalNetworkInterfaces;
  }

  public void setPhysicalNetworkInterfaces(
    List<PhysicalNetworkInterfaceDto> physicalNetworkInterfaces
  ) {
    this.physicalNetworkInterfaces = physicalNetworkInterfaces;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) { return true; }
    if (!this.getClass().isInstance(obj)) { return false; }
    return this.id == ((LagDto)obj).getId();
  }

  @Override
  public int hashCode() {
    return id;
  }

}
