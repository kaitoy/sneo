/*_##########################################################################
  _##
  _##  Copyright (C) 2012  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.network.dto;

import java.io.Serializable;

public class IpV4RouteDto implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 835932180559937428L;

  private Integer id;
  private String networkDestination;
  private String netmask;
  private String gateway;
  private Integer metric;
  private NodeDto node;

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

  public String getNetmask() {
    return netmask;
  }

  public void setNetmask(String netmask) {
    this.netmask = netmask;
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

  public NodeDto getNode() {
    return node;
  }

  public void setNode(NodeDto node) {
    this.node = node;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) { return true; }
    if (!this.getClass().isInstance(obj)) { return false; }
    return this.id == ((IpV4RouteDto)obj).getId();
  }

  @Override
  public int hashCode() {
    return id;
  }

}
