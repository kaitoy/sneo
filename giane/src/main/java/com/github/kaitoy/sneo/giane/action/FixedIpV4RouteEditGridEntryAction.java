/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.action;

import java.util.Map;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;

import com.github.kaitoy.sneo.giane.model.FixedIpV4Route;
import com.github.kaitoy.sneo.giane.model.dao.FixedIpV4RouteDao;
import com.github.kaitoy.sneo.giane.model.dao.NodeDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class FixedIpV4RouteEditGridEntryAction extends ActionSupport {

  /**
   *
   */
  private static final long serialVersionUID = 8957049474442028027L;

  private FixedIpV4RouteDao fixedIpV4RouteDao;
  private NodeDao nodeDao;

  private String oper;
  private Integer id;
  private String networkDestination;
  private String netmask;
  private String gateway;
  private Integer metric;

  // for DI
  public void setFixedIpV4RouteDao(FixedIpV4RouteDao fixedIpV4RouteDao) {
    this.fixedIpV4RouteDao = fixedIpV4RouteDao;
  }

  // for DI
  public void setNodeDao(NodeDao nodeDao) {
    this.nodeDao = nodeDao;
  }

  public String execute() throws Exception {
    if (oper.equalsIgnoreCase("add")) {
      FixedIpV4Route model = new FixedIpV4Route();
      model.setNetworkDestination(networkDestination);
      model.setNetmask(netmask);
      model.setGateway(gateway);
      model.setMetric(metric);

      Map<String, Object> params = ActionContext.getContext().getParameters();
      Integer node_id
        = Integer.valueOf(((String[])params.get("node_id"))[0]);
      model.setNode(nodeDao.findByKey(node_id));

      fixedIpV4RouteDao.create(model);
    }
    else if (oper.equalsIgnoreCase("edit")) {
      FixedIpV4Route model = fixedIpV4RouteDao.findByKey(id);
      model.setNetworkDestination(networkDestination);
      model.setNetmask(netmask);
      model.setGateway(gateway);
      model.setMetric(metric);
      fixedIpV4RouteDao.update(model);
    }
    else if (oper.equalsIgnoreCase("del")) {
      FixedIpV4Route model = fixedIpV4RouteDao.findByKey(id);
      fixedIpV4RouteDao.delete(model);
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
