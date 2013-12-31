/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.ParameterAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.github.kaitoy.sneo.giane.action.message.RealNetworkInterfaceMessage;
import com.github.kaitoy.sneo.giane.interceptor.GoingBackward;
import com.github.kaitoy.sneo.giane.interceptor.GoingForward;
import com.github.kaitoy.sneo.giane.model.RealNetworkInterface;
import com.github.kaitoy.sneo.giane.model.RealNetworkInterfaceConfiguration;
import com.github.kaitoy.sneo.giane.model.dao.NodeDao;
import com.github.kaitoy.sneo.giane.model.dao.RealNetworkInterfaceConfigurationDao;
import com.github.kaitoy.sneo.giane.model.dao.RealNetworkInterfaceDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class RealNetworkInterfaceAction
extends ActionSupport
implements ModelDriven<RealNetworkInterface>, ParameterAware, FormMessage, RealNetworkInterfaceMessage {

  /**
   *
   */
  private static final long serialVersionUID = 2055134948841708871L;

  private RealNetworkInterface model = new RealNetworkInterface();
  private Map<String, String[]> parameters;
  private RealNetworkInterfaceDao realNetworkInterfaceDao;
  private NodeDao nodeDao;
  private RealNetworkInterfaceConfigurationDao realNetworkInterfaceConfigurationDao;
  private String uniqueColumn;
  private String uniqueDomain;
  private String deletingIdList;

  public RealNetworkInterface getModel() { return model; }

  @VisitorFieldValidator(appendPrefix = true)
  public void setModel(RealNetworkInterface model) { this.model = model; }

  public void setParameters(Map<String, String[]> parameters) {
    this.parameters = parameters;
  }

  // for DI
  public void setRealNetworkInterfaceDao(
    RealNetworkInterfaceDao realNetworkInterfaceDao
  ) {
    this.realNetworkInterfaceDao = realNetworkInterfaceDao;
  }

  // for DI
  public void setNodeDao(NodeDao nodeDao) {
    this.nodeDao = nodeDao;
  }

  // for DI
  public void setRealNetworkInterfaceConfigurationDao(
    RealNetworkInterfaceConfigurationDao realNetworkInterfaceConfigurationDao
  ) {
    this.realNetworkInterfaceConfigurationDao
      = realNetworkInterfaceConfigurationDao;
  }

  public String getUniqueColumn() {
    return uniqueColumn;
  }

  public String getUniqueDomain() {
    return uniqueDomain;
  }

  public void setDeletingIdList(String deletingIdList) {
    this.deletingIdList = deletingIdList;
  }

  public Map<Integer, String> getRealNetworkInterfaceConfigurations() {
    Map<Integer, String> map = new HashMap<Integer, String>();
    for (RealNetworkInterfaceConfiguration conf: realNetworkInterfaceConfigurationDao.list()) {
      map.put(
        conf.getId(),
        conf.getName()
      );
    }
    return map;
  }

  @Override
  @GoingForward
  public String execute() throws Exception {
    ValueStack stack = ActionContext.getContext().getValueStack();
    Map<String, Object> valueMap = new HashMap<String, Object>();
    valueMap.put("realNetworkInterface_id", model.getId());
    valueMap.put("realNetworkInterface_name", model.getName());
    stack.push(valueMap);

    return "config";
  }

  @Action(
    value = "back-to-real-network-interface-config",
    results = { @Result(name = "config", location = "real-network-interface-config.jsp")}
  )
  @SkipValidation
  @GoingBackward
  public String back() throws Exception {
    ValueStack stack = ActionContext.getContext().getValueStack();
    Map<String, Object> valueMap = new HashMap<String, Object>();
    valueMap.put("realNetworkInterface_id", parameters.get("realNetworkInterface_id")[0]);
    valueMap.put("realNetworkInterface_name", parameters.get("realNetworkInterface_name")[0]);
    stack.push(valueMap);

    return "config";
  }

  @Action(
    value = "real-network-interface-tab-content",
    results = {
      @Result(
        name = "tab",
        location = "real-network-interface-tab-content.jsp"
      )
    }
  )
  @SkipValidation
  public String tab() throws Exception {
    return "tab";
  }


  @Action(
    value = "set-real-network-interface-configuration-to-real-network-interface-tab-content",
    results = {
      @Result(
        name = "tab",
        location = "set-real-network-interface-configuration-to-real-network-interface-tab-content.jsp"
      )
    }
  )
  @SkipValidation
  public String setRealNetworkInterfaceConfigurationTab() throws Exception {
    return "tab";
  }

  @Action(
    value = "real-network-interface-create",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  public String create() throws Exception {
    Integer node_id = Integer.valueOf(parameters.get("node_id")[0]);
    model.setNode(nodeDao.findByKey(node_id));
    realNetworkInterfaceDao.create(model);

    return "success";
  }

  @Action(
    value = "real-network-interface-update",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  public String update() throws Exception {
    RealNetworkInterface update
      = realNetworkInterfaceDao.findByKey(model.getId());
    update.setName(model.getName());
    realNetworkInterfaceDao.update(update);

    return "success";
  }

  @Action(
    value = "real-network-interface-delete",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  @SkipValidation
  public String delete() throws Exception {
    List<RealNetworkInterface> deletingList = new ArrayList<RealNetworkInterface>();
    for (String idStr: deletingIdList.split(",")) {
      deletingList.add(realNetworkInterfaceDao.findByKey(Integer.valueOf(idStr)));
    }
    realNetworkInterfaceDao.delete(deletingList);
    return "success";
  }

  @Override
  public void validate() {
    String contextName = ActionContext.getContext().getName();

    if (contextName.equals("real-network-interface")) {
      if (model.getId() == null) {
        addActionError(getText("select.a.row"));
        return;
      }
    }

    if (contextName.equals("real-network-interface-update")) {
      if (model.getId() == null) {
        addActionError(getText("select.a.row"));
        return;
      }

      if (model.getName() != null) {
        Integer nodeId = Integer.valueOf(parameters.get("node_id")[0]);

        RealNetworkInterface someone
          = realNetworkInterfaceDao
              .findByNameAndNodeId(model.getName(), nodeId);
        if (someone != null && !someone.getId().equals(model.getId())) {
          uniqueDomain = getText("realNetworkInterface.node.label");
          uniqueColumn = getText("realNetworkInterface.name.label");
          addFieldError("name", getText("need.to.be.unique.in.domain"));
          return;
        }
      }
    }

    if (contextName.equals("real-network-interface-create")) {
      Map<String, Object> params = ActionContext.getContext().getParameters();
      Integer nodeId = Integer.valueOf(((String[])params.get("node_id"))[0]);
      if (
           model.getName() != null
        && realNetworkInterfaceDao
             .findByNameAndNodeId(model.getName(), nodeId) != null
      ) {
        uniqueDomain = getText("realNetworkInterface.node.label");
        uniqueColumn = getText("realNetworkInterface.name.label");
        addFieldError("name", getText("need.to.be.unique.in.domain"));
        return;
      }
    }
  }

}
