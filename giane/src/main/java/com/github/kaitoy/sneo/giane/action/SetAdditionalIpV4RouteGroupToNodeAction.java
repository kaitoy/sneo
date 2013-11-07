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
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.github.kaitoy.sneo.giane.action.message.NodeMessage;
import com.github.kaitoy.sneo.giane.model.AdditionalIpV4RouteGroup;
import com.github.kaitoy.sneo.giane.model.Node;
import com.github.kaitoy.sneo.giane.model.Simulation;
import com.github.kaitoy.sneo.giane.model.dao.SimulationDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import com.opensymphony.xwork2.validator.annotations.ConversionErrorFieldValidator;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class SetAdditionalIpV4RouteGroupToNodeAction extends ActionSupport
implements FormMessage, NodeMessage {

  /**
   *
   */
  private static final long serialVersionUID = 433422932342221678L;

  private Node node;
  private AdditionalIpV4RouteGroup additionalIpV4RouteGroup;
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
    converter = "com.github.kaitoy.sneo.giane.typeconverter.AdditionalIpV4RouteGroupConverter"
  )
  public void setAdditionalIpV4RouteGroup(AdditionalIpV4RouteGroup additionalIpV4RouteGroup) {
    this.additionalIpV4RouteGroup = additionalIpV4RouteGroup;
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

    if (additionalIpV4RouteGroup != null) {
      config.getAdditionalIpV4RouteGroups().put(node, additionalIpV4RouteGroup);
    }
    else {
      config.getAdditionalIpV4RouteGroups().remove(node);
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
