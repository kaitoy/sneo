/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.github.kaitoy.sneo.network.dto.IpV4RouteDto;
import com.github.kaitoy.sneo.network.dto.NodeDto;
import com.github.kaitoy.sneo.network.dto.PhysicalNetworkInterfaceDto;
import com.github.kaitoy.sneo.network.dto.RealNetworkInterfaceDto;
import com.github.kaitoy.sneo.network.dto.VlanDto;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;

@Entity
@Table(name = "NODE")
public class Node implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 2255374375541080638L;

  private Integer id;
  private String name;
  private Integer ttl;
  private String descr;
  private SnmpAgent agent;
  private List<PhysicalNetworkInterface> physicalNetworkInterfaces;
  private List<RealNetworkInterface> realNetworkInterfaces;
  private List<Vlan> vlans;
  private List<FixedIpV4Route> ipV4Routes;
  private Network network;

  @Id
  @GeneratedValue(strategy=GenerationType.AUTO, generator="giane_seq_gen")
  @SequenceGenerator(name="giane_seq_gen", sequenceName="GIANE_SEQ")
  @Column(name = "ID")
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @Column(name = "NAME", nullable = false, length = 50)
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
    maxLength = "50",
    shortCircuit = true
  )
  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "TTL", nullable = false)
  public Integer getTtl() {
    return ttl;
  }

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

  @Column(name = "DESCR", nullable = true, length = 2000, unique = false)
  public String getDescr() {
    return descr;
  }

  @StringLengthFieldValidator(
    key = "StringLengthFieldValidator.error.max",
    trim = true,
    maxLength = "2000",
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

    NodeDto dto = new NodeDto();
    dto.setId(id);
    dto.setName(name);
    dto.setTtl(ttl);
    dto.setAgent(agent.toDto());
    dto.setPhysicalNetworkInterfaces(physicalNetworkInterfaceDtos);
    dto.setRealNetworkInterfaces(realNetworkInterfaceDtos);
    dto.setVlans(vlanDtos);
    dto.setIpV4Routes(ipV4RouteDtos);
    return dto;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) { return true; }
    if (!this.getClass().isInstance(obj)) { return false; }
    return this.getId().equals(((Node)obj).getId());
  }

  @Override
  public int hashCode() {
    return getId();
  }

}
