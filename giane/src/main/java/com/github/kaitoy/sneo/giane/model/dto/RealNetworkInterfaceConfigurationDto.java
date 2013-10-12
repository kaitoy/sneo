/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dto;

import java.io.Serializable;
import com.github.kaitoy.sneo.giane.model.RealNetworkInterfaceConfiguration;

public class RealNetworkInterfaceConfigurationDto implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 6328799197497162416L;

  private Integer id;
  private String name;
  private String deviceName;
  private String macAddress;
  private String descr;

  public RealNetworkInterfaceConfigurationDto(
    RealNetworkInterfaceConfiguration conf
  ) {
    this.id = conf.getId();
    this.name = conf.getName();
    this.deviceName = conf.getDeviceName();
    this.macAddress = conf.getMacAddress();
    this.descr = conf.getDescr().replaceAll("\n", " ");
  }

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

  public String getDescr() {
    return descr;
  }

  public void setDescr(String descr) {
    this.descr = descr;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) { return true; }
    if (!this.getClass().isInstance(obj)) { return false; }
    return this.id == ((RealNetworkInterfaceConfigurationDto)obj).getId();
  }

  @Override
  public int hashCode() {
    return id;
  }

}
