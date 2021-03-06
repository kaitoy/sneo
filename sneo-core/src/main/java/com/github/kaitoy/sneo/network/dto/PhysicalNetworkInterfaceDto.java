/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.network.dto;

import java.util.List;

public class PhysicalNetworkInterfaceDto implements VlanMemberDto {

  /**
   *
   */
  private static final long serialVersionUID = 5180716016297906444L;

  private Integer id;
  private String name;
  private boolean trunk;
  private String aggregatorName = null;
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

  public boolean isTrunk() {
    return trunk;
  }

  public void setTrunk(boolean trunk) {
    this.trunk = trunk;
  }

  public String getAggregatorName() {
    return aggregatorName;
  }

  public void setAggregatorName(String aggregatorName) {
    this.aggregatorName = aggregatorName;
  }

  public List<IpAddressDto> getIpAddresses() {
    return ipAddresses;
  }

  public void setIpAddresses(List<IpAddressDto> ipAddresses) {
    this.ipAddresses = ipAddresses;
  }

}
