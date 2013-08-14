package com.github.kaitoy.sneo.giane.action;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;

import com.github.kaitoy.sneo.giane.model.Network;
import com.github.kaitoy.sneo.giane.model.Simulation;
import com.github.kaitoy.sneo.giane.model.dao.SimulationDao;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class SimulationEditGridEntryAction extends ActionSupport {

  /**
   *
   */
  private static final long serialVersionUID = -2712705719319444050L;

  private SimulationDao simulationDao;

  private String oper;
  private Integer id;
  private String name;
  private Network network;

  // for DI
  public void setSimulationDao(SimulationDao simulationDao) {
    this.simulationDao = simulationDao;
  }

  public String execute() throws Exception {
    if (oper.equalsIgnoreCase("add")) {
      Simulation model = new Simulation();
      model.setName(name);
      model.setNetwork(network);
      simulationDao.save(model);
    }
    else if (oper.equalsIgnoreCase("edit")) {
      Simulation model = simulationDao.findByKey(id);
      model.setName(name);
      model.setNetwork(network);
      simulationDao.update(model);
    }
    else if (oper.equalsIgnoreCase("del")) {
      Simulation model = simulationDao.findByKey(id);
      simulationDao.delete(model);
    }

    return NONE;
  }

  public String getOper() {
    return oper;
  }

  public void setOper(String oper) {
    this.oper = oper;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Network getNetwork() {
    return network;
  }

  public void setNetwork(Network network) {
    this.network = network;
  }

}
