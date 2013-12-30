/*_##########################################################################
  _##
  _##  Copyright (C) 2013  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dto;

import java.io.Serializable;
import com.github.kaitoy.sneo.giane.model.IpV6Route;

public class IpV6RouteDto implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 6349705518747593010L;

  private Integer id;
  private String networkDestination;
  private Integer prefixLength;
  private String gateway;
  private Integer metric;

  public IpV6RouteDto(IpV6Route model) {
    this.id = model.getId();
    this.networkDestination = model.getNetworkDestination();
    this.setPrefixLength(model.getPrefixLength());
    this.gateway = model.getGateway();
    this.metric = model.getMetric();
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getNetworkDestination() {
    return networkDestination;
  }

  public void setNetworkDestination(String networkDestination) {
    this.networkDestination = networkDestination;
  }

  public Integer getPrefixLength() {
    return prefixLength;
  }

  public void setPrefixLength(Integer prefixLength) {
    this.prefixLength = prefixLength;
  }

  public String getGateway() {
    return gateway;
  }

  public void setGateway(String gateway) {
    this.gateway = gateway;
  }

  public Integer getMetric() {
    return metric;
  }

  public void setMetric(Integer metric) {
    this.metric = metric;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) { return true; }
    if (!this.getClass().isInstance(obj)) { return false; }
    return this.id.equals(((IpV6RouteDto)obj).getId());
  }

  @Override
  public int hashCode() {
    return id;
  }

}
