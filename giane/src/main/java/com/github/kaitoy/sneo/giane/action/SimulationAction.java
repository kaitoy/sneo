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
import com.github.kaitoy.sneo.giane.action.message.BreadCrumbsMessage;
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.github.kaitoy.sneo.giane.action.message.SimulationMessage;
import com.github.kaitoy.sneo.giane.interceptor.GoingBackward;
import com.github.kaitoy.sneo.giane.interceptor.GoingForward;
import com.github.kaitoy.sneo.giane.model.Network;
import com.github.kaitoy.sneo.giane.model.Simulation;
import com.github.kaitoy.sneo.giane.model.dao.NetworkDao;
import com.github.kaitoy.sneo.giane.model.dao.SimulationDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class SimulationAction
extends ActionSupport
implements ModelDriven<Simulation>, ParameterAware, FormMessage, SimulationMessage, BreadCrumbsMessage {

  /**
   *
   */
  private static final long serialVersionUID = -80416172987212604L;

  private Simulation model = new Simulation();
  private Map<String, String[]> parameters;
  private SimulationDao simulationDao;
  private NetworkDao networkDao;
  private String uniqueColumn;
  private String deletingIdList;

  public Simulation getModel() { return model; }

  @VisitorFieldValidator(appendPrefix = true)
  public void setModel(Simulation model) { this.model = model; }

  public void setParameters(Map<String, String[]> parameters) {
    this.parameters = parameters;
  }

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

  public void setDeletingIdList(String deletingIdList) {
    this.deletingIdList = deletingIdList;
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
  @GoingForward
  public String execute() throws Exception {
    ValueStack stack = ActionContext.getContext().getValueStack();
    Map<String, Object> valueMap = new HashMap<String, Object>();
    valueMap.put("simulation_id", model.getId());
    valueMap.put("simulation_name", model.getName());
    stack.push(valueMap);

    return "config";
  }

  @Action(
    value = "back-to-simulation-config",
    results = { @Result(name = "config", location = "simulation-config.jsp")}
  )
  @SkipValidation
  @GoingBackward
  public String back() throws Exception {
    ValueStack stack = ActionContext.getContext().getValueStack();
    Map<String, Object> valueMap = new HashMap<String, Object>();
    valueMap.put("simulation_id", parameters.get("simulation_id")[0]);
    valueMap.put("simulation_name", parameters.get("simulation_name")[0]);
    stack.push(valueMap);

    return "config";
  }

  @Action(
    value = "simulation-grid-box",
    results = { @Result(name = "success", location = "simulation-grid.jsp")}
  )
  @SkipValidation
  public String gridBox() throws Exception {
    return "success";
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

  @Action(
    value = "simulation-delete",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  @SkipValidation
  public String delete() throws Exception {
    List<Simulation> deletingList = new ArrayList<Simulation>();
    for (String idStr: deletingIdList.split(",")) {
      deletingList.add(simulationDao.findByKey(Integer.valueOf(idStr)));
    }
    simulationDao.delete(deletingList);
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
          addFieldError("name", getText("need.to.be.unique"));
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
        addFieldError("name", getText("need.to.be.unique"));
        return;
      }
    }
  }

}
