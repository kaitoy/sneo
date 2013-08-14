/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.action;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;

import com.github.kaitoy.sneo.giane.model.AdditionalIpV4Route;
import com.github.kaitoy.sneo.giane.model.dao.AdditionalIpV4RouteDao;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class AdditionalIpV4RouteEditGridEntryAction extends ActionSupport {

  /**
   *
   */
  private static final long serialVersionUID = 8957049474442028027L;

  private AdditionalIpV4RouteDao additionalIpV4RouteDao;

  private String oper;
  private Integer id;
  private String name;
  private String networkDestination;
  private String netmask;
  private String gateway;
  private Integer metric;

  // for DI
  public void setAdditionalIpV4RouteDao(
    AdditionalIpV4RouteDao additionalIpV4RouteDao
  ) {
    this.additionalIpV4RouteDao = additionalIpV4RouteDao;
  }

  public String execute() throws Exception {
    if (oper.equalsIgnoreCase("add")) {
      AdditionalIpV4Route model = new AdditionalIpV4Route();
      model.setName(name);
      model.setNetworkDestination(networkDestination);
      model.setNetmask(netmask);
      model.setGateway(gateway);
      model.setMetric(metric);
      additionalIpV4RouteDao.save(model);
    }
    else if (oper.equalsIgnoreCase("edit")) {
      AdditionalIpV4Route model = additionalIpV4RouteDao.findByKey(id);
      model.setName(name);
      model.setNetworkDestination(networkDestination);
      model.setNetmask(netmask);
      model.setGateway(gateway);
      model.setMetric(metric);
      additionalIpV4RouteDao.update(model);
    }
    else if (oper.equalsIgnoreCase("del")) {
      AdditionalIpV4Route model = additionalIpV4RouteDao.findByKey(id);
      additionalIpV4RouteDao.delete(model);
    }

    return NONE;
  }

  public String getOper() {
    return oper;
  }

  public void setOper(String oper) {
    this.oper = oper;
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

}
