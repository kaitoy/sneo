/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.network.dto;

import java.io.Serializable;
import java.util.List;

public class VlanDto implements Serializable  {

  /**
   *
   */
  private static final long serialVersionUID = 5541241260538484516L;

  private Integer id;
  private String name;
  private Integer vid;
  private List<IpAddressDto> ipAddresses;
  private List<VlanMemberDto> vlanMembers;

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

  public Integer getVid() {
    return vid;
  }

  public void setVid(Integer vid) {
    this.vid = vid;
  }

  public List<IpAddressDto> getIpAddresses() {
    return ipAddresses;
  }

  public void setIpAddresses(List<IpAddressDto> ipAddresses) {
    this.ipAddresses = ipAddresses;
  }

  public List<VlanMemberDto> getVlanMembers() {
    return vlanMembers;
  }

  public void setVlanMembers(List<VlanMemberDto> vlanMembers) {
    this.vlanMembers = vlanMembers;
  }

}
