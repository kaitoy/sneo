/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.action;

import java.util.HashMap;
import java.util.Map;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.github.kaitoy.sneo.giane.action.message.RealNetworkInterfaceMessage;
import com.github.kaitoy.sneo.giane.model.RealNetworkInterface;
import com.github.kaitoy.sneo.giane.model.RealNetworkInterfaceConfiguration;
import com.github.kaitoy.sneo.giane.model.dao.NodeDao;
import com.github.kaitoy.sneo.giane.model.dao.RealNetworkInterfaceConfigurationDao;
import com.github.kaitoy.sneo.giane.model.dao.RealNetworkInterfaceDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class RealNetworkInterfaceAction
extends ActionSupport
implements ModelDriven<RealNetworkInterface>, FormMessage, RealNetworkInterfaceMessage {

  /**
   *
   */
  private static final long serialVersionUID = 2055134948841708871L;

  private RealNetworkInterface model = new RealNetworkInterface();
  private RealNetworkInterfaceDao realNetworkInterfaceDao;
  private NodeDao nodeDao;
  private RealNetworkInterfaceConfigurationDao realNetworkInterfaceConfigurationDao;
  private String uniqueColumn;
  private String uniqueDomain;

  public RealNetworkInterface getModel() { return model; }

  @VisitorFieldValidator(appendPrefix = false)
  public void setModel(RealNetworkInterface model) { this.model = model; }

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
  public String execute() throws Exception {
    @SuppressWarnings("unchecked")
    Map<String, Object> parameters
      = (Map<String, Object>)ActionContext.getContext().get("parameters");
    setModel(realNetworkInterfaceDao.findByKey(model.getId()));
    parameters.put("network_id", model.getNode().getNetwork().getId());
    parameters.put("network_name", model.getNode().getNetwork().getName());
    parameters.put("node_id", model.getNode().getId());
    parameters.put("node_name", model.getNode().getName());
    parameters.put("realNetworkInterface_id", model.getId());
    parameters.put("realNetworkInterface_name", model.getName());

    return "config";
  }

  @Action(
    value = "back-to-real-network-interface-config",
    results = { @Result(name = "config", location = "real-network-interface-config.jsp")}
  )
  @SkipValidation
  public String back() throws Exception {
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
    Map<String, Object> params = ActionContext.getContext().getParameters();
    Integer node_id = Integer.valueOf(((String[])params.get("node_id"))[0]);
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
        @SuppressWarnings("unchecked")
        Map<String, Object> params
          = (Map<String, Object>)ActionContext.getContext().get("parameters");
        Integer nodeId = Integer.valueOf(((String[])params.get("node_id"))[0]);

        RealNetworkInterface someone
          = realNetworkInterfaceDao
              .findByNameAndNodeId(model.getName(), nodeId);
        if (someone != null && !someone.getId().equals(model.getId())) {
          uniqueDomain = getText("realNetworkInterface.node.label");
          uniqueColumn = getText("realNetworkInterface.name.label");
          addActionError(getText("need.to.be.unique.in.domain"));
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
        addActionError(getText("need.to.be.unique.in.domain"));
        return;
      }
    }
  }

}
