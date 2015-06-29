/*_##########################################################################
  _##
  _##  Copyright (C) 2013-2015 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model;

import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import com.opensymphony.xwork2.validator.annotations.ConversionErrorFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;

@Entity
@Table(name = "SIMULATION")
public class Simulation extends AbstractModel implements FormMessage {

  /**
   *
   */
  private static final long serialVersionUID = -931148670770303173L;

  private String name;
  private Network network;
  private String descr;
  private Map<SnmpAgent, TrapTargetGroup> trapTargetGroups;
  private Map<RealNetworkInterface, RealNetworkInterfaceConfiguration> realNetworkInterfaceConfigurations;
  private Map<Node, AdditionalIpV4RouteGroup> additionalIpV4RouteGroups;
  private Map<Node, AdditionalIpV6RouteGroup> additionalIpV6RouteGroups;

  @Column(name = "NAME", nullable = false, length = 200, unique = true)
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

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "NETWORK_ID", nullable = false)
  public Network getNetwork() {
    return network;
  }

  @RequiredFieldValidator(
    key = "RequiredFieldValidator.error",
    shortCircuit = true // Stops checking if detects error
  )
  @ConversionErrorFieldValidator(
    key = "ConversionErrorFieldValidator.error",
    shortCircuit = true
  )
  @TypeConversion(converter = "com.github.kaitoy.sneo.giane.typeconverter.NetworkConverter")
  public void setNetwork(Network network) {
    this.network = network;
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

  @ManyToMany(fetch = FetchType.LAZY)
  @MapKeyJoinColumn(name = "SNMP_AGENT_ID", nullable = false)
  @JoinTable(
    name = "SIMULATION__SNMP_AGENT__TRAP_TARGET_GROUP",
    joinColumns
      = @JoinColumn(name = "SIMULATION_ID", nullable = false),
    inverseJoinColumns
      = @JoinColumn(name = "TRAP_TARGET_GROUP_ID", nullable = false)
  )
  public Map<SnmpAgent, TrapTargetGroup> getTrapTargetGroups() {
    return trapTargetGroups;
  }

  public void setTrapTargetGroups(
    Map<SnmpAgent, TrapTargetGroup> trapTargetGroups
  ) {
    this.trapTargetGroups = trapTargetGroups;
  }

  @Transient
  public TrapTargetGroup getTrapTargetGroup(Integer snmpAgentId) {
    SnmpAgent key = new SnmpAgent();
    key.setId(snmpAgentId);
    return trapTargetGroups.get(key);
  }

  @ManyToMany(fetch = FetchType.LAZY)
  @MapKeyJoinColumn(name = "REAL_NETWORK_INTERFACE_ID", nullable = false)
  @JoinTable(
    name = "SIMULATION__"
             + "REAL_NETWORK_INTERFACE__"
             + "REAL_NETWORK_INTERFACE_CONFIGURATION",
    joinColumns
      = @JoinColumn(name = "SIMULATION_ID", nullable = false),
    inverseJoinColumns
      = @JoinColumn(
          name = "REAL_NETWORK_INTERFACE_CONFIGURATION_ID",
          nullable = false
        )
  )
  public Map<RealNetworkInterface, RealNetworkInterfaceConfiguration>
  getRealNetworkInterfaceConfigurations() {
    return realNetworkInterfaceConfigurations;
  }

  public void setRealNetworkInterfaceConfigurations(
    Map<RealNetworkInterface, RealNetworkInterfaceConfiguration>
      realNetworkInterfaceConfigurations
  ) {
    this.realNetworkInterfaceConfigurations
      = realNetworkInterfaceConfigurations;
  }

  @Transient
  public RealNetworkInterfaceConfiguration getRealNetworkInterfaceConfiguration(
    Integer realNetworkInterfaceId
  ) {
    RealNetworkInterface key = new RealNetworkInterface();
    key.setId(realNetworkInterfaceId);
    return realNetworkInterfaceConfigurations.get(key);
  }

  @ManyToMany(fetch = FetchType.LAZY)
  @MapKeyJoinColumn(name = "NODE_ID", nullable = false)
  @JoinTable(
    name = "SIMULATION__NODE__ADDITIONAL_IP_V4_ROUTE_GROUP",
    joinColumns
      = @JoinColumn(name = "SIMULATION_ID", nullable = false),
    inverseJoinColumns
      = @JoinColumn(name = "ADDITIONAL_IP_V4_ROUTE_GROUP_ID", nullable = false)
  )
  public Map<Node, AdditionalIpV4RouteGroup> getAdditionalIpV4RouteGroups() {
    return additionalIpV4RouteGroups;
  }

  public void setAdditionalIpV4RouteGroups(
    Map<Node, AdditionalIpV4RouteGroup> additionalIpV4RouteGroups
  ) {
    this.additionalIpV4RouteGroups = additionalIpV4RouteGroups;
  }

  @Transient
  public AdditionalIpV4RouteGroup getAdditionalIpV4RouteGroup(Integer nodeId) {
    Node key = new Node();
    key.setId(nodeId);
    return additionalIpV4RouteGroups.get(key);
  }

  @ManyToMany(fetch = FetchType.LAZY)
  @MapKeyJoinColumn(name = "NODE_ID", nullable = false)
  @JoinTable(
    name = "SIMULATION__NODE__ADDITIONAL_IP_V6_ROUTE_GROUP",
    joinColumns
      = @JoinColumn(name = "SIMULATION_ID", nullable = false),
    inverseJoinColumns
      = @JoinColumn(name = "ADDITIONAL_IP_V6_ROUTE_GROUP_ID", nullable = false)
  )
  public Map<Node, AdditionalIpV6RouteGroup> getAdditionalIpV6RouteGroups() {
    return additionalIpV6RouteGroups;
  }

  public void setAdditionalIpV6RouteGroups(
    Map<Node, AdditionalIpV6RouteGroup> additionalIpV6RouteGroups
  ) {
    this.additionalIpV6RouteGroups = additionalIpV6RouteGroups;
  }

  @Transient
  public AdditionalIpV6RouteGroup getAdditionalIpV6RouteGroup(Integer nodeId) {
    Node key = new Node();
    key.setId(nodeId);
    return additionalIpV6RouteGroups.get(key);
  }

}
