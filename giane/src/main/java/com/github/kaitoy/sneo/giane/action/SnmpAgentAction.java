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
import org.apache.struts2.interceptor.ParameterAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.github.kaitoy.sneo.giane.action.message.SnmpAgentMessage;
import com.github.kaitoy.sneo.giane.interceptor.GoingBackward;
import com.github.kaitoy.sneo.giane.interceptor.GoingForward;
import com.github.kaitoy.sneo.giane.model.FileMibFormat;
import com.github.kaitoy.sneo.giane.model.SnmpAgent;
import com.github.kaitoy.sneo.giane.model.TrapTargetGroup;
import com.github.kaitoy.sneo.giane.model.dao.NodeDao;
import com.github.kaitoy.sneo.giane.model.dao.SnmpAgentDao;
import com.github.kaitoy.sneo.giane.model.dao.TrapTargetGroupDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class SnmpAgentAction extends ActionSupport
implements ModelDriven<SnmpAgent>, ParameterAware, FormMessage, SnmpAgentMessage {

  /**
   *
   */
  private static final long serialVersionUID = 3139857666582814492L;

  private SnmpAgent model = new SnmpAgent();
  private Map<String, String[]> parameters;
  private SnmpAgentDao snmpAgentDao;
  private NodeDao nodeDao;
  private TrapTargetGroupDao trapTargetGroupDao;

  public SnmpAgent getModel() { return model; }

  @VisitorFieldValidator(appendPrefix = false)
  public void setModel(SnmpAgent model) { this.model = model; }

  public void setParameters(Map<String, String[]> parameters) {
    this.parameters = parameters;
  }

  // for DI
  public void setSnmpAgentDao(SnmpAgentDao snmpAgentDao) {
    this.snmpAgentDao = snmpAgentDao;
  }

  // for DI
  public void setNodeDao(NodeDao nodeDao) {
    this.nodeDao = nodeDao;
  }

  // for DI
  public void setTrapTargetGroupDao(TrapTargetGroupDao trapTargetGroupDao) {
    this.trapTargetGroupDao = trapTargetGroupDao;
  }

  public static FileMibFormat[] getFormats() {
    return FileMibFormat.values();
  }

  public Map<Integer, String> getTrapTargetGroups() {
    Map<Integer, String> map = new HashMap<Integer, String>();
    for (TrapTargetGroup ttg: trapTargetGroupDao.list()) {
      map.put(
        ttg.getId(),
        ttg.getName()
      );
    }
    return map;
  }

  @Override
  @GoingForward
  public String execute() throws Exception {
    ValueStack stack = ActionContext.getContext().getValueStack();
    Map<String, Object> valueMap = new HashMap<String, Object>();
    valueMap.put("snmpAgent_id", model.getId());
    valueMap.put("snmpAgent_address", model.getAddress());
    stack.push(valueMap);

    return "config";
  }

  @Action(
    value = "back-to-snmp-agent-config",
    results = { @Result(name = "config", location = "snmp-agent-config.jsp")}
  )
  @SkipValidation
  @GoingBackward
  public String back() throws Exception {
    ValueStack stack = ActionContext.getContext().getValueStack();
    Map<String, Object> valueMap = new HashMap<String, Object>();
    valueMap.put("snmpAgent_id", parameters.get("snmpAgent_id")[0]);
    valueMap.put("snmpAgent_address", parameters.get("snmpAgent_address")[0]);
    stack.push(valueMap);

    return "config";
  }

  @Action(
    value = "snmp-agent-tab-content",
    results = { @Result(name = "tab", location = "snmp-agent-tab-content.jsp")}
  )
  @SkipValidation
  public String tab() throws Exception {
    return "tab";
  }

  @Action(
    value = "set-trap-target-group-to-snmp-agent-tab-content",
    results = { @Result(name = "tab", location = "set-trap-target-group-to-snmp-agent-tab-content.jsp")}
  )
  @SkipValidation
  public String setTrapTargetGroupTab() throws Exception {
    return "tab";
  }

  @Action(
    value = "snmp-agent-create",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  public String create() throws Exception {
    Map<String, Object> params = ActionContext.getContext().getParameters();
    Integer node_id = Integer.valueOf(((String[])params.get("node_id"))[0]);
    model.setNode(nodeDao.findByKey(node_id));
    snmpAgentDao.create(model);

    return "success";
  }

  @Action(
    value = "snmp-agent-update",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  public String update() throws Exception {
    SnmpAgent update = snmpAgentDao.findByKey(model.getId());

    @SuppressWarnings("unchecked")
    Map<String, Object> parameters
      = (Map<String, Object>)ActionContext.getContext().get("parameters");
    parameters.put("node_id", update.getNode().getId());

    update.setAddress(model.getAddress());
    update.setPort(model.getPort());
    update.setCommunityName(model.getCommunityName());
    update.setSecurityName(model.getSecurityName());
    update.setFileMibPath(model.getFileMibPath());
    update.setFileMibFormat(model.getFileMibFormat());
    update.setCommunityStringIndexList(model.getCommunityStringIndexList());
    snmpAgentDao.update(update);

    return "success";
  }

  @Override
  public void validate() {
    String contextName = ActionContext.getContext().getName();

    if (contextName.equals("snmp-agent")) {
      if (model.getId() == null) {
        addActionError(getText("select.a.row"));
        return;
      }
    }

    if (contextName.equals("snmp-agent-create")) {
      Map<String, Object> params = ActionContext.getContext().getParameters();
      Integer node_id = Integer.valueOf(((String[])params.get("node_id"))[0]);
      if (nodeDao.findByKey(node_id).getAgent() != null) {
        addActionError(getText("node.can.have.one.snmpAgent"));
        return;
      }
    }

    if (contextName.equals("snmp-agent-update")) {
      if (model.getId() == null) {
        addActionError(getText("select.a.row"));
      }
    }
  }

}
