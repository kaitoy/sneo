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
import com.github.kaitoy.sneo.giane.action.message.RealNetworkInterfaceMessage;
import com.github.kaitoy.sneo.giane.model.RealNetworkInterface;
import com.github.kaitoy.sneo.giane.model.RealNetworkInterfaceConfiguration;
import com.github.kaitoy.sneo.giane.model.Simulation;
import com.github.kaitoy.sneo.giane.model.dao.SimulationDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import com.opensymphony.xwork2.validator.annotations.ConversionErrorFieldValidator;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class SetRealNetworkInterfaceConfigurationToRealNetworkInterfaceAction
extends ActionSupport
implements FormMessage, RealNetworkInterfaceMessage, DialogMessage {

  /**
   *
   */
  private static final long serialVersionUID = 7696872724899169733L;

  private RealNetworkInterface realNetworkInterface;
  private RealNetworkInterfaceConfiguration realNetworkInterfaceConfiguration;
  private SimulationDao simulationDao;

  @ConversionErrorFieldValidator(
    key = "ConversionErrorFieldValidator.error",
    shortCircuit = true
  )
  @TypeConversion(
    converter = "com.github.kaitoy.sneo.giane.typeconverter.RealNetworkInterfaceConverter"
  )
  public void setRealNetworkInterface(
    RealNetworkInterface realNetworkInterface
  ) {
    this.realNetworkInterface = realNetworkInterface;
  }

  @ConversionErrorFieldValidator(
    key = "ConversionErrorFieldValidator.error",
    shortCircuit = true
  )
  @TypeConversion(
    converter
      = "com.github.kaitoy.sneo.giane.typeconverter.RealNetworkInterfaceConfigurationConverter"
  )
  public void setRealNetworkInterfaceConfiguration(
    RealNetworkInterfaceConfiguration realNetworkInterfaceConfiguration
  ) {
    this.realNetworkInterfaceConfiguration = realNetworkInterfaceConfiguration;
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

    if (realNetworkInterfaceConfiguration != null) {
      config.getRealNetworkInterfaceConfigurations()
        .put(realNetworkInterface, realNetworkInterfaceConfiguration);
    }
    else {
      config.getRealNetworkInterfaceConfigurations().remove(realNetworkInterface);
    }
    simulationDao.update(config);

    return "success";
  }

  @Override
  public void validate() {
    if (realNetworkInterface == null) {
      addActionError(getText("select.a.row"));
    }
  }

}
