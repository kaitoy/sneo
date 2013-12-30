/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.ParameterAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import com.github.kaitoy.sneo.giane.action.message.BreadCrumbsMessage;
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.github.kaitoy.sneo.giane.action.message.RealNetworkInterfaceConfigurationMessage;
import com.github.kaitoy.sneo.giane.interceptor.GoingBackward;
import com.github.kaitoy.sneo.giane.interceptor.GoingForward;
import com.github.kaitoy.sneo.giane.model.RealNetworkInterfaceConfiguration;
import com.github.kaitoy.sneo.giane.model.RealNetworkInterfaceConfigurationIpAddressRelation;
import com.github.kaitoy.sneo.giane.model.dao.IpAddressRelationDao;
import com.github.kaitoy.sneo.giane.model.dao.RealNetworkInterfaceConfigurationDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class RealNetworkInterfaceConfigurationAction
extends ActionSupport
implements ModelDriven<RealNetworkInterfaceConfiguration>, ParameterAware, FormMessage,
  RealNetworkInterfaceConfigurationMessage, BreadCrumbsMessage {

  /**
   *
   */
  private static final long serialVersionUID = 3711045919986559841L;

  private static final List<PcapNetworkInterface> DEVICE_LIST;

  private RealNetworkInterfaceConfiguration model
    = new RealNetworkInterfaceConfiguration();
  private Map<String, String[]> parameters;
  private RealNetworkInterfaceConfigurationDao realNetworkInterfaceConfigurationDao;
  private IpAddressRelationDao ipAddressRelationDao;
  private String uniqueColumn;
  private String deletingIdList;

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

  @VisitorFieldValidator(appendPrefix = true)
  public void setModel(RealNetworkInterfaceConfiguration model) {
    this.model = model;
  }

  public void setParameters(Map<String, String[]> parameters) {
    this.parameters = parameters;
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

  public void setDeletingIdList(String deletingIdList) {
    this.deletingIdList = deletingIdList;
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

  @Override
  @GoingForward
  public String execute() throws Exception {
    ValueStack stack = ActionContext.getContext().getValueStack();
    Map<String, Object> valueMap = new HashMap<String, Object>();
    setModel(realNetworkInterfaceConfigurationDao.findByKey(model.getId()));
    valueMap.put(
      "realNetworkInterfaceConfiguration_name", model.getName()
    );
    valueMap.put(
      "ipAddressRelation_id", model.getIpAddressRelation().getId()
    );
    stack.push(valueMap);

    return "config";
  }

  @Action(
    value = "back-to-real-network-interface-configuration-config",
    results = { @Result(name = "config", location = "real-network-interface-configuration-config.jsp")}
  )
  @SkipValidation
  @GoingBackward
  public String back() throws Exception {
    ValueStack stack = ActionContext.getContext().getValueStack();
    Map<String, Object> valueMap = new HashMap<String, Object>();
    valueMap.put("realNetworkInterfaceConfiguration_name", parameters.get("realNetworkInterfaceConfiguration_name")[0]);
    valueMap.put("ipAddressRelation_id", parameters.get("ipAddressRelation_id")[0]);
    stack.push(valueMap);

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
    ipAddressRelationDao.create(relation);

    model.setIpAddressRelation(relation);
    realNetworkInterfaceConfigurationDao.create(model);

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
    update.setDescr(model.getDescr());
    realNetworkInterfaceConfigurationDao.update(update);

    return "success";
  }

  @Action(
    value = "real-network-interface-configuration-delete",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  @SkipValidation
  public String delete() throws Exception {
    List<RealNetworkInterfaceConfiguration> deletingList
      = new ArrayList<RealNetworkInterfaceConfiguration>();
    for (String idStr: deletingIdList.split(",")) {
      deletingList.add(
        realNetworkInterfaceConfigurationDao.findByKey(Integer.valueOf(idStr))
      );
    }
    realNetworkInterfaceConfigurationDao.delete(deletingList);
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

  @Override
  public void validate() {
    String contextName = ActionContext.getContext().getName();

    if (contextName.equals("real-network-interface-configuration")) {
      if (model.getId() == null) {
        addActionError(getText("select.a.row"));
        return;
      }
    }

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
