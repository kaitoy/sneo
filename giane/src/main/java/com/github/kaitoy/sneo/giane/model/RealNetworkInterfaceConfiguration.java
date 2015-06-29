/*_##########################################################################
  _##
  _##  Copyright (C) 2013-2015  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import com.opensymphony.xwork2.validator.annotations.ConversionErrorFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;

@Entity
@Table(name = "REAL_NETWORK_INTERFACE_CONFIGURATION")
public class RealNetworkInterfaceConfiguration extends AbstractModel implements FormMessage {

  /**
   *
   */
  private static final long serialVersionUID = -8860287611188309529L;

  private String name;
  private String deviceName;
  private String macAddress;
  private String descr;
  private RealNetworkInterfaceConfigurationIpAddressRelation ipAddressRelation;

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


  @Column(name = "MAC_ADDRESS", nullable = false)
  public String getMacAddress() {
    return macAddress;
  }

  @RequiredStringValidator(
    key = "RequiredStringValidator.error",
    trim = true,
    shortCircuit = true // Stops checking if detects error
  )
  @RegexFieldValidator(
    key = "RegexFieldValidator.error",
    regex = "[0-9a-fA-F]{2}(:[0-9a-fA-F]{2}){5}",
    shortCircuit = true
  )
  public void setMacAddress(String macAddress) {
    this.macAddress = macAddress;
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
