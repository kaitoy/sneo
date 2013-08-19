/*_##########################################################################
  _##
  _##  Copyright (C) 2013  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import com.opensymphony.xwork2.validator.annotations.ConversionErrorFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;

@Entity
@Table(name = "REAL_NETWORK_INTERFACE_CONFIGURATION")
public class RealNetworkInterfaceConfiguration implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -5330378911104806009L;

  private Integer id;
  private String name;
  private String deviceName;
  private String macAddress;
  private RealNetworkInterfaceConfigurationIpAddressRelation ipAddressRelation;

  @Id
  @GeneratedValue(strategy=GenerationType.AUTO, generator="giane_seq_gen")
  @SequenceGenerator(name="giane_seq_gen", sequenceName="GIANE_SEQ")
  @Column(name = "ID")
  public Integer getId() { return id; }

  public void setId(Integer id) { this.id = id; }

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

  @Column(name = "DEVICE_NAME", nullable = false)
  public String getDeviceName() {
    return deviceName;
  }

  @ConversionErrorFieldValidator(
    key = "ConversionErrorFieldValidator.error",
    shortCircuit = true
  )
  @RequiredStringValidator(
    key = "RequiredStringValidator.error",
    trim = true,
    shortCircuit = true // Stops checking if detects error
  )
  @TypeConversion(converter = "com.github.kaitoy.sneo.giane.typeconverter.DeviceNameConverter")
  public void setDeviceName(String deviceName) {
    this.deviceName = deviceName;
  }

  @RequiredStringValidator(
    key = "RequiredStringValidator.error",
    trim = true,
    shortCircuit = true // Stops checking if detects error
  )
  @Column(name = "MAC_ADDRESS", nullable = false)
  public String getMacAddress() {
    return macAddress;
  }

  @RegexFieldValidator(
    key = "RegexFieldValidator.error",
    expression = "[0-9a-fA-F]{2}(:[0-9a-fA-F]{2}){5}",
    shortCircuit = true
  )
  public void setMacAddress(String macAddress) {
    this.macAddress = macAddress;
  }

  @OneToOne(
    fetch = FetchType.LAZY,
    orphanRemoval = true,
    cascade = {
      CascadeType.REMOVE
    },
    optional = false
  )
  @JoinColumn(
    name = "IP_ADDRESS_RELATION_ID",
    nullable = false,
    unique = true
  )
  public RealNetworkInterfaceConfigurationIpAddressRelation
  getIpAddressRelation() {
    return ipAddressRelation;
  }

  public void setIpAddressRelation(
    RealNetworkInterfaceConfigurationIpAddressRelation ipAddressRelation
  ) {
    this.ipAddressRelation = ipAddressRelation;
  }

}
