/*_##########################################################################
  _##
  _##  Copyright (C) 2013  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.github.kaitoy.sneo.network.dto.IpV6RouteDto;
import com.opensymphony.xwork2.validator.annotations.ConversionErrorFieldValidator;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "IP_V6_ROUTE")
public abstract class IpV6Route implements Serializable, FormMessage {

  /**
   *
   */
  private static final long serialVersionUID = 1178632540373600735L;

  private Integer id;
  private String networkDestination;
  private Integer prefixLength;
  private String gateway;
  private Integer metric;

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
    key = "Inet6AddressStringValidator.error",
    type = "inet6addressstring"
  )
  public void setNetworkDestination(String networkDestination) {
    this.networkDestination = networkDestination;
  }

  @Column(name = "PREFIX_LENGTH", nullable = false)
  public Integer getPrefixLength() {
    return prefixLength;
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
    max = "128",
    shortCircuit = true
  )
  public void setPrefixLength(Integer prefixLength) {
    this.prefixLength = prefixLength;
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
    key = "Inet6AddressStringValidator.error",
    type = "inet6addressstring"
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

  public IpV6RouteDto toDto() {
    IpV6RouteDto dto = new IpV6RouteDto();
    dto.setId(id);
    dto.setNetworkDestination(networkDestination);
    dto.setPrefixLength(prefixLength);
    dto.setGateway(gateway);
    dto.setMetric(metric);
    return dto;
  }

}
