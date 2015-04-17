/*_##########################################################################
  _##
  _##  Copyright (C) 2013-2015  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.github.kaitoy.sneo.network.dto.IpV4RouteDto;
import com.opensymphony.xwork2.validator.annotations.ConversionErrorFieldValidator;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "IP_V4_ROUTE")
public abstract class IpV4Route extends AbstractModel implements FormMessage {

  /**
   *
   */
  private static final long serialVersionUID = -9203319368333071677L;

  private String networkDestination;
  private String netmask;
  private String gateway;
  private Integer metric;

  @Column(name = "NETWORK_DESTINATION", nullable = false)
  public String getNetworkDestination() {
    return networkDestination;
  }

  @RequiredStringValidator(
    key = "RequiredStringValidator.error",
    trim = true,
    shortCircuit = true // Stops checking if detects error
  )
  @CustomValidator(
    key = "Inet4AddressStringValidator.error",
    type = "inet4addressstring"
  )
  public void setNetworkDestination(String networkDestination) {
    this.networkDestination = networkDestination;
  }

  @Column(name = "NETMASK", nullable = false)
  public String getNetmask() {
    return netmask;
  }

  @RequiredStringValidator(
    key = "RequiredStringValidator.error",
    trim = true,
    shortCircuit = true // Stops checking if detects error
  )
  @CustomValidator(
    key = "Inet4AddressStringValidator.error",
    type = "inet4addressstring"
  )
  public void setNetmask(String netmask) {
    this.netmask = netmask;
  }

  @Column(name = "GATEWAY", nullable = false)
  public String getGateway() {
    return gateway;
  }

  @RequiredStringValidator(
    key = "RequiredStringValidator.error",
    trim = true,
    shortCircuit = true // Stops checking if detects error
  )
  @CustomValidator(
    key = "Inet4AddressStringValidator.error",
    type = "inet4addressstring"
  )
  public void setGateway(String gateway) {
    this.gateway = gateway;
  }

  @Column(name = "METRIC", nullable = false)
  public Integer getMetric() {
    return metric;
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
    min = "1",
    max = "9999",
    shortCircuit = true
  )
  public void setMetric(Integer metric) {
    this.metric = metric;
  }

  public IpV4RouteDto toDto() {
    IpV4RouteDto dto = new IpV4RouteDto();
    dto.setId(getId());
    dto.setNetworkDestination(networkDestination);
    dto.setNetmask(netmask);
    dto.setGateway(gateway);
    dto.setMetric(metric);
    return dto;
  }

}
