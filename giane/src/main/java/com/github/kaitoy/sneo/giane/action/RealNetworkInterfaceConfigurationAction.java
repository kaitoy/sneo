/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;

import com.github.kaitoy.sneo.giane.model.RealNetworkInterfaceConfiguration;
import com.github.kaitoy.sneo.giane.model.RealNetworkInterfaceConfigurationIpAddressRelation;
import com.github.kaitoy.sneo.giane.model.dao.IpAddressRelationDao;
import com.github.kaitoy.sneo.giane.model.dao.RealNetworkInterfaceConfigurationDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class RealNetworkInterfaceConfigurationAction
extends ActionSupport
implements ModelDriven<RealNetworkInterfaceConfiguration> {

  /**
   *
   */
  private static final long serialVersionUID = 3711045919986559841L;

  private static final List<PcapNetworkInterface> DEVICE_LIST;

  private RealNetworkInterfaceConfiguration model
    = new RealNetworkInterfaceConfiguration();
  private RealNetworkInterfaceConfigurationDao realNetworkInterfaceConfigurationDao;
  private IpAddressRelationDao ipAddressRelationDao;
  private String uniqueColumn;

  static {
    List<PcapNetworkInterface> devList;
    try {
      devList = Pcaps.findAllDevs();
    } catch (PcapNativeException e) {
      e.printStackTrace();
      devList = new ArrayList<PcapNetworkInterface>(0);
    }
    DEVICE_LIST = devList;
  }

  public RealNetworkInterfaceConfiguration getModel() { return model; }

  @VisitorFieldValidator(appendPrefix = false)
  public void setModel(RealNetworkInterfaceConfiguration model) {
    this.model = model;
  }

  // for DI
  public void setRealNetworkInterfaceConfigurationDao(
    RealNetworkInterfaceConfigurationDao realNetworkInterfaceConfigurationDao
  ) {
    this.realNetworkInterfaceConfigurationDao
      = realNetworkInterfaceConfigurationDao;
  }

  // for DI
   public void setIpAddressRelationDao(IpAddressRelationDao ipAddressRelationDao) {
     this.ipAddressRelationDao = ipAddressRelationDao;
   }

  public String getUniqueColumn() {
    return uniqueColumn;
  }

  public static List<PcapNetworkInterface> getDeviceList() {
    return DEVICE_LIST;
  }

  public Map<String, String> getDevices() {
    Map<String, String> map = new TreeMap<String, String>();
    int id = 0;
    for (PcapNetworkInterface dev: DEVICE_LIST) {
      StringBuilder sb = new StringBuilder(100);
      id++;
      sb.append(id).append(":").append(dev.getName());
      map.put(sb.toString(), sb.toString());
    }

    return map;
  }

  @SkipValidation
  public String execute() throws Exception {
    @SuppressWarnings("unchecked")
    Map<String, Object> parameters
      = (Map<String, Object>)ActionContext.getContext().get("parameters");
    if (parameters.get("realNetworkInterfaceConfiguration_name") == null) {
      setModel(realNetworkInterfaceConfigurationDao.findByKey(model.getId()));
      parameters.put(
        "realNetworkInterfaceConfiguration_name", model.getName()
      );
      parameters.put(
        "ipAddressRelation_id", model.getIpAddressRelation().getId()
      );
    }

    return "config";
  }

  @Action(
    value = "real-network-interface-configuration-tab-content",
    results = {
      @Result(
        name = "tab",
        location = "real-network-interface-configuration-tab-content.jsp"
      )
    }
  )
  @SkipValidation
  public String tab() throws Exception {
    return "tab";
  }

  @Action(
    value = "real-network-interface-configuration-create",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  public String create() throws Exception {
    RealNetworkInterfaceConfigurationIpAddressRelation relation
      = new RealNetworkInterfaceConfigurationIpAddressRelation();
    relation.setRealNetworkInterfaceConfiguration(model);
    ipAddressRelationDao.save(relation);

    model.setIpAddressRelation(relation);
    realNetworkInterfaceConfigurationDao.save(model);

    return "success";
  }

  @Action(
    value = "real-network-interface-configuration-update",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  public String update() throws Exception {
    RealNetworkInterfaceConfiguration update
      = realNetworkInterfaceConfigurationDao.findByKey(model.getId());
    update.setName(model.getName());
    update.setMacAddress(model.getMacAddress());
    update.setDeviceName(model.getDeviceName());
    realNetworkInterfaceConfigurationDao.update(update);

    return "success";
  }

  @Action(
    value = "show-device-list",
    results = { @Result(name = "success", location = "device-list.jsp") }
  )
  @SkipValidation
  public String showDeviceList() throws Exception {
    return "success";
  }

  public void validate() {
    String contextName = ActionContext.getContext().getName();

    if (contextName.equals("real-network-interface-configuration-update")) {
      if (model.getId() == null) {
        addActionError(getText("select.a.row"));
        return;
      }

      if (model.getName() != null) {
        RealNetworkInterfaceConfiguration someone
          = realNetworkInterfaceConfigurationDao.findByName(model.getName());
        if (someone != null && !someone.getId().equals(model.getId())) {
          uniqueColumn
            = getText("realNetworkInterfaceConfiguration.name.label");
          addActionError(getText("need.to.be.unique"));
          return;
        }
      }
    }

    if (contextName.equals("real-network-interface-configuration-create")) {
      if (
           model.getName() != null
        && realNetworkInterfaceConfigurationDao.findByName(model.getName())
             != null
      ) {
        uniqueColumn = getText("realNetworkInterfaceConfiguration.name.label");
        addActionError(getText("need.to.be.unique"));
        return;
      }
    }
  }

}
