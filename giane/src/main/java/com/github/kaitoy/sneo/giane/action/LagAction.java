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
import org.apache.struts2.interceptor.validation.SkipValidation;
import com.github.kaitoy.sneo.giane.action.message.AssociateActionMessage;
import com.github.kaitoy.sneo.giane.action.message.BreadCrumbsMessage;
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.github.kaitoy.sneo.giane.action.message.LagMessage;
import com.github.kaitoy.sneo.giane.model.Lag;
import com.github.kaitoy.sneo.giane.model.LagIpAddressRelation;
import com.github.kaitoy.sneo.giane.model.dao.IpAddressRelationDao;
import com.github.kaitoy.sneo.giane.model.dao.LagDao;
import com.github.kaitoy.sneo.giane.model.dao.NodeDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class LagAction
extends ActionSupport
implements ModelDriven<Lag>, FormMessage, LagMessage, BreadCrumbsMessage,
  AssociateActionMessage {

  /**
   *
   */
  private static final long serialVersionUID = 6917277494829749632L;

  private Lag model = new Lag();
  private LagDao lagDao;
  private NodeDao nodeDao;
  private IpAddressRelationDao ipAddressRelationDao;
  private String uniqueColumn;
  private String uniqueDomain;

  public Lag getModel() { return model; }

  @VisitorFieldValidator(appendPrefix = false)
  public void setModel(Lag model) { this.model = model; }

  // for DI
  public void setLagDao(LagDao lagDao) {
    this.lagDao = lagDao;
  }

  // for DI
  public void setNodeDao(NodeDao nodeDao) {
    this.nodeDao = nodeDao;
  }

  // for DI
  public void setIpAddressRelationDao(IpAddressRelationDao ipAddressRelationDao) {
    this.ipAddressRelationDao = ipAddressRelationDao;
  }

  public String getUniqueColumn() {
    return uniqueColumn;
  }

  public String getUniqueDomain() {
    return uniqueDomain;
  }

  @Override
  public String execute() throws Exception {
    @SuppressWarnings("unchecked")
    Map<String, Object> parameters
      = (Map<String, Object>)ActionContext.getContext().get("parameters");
    setModel(lagDao.findByKey(model.getId()));
    parameters.put("network_id", model.getNode().getNetwork().getId());
    parameters.put("network_name", model.getNode().getNetwork().getName());
    parameters.put("node_id", model.getNode().getId());
    parameters.put("node_name", model.getNode().getName());
    parameters.put("lag_id", model.getId());
    parameters.put("lag_name", model.getName());
    parameters.put(
      "ipAddressRelation_id", model.getIpAddressRelation().getId()
    );

    return "config";
  }

  @Action(
    value = "back-to-lag-config",
    results = { @Result(name = "config", location = "lag-config.jsp")}
  )
  @SkipValidation
  public String back() throws Exception {
    return "config";
  }

  @Action(
    value = "lag-tab-content",
    results = { @Result(name = "tab", location = "lag-tab-content.jsp")}
  )
  @SkipValidation
  public String tab() throws Exception {
    return "tab";
  }

  @Action(
    value = "associate-lag-with-physical-network-interfaces-tab-content",
    results = {
      @Result(
        name = "tab",
        location = "associate-lag-with-physical-network-interfaces-tab-content.jsp"
      )
    }
  )
  @SkipValidation
  public String associatePhysicalNetworkInterfacesTab() throws Exception {
    return "tab";
  }

  @Action(
    value = "lag-create",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  public String create() throws Exception {
    Map<String, Object> params = ActionContext.getContext().getParameters();
    Integer node_id = Integer.valueOf(((String[])params.get("node_id"))[0]);
    model.setNode(nodeDao.findByKey(node_id));

    LagIpAddressRelation relation
      = new LagIpAddressRelation();
    relation.setLag(model);
    ipAddressRelationDao.create(relation);

    model.setIpAddressRelation(relation);
    lagDao.create(model);

    return "success";
  }

  @Action(
    value = "lag-update",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  public String update() throws Exception {
    Lag update = lagDao.findByKey(model.getId());
    update.setName(model.getName());
    update.setChannelGroupNumber(model.getChannelGroupNumber());
    lagDao.update(update);

    return "success";
  }

  @Override
  public void validate() {
    String contextName = ActionContext.getContext().getName();

    if (contextName.equals("lag")) {
      if (model.getId() == null) {
        addActionError(getText("select.a.row"));
        return;
      }
    }

    if (contextName.equals("lag-update")) {
      if (model.getId() == null) {
        addActionError(getText("select.a.row"));
      }

      if (model.getName() != null) {
        @SuppressWarnings("unchecked")
        Map<String, Object> params
          = (Map<String, Object>)ActionContext.getContext().get("parameters");
        Integer nodeId = Integer.valueOf(((String[])params.get("node_id"))[0]);

        Lag someone= lagDao.findByNameAndNodeId(model.getName(), nodeId);
        if (someone != null && !someone.getId().equals(model.getId())) {
          uniqueDomain = getText("lag.node.label");
          uniqueColumn = getText("lag.name.label");
          addActionError(getText("need.to.be.unique.in.domain"));
          return;
        }
      }
    }

    if (contextName.equals("lag-create")) {
      Map<String, Object> params = ActionContext.getContext().getParameters();
      Integer nodeId = Integer.valueOf(((String[])params.get("node_id"))[0]);
      if (
           model.getName() != null
        && lagDao.findByNameAndNodeId(model.getName(), nodeId) != null
      ) {
        uniqueDomain = getText("lag.node.label");
        uniqueColumn = getText("lag.name.label");
        addActionError(getText("need.to.be.unique.in.domain"));
        return;
      }
    }
  }

}
