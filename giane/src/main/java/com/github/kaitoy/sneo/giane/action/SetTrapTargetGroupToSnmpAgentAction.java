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
import com.github.kaitoy.sneo.giane.action.message.SnmpAgentMessage;
import com.github.kaitoy.sneo.giane.model.Simulation;
import com.github.kaitoy.sneo.giane.model.SnmpAgent;
import com.github.kaitoy.sneo.giane.model.TrapTargetGroup;
import com.github.kaitoy.sneo.giane.model.dao.SimulationDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import com.opensymphony.xwork2.validator.annotations.ConversionErrorFieldValidator;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class SetTrapTargetGroupToSnmpAgentAction extends ActionSupport
implements FormMessage, SnmpAgentMessage, DialogMessage {

  /**
   *
   */
  private static final long serialVersionUID = 433422932342221678L;

  private SnmpAgent snmpAgent;
  private TrapTargetGroup trapTargetGroup;
  private SimulationDao simulationDao;

  @ConversionErrorFieldValidator(
    key = "ConversionErrorFieldValidator.error",
    shortCircuit = true
  )
  @TypeConversion(converter = "com.github.kaitoy.sneo.giane.typeconverter.SnmpAgentConverter")
  public void setSnmpAgent(SnmpAgent snmpAgent) {
    this.snmpAgent = snmpAgent;
  }

  @ConversionErrorFieldValidator(
    key = "ConversionErrorFieldValidator.error",
    shortCircuit = true
  )
  @TypeConversion(converter = "com.github.kaitoy.sneo.giane.typeconverter.TrapTargetGroupConverter")
  public void setTrapTargetGroup(TrapTargetGroup trapTargetGroup) {
    this.trapTargetGroup = trapTargetGroup;
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

    if (trapTargetGroup != null) {
      config.getTrapTargetGroups().put(snmpAgent, trapTargetGroup);
    }
    else {
      config.getTrapTargetGroups().remove(snmpAgent);
    }
    simulationDao.update(config);

    return "success";
  }

  @Override
  public void validate() {
    if (snmpAgent == null) {
      addActionError(getText("select.a.row"));
    }
  }

}
