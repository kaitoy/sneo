/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.action;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import com.github.kaitoy.sneo.giane.model.RealNetworkInterfaceConfiguration;
import com.github.kaitoy.sneo.giane.model.RealNetworkInterfaceConfigurationIpAddressRelation;
import com.github.kaitoy.sneo.giane.model.dao.RealNetworkInterfaceConfigurationDao;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class RealNetworkInterfaceConfigurationEditGridEntryAction
extends ActionSupport {

  /**
   *
   */
  private static final long serialVersionUID = -465085022215756189L;

  private RealNetworkInterfaceConfigurationDao realNetworkInterfaceConfigurationDao;

  private String oper;
  private Integer id;
  private String name;
  private String macAddress;
  private String deviceName;
  private String descr;

  // for DI
  public void setRealNetworkInterfaceConfigurationDao(
    RealNetworkInterfaceConfigurationDao realNetworkInterfaceConfigurationDao
  ) {
    this.realNetworkInterfaceConfigurationDao
      = realNetworkInterfaceConfigurationDao;
  }

  @Override
  public String execute() throws Exception {
    if (oper.equalsIgnoreCase("add")) {
      RealNetworkInterfaceConfiguration model
        = new RealNetworkInterfaceConfiguration();
      model.setName(name);
      model.setMacAddress(macAddress);
      model.setDeviceName(deviceName);
      model.setDescr(descr);
      model.setIpAddressRelation(
        new RealNetworkInterfaceConfigurationIpAddressRelation()
      );
      realNetworkInterfaceConfigurationDao.create(model);
    }
    else if (oper.equalsIgnoreCase("edit")) {
      RealNetworkInterfaceConfiguration model
        = realNetworkInterfaceConfigurationDao.findByKey(id);
      model.setName(name);
      model.setMacAddress(macAddress);
      model.setDeviceName(deviceName);
      model.setDescr(descr);
      realNetworkInterfaceConfigurationDao.update(model);
    }
    else if (oper.equalsIgnoreCase("del")) {
      RealNetworkInterfaceConfiguration model
        = realNetworkInterfaceConfigurationDao.findByKey(id);
      realNetworkInterfaceConfigurationDao.delete(model);
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

  public String getMacAddress() {
    return macAddress;
  }

  public void setMacAddress(String macAddress) {
    this.macAddress = macAddress;
  }

  public String getDeviceName() {
    return deviceName;
  }

  public void setDeviceName(String deviceName) {
    this.deviceName = deviceName;
  }

  public String getDescr() {
    return descr;
  }

  public void setDescr(String descr) {
    this.descr = descr;
  }

}
