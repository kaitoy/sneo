/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.network.dto;

import java.io.Serializable;
import java.util.List;

public class NodeDto implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -5709522393868990712L;

  private Integer id;
  private String name;
  private Integer ttl;
  private SnmpAgentDto agent;
  private List<PhysicalNetworkInterfaceDto> physicalNetworkInterfaces;
  private List<LagDto> lags;
  private List<RealNetworkInterfaceDto> realNetworkInterfaces;
  private List<VlanDto> vlans;
  private List<IpV4RouteDto> ipV4Routes;

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

  public Integer getTtl() {
    return ttl;
  }

  public void setTtl(Integer ttl) {
    this.ttl = ttl;
  }

  public SnmpAgentDto getAgent() {
    return agent;
  }

  public void setAgent(SnmpAgentDto agent) {
    this.agent = agent;
  }

  public List<PhysicalNetworkInterfaceDto> getPhysicalNetworkInterfaces() {
    return physicalNetworkInterfaces;
  }

  public void setPhysicalNetworkInterfaces(
    List<PhysicalNetworkInterfaceDto> physicalNetworkInterfaces
  ) {
    this.physicalNetworkInterfaces = physicalNetworkInterfaces;
  }

  public List<LagDto> getLags() {
    return lags;
  }

  public void setLags(List<LagDto> lags) {
    this.lags = lags;
  }

  public List<RealNetworkInterfaceDto> getRealNetworkInterfaces() {
    return realNetworkInterfaces;
  }

  public void setRealNetworkInterfaces(
    List<RealNetworkInterfaceDto> realNetworkInterfaces
  ) {
    this.realNetworkInterfaces = realNetworkInterfaces;
  }

  public List<VlanDto> getVlans() {
    return vlans;
  }

  public void setVlans(List<VlanDto> vlans) {
    this.vlans = vlans;
  }

  public List<IpV4RouteDto> getIpV4Routes() {
    return ipV4Routes;
  }

  public void setIpV4Routes(List<IpV4RouteDto> ipV4Routes) {
    this.ipV4Routes = ipV4Routes;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) { return true; }
    if (!this.getClass().isInstance(obj)) { return false; }
    return this.id == ((NodeDto)obj).getId();
  }

  @Override
  public int hashCode() {
    return id;
  }

}
