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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.github.kaitoy.sneo.network.dto.IpV4RouteDto;
import com.github.kaitoy.sneo.network.dto.IpV6RouteDto;
import com.github.kaitoy.sneo.network.dto.LagDto;
import com.github.kaitoy.sneo.network.dto.NodeDto;
import com.github.kaitoy.sneo.network.dto.PhysicalNetworkInterfaceDto;
import com.github.kaitoy.sneo.network.dto.RealNetworkInterfaceDto;
import com.github.kaitoy.sneo.network.dto.VlanDto;
import com.opensymphony.xwork2.validator.annotations.ConversionErrorFieldValidator;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;

@Entity
@Table(name = "NODE")
public class Node extends AbstractModel implements FormMessage {

  /**
   *
   */
  private static final long serialVersionUID = -7610776798277867580L;

  private String name;
  private Integer ttl;
  private String descr;
  private SnmpAgent agent;
  private List<PhysicalNetworkInterface> physicalNetworkInterfaces;
  private List<Lag> lags;
  private List<RealNetworkInterface> realNetworkInterfaces;
  private List<Vlan> vlans;
  private List<FixedIpV4Route> ipV4Routes;
  private List<FixedIpV6Route> ipV6Routes;
  private Network network;

  @Column(name = "NAME", nullable = false, length = 200)
  public String getName() {
    return name;
  }

  @RequiredStringValidator(
    key = "RequiredStringValidator.error",
    trim = true,
    shortCircuit = true // Stops checking if detects error
  )
  @StringLengthFieldValidator(
    key = "StringLengthFieldValidator.error.max",
    trim = true,
    maxLength = "200",
    shortCircuit = true
  )
  @RegexFieldValidator(
    key = "RegexFieldValidator.error.objectName",
    // this field's value is/will be used for an MBean object name, and may be used in a command line.
    regex = "[^,=:\"*?]+",
    shortCircuit = true
  )
  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "TTL", nullable = false)
  public Integer getTtl() {
    return ttl;
  }

  @ConversionErrorFieldValidator(
    key = "ConversionErrorFieldValidator.error",
    shortCircuit = true
  )
  @RequiredFieldValidator(
    key = "RequiredFieldValidator.error",
    shortCircuit = true
  )
  @IntRangeFieldValidator(
    key = "IntRangeFieldValidator.error.min.max",
    min = "0",
    max = "255",
    shortCircuit = true
  )
  public void setTtl(Integer ttl) {
    this.ttl = ttl;
  }

  @Column(name = "DESCR", nullable = true, length = 5000, unique = false)
  public String getDescr() {
    return descr;
  }

  @StringLengthFieldValidator(
    key = "StringLengthFieldValidator.error.max",
    trim = true,
    maxLength = "5000",
    shortCircuit = true // Stops checking if detects error
  )
  public void setDescr(String descr) {
    this.descr = descr;
  }

  @OneToOne(
    mappedBy = "node",
    fetch = FetchType.LAZY,
    orphanRemoval = true,
    cascade = {
      CascadeType.REMOVE
    }
  )
  public SnmpAgent getAgent() {
    return agent;
  }

  public void setAgent(SnmpAgent agent) {
    this.agent = agent;
  }

  @OneToMany(
    mappedBy = "node",
    fetch = FetchType.LAZY,
    orphanRemoval = true,
    cascade = {
      CascadeType.REMOVE
    }
  )
  public List<PhysicalNetworkInterface> getPhysicalNetworkInterfaces() {
    return physicalNetworkInterfaces;
  }

  public void setPhysicalNetworkInterfaces(
    List<PhysicalNetworkInterface> physicalNetworkInterfaces
  ) {
    this.physicalNetworkInterfaces = physicalNetworkInterfaces;
  }

  @OneToMany(
    mappedBy = "node",
    fetch = FetchType.LAZY,
    orphanRemoval = true,
    cascade = {
      CascadeType.REMOVE
    }
  )
  public List<Lag> getLags() {
    return lags;
  }

  public void setLags(List<Lag> lags) {
    this.lags = lags;
  }

  @OneToMany(
    mappedBy = "node",
    fetch = FetchType.LAZY,
    orphanRemoval = true,
    cascade = {
      CascadeType.REMOVE
    }
  )
  public List<RealNetworkInterface> getRealNetworkInterfaces() {
    return realNetworkInterfaces;
  }

  public void setRealNetworkInterfaces(
    List<RealNetworkInterface> realNetworkInterfaces
  ) {
    this.realNetworkInterfaces = realNetworkInterfaces;
  }

  @OneToMany(
    mappedBy = "node",
    fetch = FetchType.LAZY,
    orphanRemoval = true,
    cascade = {
      CascadeType.REMOVE
    }
  )
  public List<Vlan> getVlans() {
    return vlans;
  }

  public void setVlans(List<Vlan> vlans) {
    this.vlans = vlans;
  }

  @OneToMany(
    mappedBy = "node",
    fetch = FetchType.LAZY,
    orphanRemoval = true,
    cascade = {
      CascadeType.REMOVE
    }
  )
  public List<FixedIpV4Route> getIpV4Routes() {
    return ipV4Routes;
  }

  public void setIpV4Routes(List<FixedIpV4Route> ipV4Routes) {
    this.ipV4Routes = ipV4Routes;
  }

  @OneToMany(
    mappedBy = "node",
    fetch = FetchType.LAZY,
    orphanRemoval = true,
    cascade = {
      CascadeType.REMOVE
    }
  )
  public List<FixedIpV6Route> getIpV6Routes() {
    return ipV6Routes;
  }

  public void setIpV6Routes(List<FixedIpV6Route> ipV6Routes) {
    this.ipV6Routes = ipV6Routes;
  }

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "NETWORK_ID", nullable = false)
  public Network getNetwork() {
    return network;
  }

  public void setNetwork(Network network) {
    this.network = network;
  }

  public NodeDto toDto() {
    List<PhysicalNetworkInterfaceDto> physicalNetworkInterfaceDtos
      = new ArrayList<PhysicalNetworkInterfaceDto>();
    for (
      PhysicalNetworkInterface physicalNetworkInterface:
        physicalNetworkInterfaces
    ) {
      physicalNetworkInterfaceDtos.add(physicalNetworkInterface.toDto());
    }

    List<LagDto> lagDtos = new ArrayList<LagDto>();
    for (Lag lag: lags) {
      lagDtos.add(lag.toDto());
    }

    List<RealNetworkInterfaceDto> realNetworkInterfaceDtos
      = new ArrayList<RealNetworkInterfaceDto>();
    for (
      RealNetworkInterface realNetworkInterface:
        realNetworkInterfaces
    ) {
      realNetworkInterfaceDtos.add(realNetworkInterface.toDto());
    }

    List<VlanDto> vlanDtos = new ArrayList<VlanDto>();
    for (Vlan vlan: vlans) {
      vlanDtos.add(vlan.toDto());
    }

    List<IpV4RouteDto> ipV4RouteDtos = new ArrayList<IpV4RouteDto>();
    for (IpV4Route ipV4Route: ipV4Routes) {
      ipV4RouteDtos.add(ipV4Route.toDto());
    }

    List<IpV6RouteDto> ipV6RouteDtos = new ArrayList<IpV6RouteDto>();
    for (IpV6Route ipV6Route: ipV6Routes) {
      ipV6RouteDtos.add(ipV6Route.toDto());
    }

    NodeDto dto = new NodeDto();
    dto.setId(getId());
    dto.setName(name);
    dto.setTtl(ttl);
    if (agent != null) {
      dto.setAgent(agent.toDto());
    }
    dto.setPhysicalNetworkInterfaces(physicalNetworkInterfaceDtos);
    dto.setLags(lagDtos);
    dto.setRealNetworkInterfaces(realNetworkInterfaceDtos);
    dto.setVlans(vlanDtos);
    dto.setIpV4Routes(ipV4RouteDtos);
    dto.setIpV6Routes(ipV6RouteDtos);
    return dto;
  }

}
