/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.action;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.github.kaitoy.sneo.giane.action.message.SimulationMessage;
import com.github.kaitoy.sneo.giane.model.AdditionalIpV4Route;
import com.github.kaitoy.sneo.giane.model.AdditionalIpV4RouteGroup;
import com.github.kaitoy.sneo.giane.model.RealNetworkInterfaceConfiguration;
import com.github.kaitoy.sneo.giane.model.Simulation;
import com.github.kaitoy.sneo.giane.model.TrapTargetGroup;
import com.github.kaitoy.sneo.giane.model.dao.SimulationDao;
import com.github.kaitoy.sneo.giane.servletlistener.JmxAgentStarter;
import com.github.kaitoy.sneo.jmx.JmxAgent;
import com.github.kaitoy.sneo.network.Network;
import com.github.kaitoy.sneo.network.dto.NetworkDto;
import com.github.kaitoy.sneo.network.dto.NodeDto;
import com.github.kaitoy.sneo.network.dto.RealNetworkInterfaceDto;
import com.github.kaitoy.sneo.network.dto.SnmpAgentDto;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class StartSimulatorAction extends ActionSupport
implements SimulationMessage, FormMessage {

  /**
   *
   */
  private static final long serialVersionUID = -6369033199702364281L;

  private static final Logger logger
    = LoggerFactory.getLogger(StartSimulatorAction.class);
  private static final Map<Integer, Network> runningNetworks
    = new ConcurrentHashMap<Integer, Network>();

  private Integer simulationId;
  private SimulationDao simulationDao;
  private String dialogTitleKey;
  private String dialogTextKey;

  static Map<Integer, Network> getRunningNetworks() {
    return runningNetworks;
  }

  public void setSimulationId(Integer simulationId) {
    this.simulationId = simulationId;
  }

  // for DI
  public void setSimulationDao(
    SimulationDao simulationDao
  ) {
    this.simulationDao = simulationDao;
  }

  public String getDialogTitleKey() {
    return dialogTitleKey;
  }

  public String getDialogTextKey() {
    return dialogTextKey;
  }

  @Override
  @Action(
    results = {
      @Result(name = "success", location = "dialog.jsp"),
      @Result(name = "noNeed", location = "dialog.jsp"),
      @Result(name = "selectARow", location = "dialog.jsp"),
      @Result(name = "error", location = "dialog.jsp")
    }
  )
  @SkipValidation
  public String execute() throws Exception {
    if (simulationId == null) {
      dialogTitleKey = "simulation.selectARow.dialog.title";
      dialogTextKey = "simulation.selectARow.dialog.text";
      return "selectARow";
    }

    synchronized (runningNetworks) {
      if (runningNetworks.containsKey(simulationId)) {
        dialogTitleKey = "simulation.start.noNeed.dialog.title";
        dialogTextKey = "simulation.start.noNeed.dialog.text";
        return "noNeed";
      }

      try {
        Simulation conf
          = simulationDao.findByKey(simulationId);
        NetworkDto networkDto = conf.getNetwork().toDto();

        for (NodeDto nodeDto: networkDto.getNodes()) {
          SnmpAgentDto agentDto = nodeDto.getAgent();
          if (agentDto != null) {
            TrapTargetGroup ttg = conf.getTrapTargetGroup(agentDto.getId());
            if (ttg != null) {
              agentDto.setTrapTargetGroup(ttg.toDto());
            }
          }

          AdditionalIpV4RouteGroup routeg = conf.getAdditionalIpV4RouteGroup(nodeDto.getId());
          if (routeg != null) {
            for (AdditionalIpV4Route route: routeg.getAdditionalIpV4Routes()) {
              nodeDto.getIpV4Routes().add(route.toDto());
            }
          }

          for (RealNetworkInterfaceDto rnifDto: nodeDto.getRealNetworkInterfaces()) {
            RealNetworkInterfaceConfiguration rnifConf
              = conf.getRealNetworkInterfaceConfiguration(rnifDto.getId());
            if (rnifConf != null) {
              rnifDto.setDeviceName(rnifConf.getDeviceName());
              rnifDto.setMacAddress(rnifConf.getMacAddress());
              rnifDto.setIpAddresses(
                rnifConf.getIpAddressRelation().getIpAddressDtos()
              );
            }
          }
        }

        Network network = new Network(networkDto);
        JmxAgent jmxAgent = JmxAgentStarter.getJmxAgent();
        network.start(jmxAgent);

        runningNetworks.put(simulationId, network);

        dialogTitleKey = "simulation.start.success.dialog.title";
        dialogTextKey = "simulation.start.success.dialog.text";
        return "success";
      } catch (Exception e) {
        logger.error("An error occurred during starting simulator: ", e);
        dialogTitleKey = "simulation.start.error.dialog.title";
        dialogTextKey = "simulation.start.error.dialog.text";
        return "error";
      }
    }
  }

  @Action(
    value = "stop-simulator",
    results = {
      @Result(name = "success", location = "dialog.jsp"),
      @Result(name = "noNeed", location = "dialog.jsp"),
      @Result(name = "selectARow", location = "dialog.jsp"),
      @Result(name = "error", location = "dialog.jsp")
    }
  )
  @SkipValidation
  public String stop() throws Exception {
    if (simulationId == null) {
      dialogTitleKey = "simulation.selectARow.dialog.title";
      dialogTextKey = "simulation.selectARow.dialog.text";
      return "selectARow";
    }

    synchronized (runningNetworks) {
      if (!runningNetworks.containsKey(simulationId)) {
        dialogTitleKey = "simulation.stop.noNeed.dialog.title";
        dialogTextKey = "simulation.stop.noNeed.dialog.text";
        return "noNeed";
      }

      try {
        Network network = runningNetworks.get(simulationId);
        JmxAgent jmxAgent = JmxAgentStarter.getJmxAgent();
        network.stop(jmxAgent);

        runningNetworks.remove(simulationId);

        dialogTitleKey = "simulation.stop.success.dialog.title";
        dialogTextKey = "simulation.stop.success.dialog.text";
        return "success";
      } catch (Exception e) {
        logger.error("An error occurred during stopping simulator: ", e);
        dialogTitleKey = "simulation.stop.error.dialog.title";
        dialogTextKey = "simulation.stop.error.dialog.text";
        return "error";
      }
    }
  }

  @Action(
    value = "start-simulator-tab-content",
    results = {
      @Result(name = "tab", location = "start-simulator-tab-content.jsp")
    }
  )
  @SkipValidation
  public String startTab() throws Exception {
    return "tab";
  }

}
