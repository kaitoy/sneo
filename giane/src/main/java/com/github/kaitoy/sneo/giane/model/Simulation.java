/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import com.opensymphony.xwork2.validator.annotations.ConversionErrorFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;

@Entity
@Table(name = "SIMULATION")
public class Simulation implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -5588681694163320753L;

  private Integer id;
  private String name;
  private Network network;
  private Map<SnmpAgent, TrapTargetGroup> trapTargetGroups;
  private Map<RealNetworkInterface, RealNetworkInterfaceConfiguration> realNetworkInterfaceConfigurations;
  private Map<Node, AdditionalIpV4RouteGroup> additionalIpV4RouteGroups;

  @Id
  @GeneratedValue(generator = "SequenceStyleGenerator")
  @GenericGenerator(
    name = "SequenceStyleGenerator",
    strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters = {
      @Parameter(name = "sequence_name", value = "SIMULATION_SEQUENCE"),
      @Parameter(name = "initial_value", value = "1"),
      @Parameter(name = "increment_size", value = "1")
    }
  )
  @Column(name = "ID")
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @Column(name = "NAME", nullable = false, length = 50, unique = true)
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

}
