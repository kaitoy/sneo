/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.network.dto;

import java.util.List;

public class RealNetworkInterfaceDto implements VlanMemberDto {

  /**
   *
   */
  private static final long serialVersionUID = -435017867195144553L;

  private Integer id;
  private String name;
  private String deviceName;
  private String macAddress;
  private List<IpAddressDto> ipAddresses;

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

  public String getDeviceName() {
    return deviceName;
  }

  public void setDeviceName(String deviceName) {
    this.deviceName = deviceName;
  }

  public String getMacAddress() {
    return macAddress;
  }

  public void setMacAddress(String macAddress) {
    this.macAddress = macAddress;
  }

  public List<IpAddressDto> getIpAddresses() {
    return ipAddresses;
  }

  public void setIpAddresses(List<IpAddressDto> ipAddresses) {
    this.ipAddresses = ipAddresses;
  }

}
