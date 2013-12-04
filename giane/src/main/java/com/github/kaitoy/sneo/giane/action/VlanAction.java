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
import com.github.kaitoy.sneo.giane.action.message.AssociateActionMessage;
import com.github.kaitoy.sneo.giane.action.message.BreadCrumbsMessage;
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.github.kaitoy.sneo.giane.action.message.VlanMessage;
import com.github.kaitoy.sneo.giane.interceptor.GoingBackward;
import com.github.kaitoy.sneo.giane.interceptor.GoingForward;
import com.github.kaitoy.sneo.giane.model.Vlan;
import com.github.kaitoy.sneo.giane.model.VlanIpAddressRelation;
import com.github.kaitoy.sneo.giane.model.dao.IpAddressRelationDao;
import com.github.kaitoy.sneo.giane.model.dao.NodeDao;
import com.github.kaitoy.sneo.giane.model.dao.VlanDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class VlanAction
extends ActionSupport
implements ModelDriven<Vlan>, ParameterAware, FormMessage, VlanMessage, BreadCrumbsMessage,
  AssociateActionMessage {

  /**
   *
   */
  private static final long serialVersionUID = -5289730548784449639L;

  private Vlan model = new Vlan();
  private Map<String, String[]> parameters;
  private VlanDao vlanDao;
  private IpAddressRelationDao ipAddressRelationDao;
  private NodeDao nodeDao;
  private String uniqueColumn;
  private String uniqueDomain;

  public Vlan getModel() { return model; }

  @VisitorFieldValidator(appendPrefix = false)
  public void setModel(Vlan model) { this.model = model; }

  public void setParameters(Map<String, String[]> parameters) {
    this.parameters = parameters;
  }

  // for DI
  public void setVlanDao(VlanDao vlanDao) {
    this.vlanDao = vlanDao;
  }

  // for DI
  public void setIpAddressRelationDao(IpAddressRelationDao ipAddressRelationDao) {
    this.ipAddressRelationDao = ipAddressRelationDao;
  }

  // for DI
  public void setNodeDao(NodeDao nodeDao) {
    this.nodeDao = nodeDao;
  }

  public String getUniqueColumn() {
    return uniqueColumn;
  }

  public String getUniqueDomain() {
    return uniqueDomain;
  }

  @Override
  @GoingForward
  public String execute() throws Exception {
    ValueStack stack = ActionContext.getContext().getValueStack();
    Map<String, Object> valueMap = new HashMap<String, Object>();
    setModel(vlanDao.findByKey(model.getId()));
    valueMap.put("node_id", model.getNode().getId());
    valueMap.put("vlan_id", model.getId());
    valueMap.put("vlan_name", model.getName());
    valueMap.put(
      "ipAddressRelation_id", model.getIpAddressRelation().getId()
    );
    stack.push(valueMap);

    return "config";
  }

  @Action(
    value = "back-to-vlan-config",
    results = { @Result(name = "config", location = "vlan-config.jsp")}
  )
  @SkipValidation
  @GoingBackward
  public String back() throws Exception {
    ValueStack stack = ActionContext.getContext().getValueStack();
    Map<String, Object> valueMap = new HashMap<String, Object>();
    valueMap.put("node_id", parameters.get("node_id")[0]);
    valueMap.put("vlan_id", parameters.get("vlan_id")[0]);
    valueMap.put("vlan_name", parameters.get("vlan_name")[0]);
    valueMap.put("ipAddressRelation_id", parameters.get("ipAddressRelation_id")[0]);
    stack.push(valueMap);

    return "config";
  }

  @Action(
    value = "vlan-tab-content",
    results = { @Result(name = "tab", location = "vlan-tab-content.jsp")}
  )
  @SkipValidation
  public String tab() throws Exception {
    return "tab";
  }

  @Action(
    value = "associate-vlan-with-vlan-members-tab-content",
    results = {
      @Result(
        name = "tab",
        location = "associate-vlan-with-vlan-members-tab-content.jsp"
      )
    }
  )
  @SkipValidation
  public String associateVlanMembersTab() throws Exception {
    return "tab";
  }

  @Action(
    value = "vlan-create",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  public String create() throws Exception {
    Map<String, Object> params = ActionContext.getContext().getParameters();
    Integer node_id = Integer.valueOf(((String[])params.get("node_id"))[0]);
    model.setNode(nodeDao.findByKey(node_id));

    VlanIpAddressRelation relation
      = new VlanIpAddressRelation();
    relation.setVlan(model);
    ipAddressRelationDao.create(relation);

    model.setIpAddressRelation(relation);
    vlanDao.create(model);

    return "success";
  }

  @Action(
    value = "vlan-update",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  public String update() throws Exception {
    Vlan update = vlanDao.findByKey(model.getId());
    update.setName(model.getName());
    update.setVid(model.getVid());
    vlanDao.update(update);

    return "success";
  }

  @Override
  public void validate() {
    String contextName = ActionContext.getContext().getName();

    if (contextName.equals("vlan")) {
      if (model.getId() == null) {
        addActionError(getText("select.a.row"));
        return;
      }
    }

    if (contextName.equals("vlan-update")) {
      if (model.getId() == null) {
        addActionError(getText("select.a.row"));
      }

      if (model.getName() != null) {
        @SuppressWarnings("unchecked")
        Map<String, Object> params
          = (Map<String, Object>)ActionContext.getContext().get("parameters");
        Integer nodeId = Integer.valueOf(((String[])params.get("node_id"))[0]);

        Vlan someone= vlanDao.findByNameAndNodeId(model.getName(), nodeId);
        if (someone != null && !someone.getId().equals(model.getId())) {
          uniqueDomain = getText("vlan.node.label");
          uniqueColumn = getText("vlan.name.label");
          addActionError(getText("need.to.be.unique.in.domain"));
          return;
        }
      }
    }

    if (contextName.equals("vlan-create")) {
      Map<String, Object> params = ActionContext.getContext().getParameters();
      Integer nodeId = Integer.valueOf(((String[])params.get("node_id"))[0]);
      if (
           model.getName() != null
        && vlanDao.findByNameAndNodeId(model.getName(), nodeId) != null
      ) {
        uniqueDomain = getText("vlan.node.label");
        uniqueColumn = getText("vlan.name.label");
        addActionError(getText("need.to.be.unique.in.domain"));
        return;
      }
    }
  }

}
