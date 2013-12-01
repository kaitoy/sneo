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
import com.github.kaitoy.sneo.giane.action.message.BreadCrumbsMessage;
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.github.kaitoy.sneo.giane.action.message.SimulationMessage;
import com.github.kaitoy.sneo.giane.model.Network;
import com.github.kaitoy.sneo.giane.model.Simulation;
import com.github.kaitoy.sneo.giane.model.dao.NetworkDao;
import com.github.kaitoy.sneo.giane.model.dao.SimulationDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class SimulationAction
extends ActionSupport
implements ModelDriven<Simulation>, FormMessage, SimulationMessage, BreadCrumbsMessage {

  /**
   *
   */
  private static final long serialVersionUID = -80416172987212604L;

  private Simulation model = new Simulation();
  private SimulationDao simulationDao;
  private NetworkDao networkDao;
  private String uniqueColumn;

  public Simulation getModel() { return model; }

  @VisitorFieldValidator(appendPrefix = false)
  public void setModel(Simulation model) { this.model = model; }

  // for DI
  public void setSimulationDao(
    SimulationDao simulationDao
  ) {
    this.simulationDao = simulationDao;
  }

  // for DI
  public void setNetworkDao(NetworkDao networkDao) {
    this.networkDao = networkDao;
  }

  public String getUniqueColumn() {
    return uniqueColumn;
  }

  public Map<Integer, String> getNetworks() {
    Map<Integer, String> map = new HashMap<Integer, String>();
    for (Network network: networkDao.list()) {
      map.put(
        network.getId(),
        network.getName()
      );
    }
    return map;
  }

  @Override
  public String execute() throws Exception {
    @SuppressWarnings("unchecked")
    Map<String, Object> parameters
      = (Map<String, Object>)ActionContext.getContext().get("parameters");
    parameters.put("simulation_id", model.getId());
    parameters.put("simulation_name", model.getName());
    parameters.put("network_id", model.getNetwork().getId());

    return "config";
  }

  @Action(
    value = "back-to-simulation-config",
    results = { @Result(name = "config", location = "simulation-config.jsp")}
  )
  @SkipValidation
  public String back() throws Exception {
    return "config";
  }

  @Action(
    value = "simulation-tab-content",
    results = { @Result(name = "tab", location = "simulation-tab-content.jsp")}
  )
  @SkipValidation
  public String tab() throws Exception {
    return "tab";
  }

  @Action(
    value = "simulation-create",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  public String create() throws Exception {
    simulationDao.create(model);
    return "success";
  }

  @Action(
    value = "simulation-update",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  public String update() throws Exception {
    Simulation update
      = simulationDao.findByKey(model.getId());
    update.setName(model.getName());
    update.setNetwork(model.getNetwork());
    update.setDescr(model.getDescr());
    simulationDao.update(update);

    return "success";
  }

  @Override
  public void validate() {
    String contextName = ActionContext.getContext().getName();

    if (contextName.equals("simulation")) {
      if (model.getId() == null) {
        addActionError(getText("select.a.row"));
        return;
      }
    }

    if (contextName.equals("simulation-update")) {
      if (model.getId() == null) {
        addActionError(getText("select.a.row"));
        return;
      }

      if (model.getName() != null) {
        Simulation someone
          = simulationDao.findByName(model.getName());
        if (someone != null && !someone.getId().equals(model.getId())) {
          uniqueColumn = getText("simulation.name.label");
          addActionError(getText("need.to.be.unique"));
          return;
        }
      }
    }

    if (contextName.equals("simulation-create")) {
      if (
           model.getName() != null
        && simulationDao.findByName(model.getName()) != null
      ) {
        uniqueColumn = getText("simulation.name.label");
        addActionError(getText("need.to.be.unique"));
        return;
      }
    }
  }

}
