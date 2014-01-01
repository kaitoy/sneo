/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.action;

import java.util.Map;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import com.github.kaitoy.sneo.giane.action.message.DialogMessage;
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.github.kaitoy.sneo.giane.action.message.NodeMessage;
import com.github.kaitoy.sneo.giane.model.AdditionalIpV6RouteGroup;
import com.github.kaitoy.sneo.giane.model.Node;
import com.github.kaitoy.sneo.giane.model.Simulation;
import com.github.kaitoy.sneo.giane.model.dao.SimulationDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import com.opensymphony.xwork2.validator.annotations.ConversionErrorFieldValidator;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class SetAdditionalIpV6RouteGroupToNodeAction extends ActionSupport
implements FormMessage, NodeMessage, DialogMessage {

  /**
   *
   */
  private static final long serialVersionUID = 433422932342221678L;

  private Node node;
  private AdditionalIpV6RouteGroup additionalIpV6RouteGroup;
  private SimulationDao simulationDao;

  @ConversionErrorFieldValidator(
    key = "ConversionErrorFieldValidator.error",
    shortCircuit = true
  )
  @TypeConversion(converter = "com.github.kaitoy.sneo.giane.typeconverter.NodeConverter")
  public void setNode(Node node) {
    this.node = node;
  }

  @ConversionErrorFieldValidator(
    key = "ConversionErrorFieldValidator.error",
    shortCircuit = true
  )
  @TypeConversion(
    converter = "com.github.kaitoy.sneo.giane.typeconverter.AdditionalIpV6RouteGroupConverter"
  )
  public void setAdditionalIpV6RouteGroup(AdditionalIpV6RouteGroup additionalIpV6RouteGroup) {
    this.additionalIpV6RouteGroup = additionalIpV6RouteGroup;
  }

  // for DI
  public void setSimulationDao(
    SimulationDao simulationDao
  ) {
    this.simulationDao = simulationDao;
  }

  @Override
  @Action(results = { @Result(name = "success", location = "empty.jsp") })
  public String execute() throws Exception {
    Map<String, Object> params = ActionContext.getContext().getParameters();
    Integer simulationId
      = Integer.valueOf(((String[])params.get("simulation_id"))[0]);
    Simulation config
      = simulationDao.findByKey(simulationId);

    if (additionalIpV6RouteGroup != null) {
      config.getAdditionalIpV6RouteGroups().put(node, additionalIpV6RouteGroup);
    }
    else {
      config.getAdditionalIpV6RouteGroups().remove(node);
    }
    simulationDao.update(config);

    return "success";
  }

  @Override
  public void validate() {
    if (node == null) {
      addActionError(getText("select.a.row"));
    }
  }

}
