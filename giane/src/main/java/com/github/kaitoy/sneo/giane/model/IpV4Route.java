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
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import com.github.kaitoy.sneo.network.dto.IpV4RouteDto;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "IP_V4_ROUTE")
public abstract class IpV4Route implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1507468931771299972L;

  private Integer id;
  private String networkDestination;
  private String netmask;
  private String gateway;
  private Integer metric;

  @Id
  @GeneratedValue(generator = "SequenceStyleGenerator")
  @GenericGenerator(
    name = "SequenceStyleGenerator",
    strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters = {
      @Parameter(name = "sequence_name", value = "IP_V4_ROUTE_SEQUENCE"),
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

  @Column(name = "NETWORK_DESTINATION", nullable = false)
  public String getNetworkDestination() {
    return networkDestination;
  }

  @RequiredStringValidator(
    key = "RequiredStringValidator.error",
    trim = true,
    shortCircuit = true // Stops checking if detects error
  )
  @RegexFieldValidator(
    key = "RegexFieldValidator.error",
    expression = "[0-9]{1,3}(\\.[0-9]{1,3}){3}",
    shortCircuit = true
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
  @RegexFieldValidator(
    key = "RegexFieldValidator.error",
    expression = "[0-9]{1,3}(\\.[0-9]{1,3}){3}",
    shortCircuit = true
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
  @RegexFieldValidator(
    key = "RegexFieldValidator.error",
    expression = "[0-9]{1,3}(\\.[0-9]{1,3}){3}",
    shortCircuit = true
  )
  public void setGateway(String gateway) {
    this.gateway = gateway;
  }

  @Column(name = "METRIC", nullable = false)
  public Integer getMetric() {
    return metric;
  }

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
    dto.setId(id);
    dto.setNetworkDestination(networkDestination);
    dto.setNetmask(netmask);
    dto.setGateway(gateway);
    dto.setMetric(metric);
    return dto;
  }

}
