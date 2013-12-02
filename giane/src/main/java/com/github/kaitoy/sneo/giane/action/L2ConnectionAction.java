/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
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
import com.github.kaitoy.sneo.giane.action.message.L2ConnectionMessage;
import com.github.kaitoy.sneo.giane.model.L2Connection;
import com.github.kaitoy.sneo.giane.model.dao.L2ConnectionDao;
import com.github.kaitoy.sneo.giane.model.dao.NetworkDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class L2ConnectionAction
extends ActionSupport
implements ModelDriven<L2Connection>, FormMessage, L2ConnectionMessage,
  BreadCrumbsMessage, AssociateActionMessage {

  /**
   *
   */
  private static final long serialVersionUID = -2836536393523178033L;

  private L2Connection model = new L2Connection();
  private L2ConnectionDao l2ConnectionDao;
  private NetworkDao networkDao;
  private String uniqueColumn;
  private String uniqueDomain;

  public L2Connection getModel() { return model; }

  @VisitorFieldValidator(appendPrefix = false)
  public void setModel(L2Connection model) { this.model = model; }

  // for DI
  public void setL2ConnectionDao(L2ConnectionDao l2ConnectionDao) {
    this.l2ConnectionDao = l2ConnectionDao;
  }

  // for DI
  public void setNetworkDao(NetworkDao networkDao) {
    this.networkDao = networkDao;
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
    setModel(l2ConnectionDao.findByKey(model.getId()));
    parameters.put("network_id", model.getNetwork().getId());
    parameters.put("l2Connection_id", model.getId());
    parameters.put("l2Connection_name", model.getName());

    return "config";
  }

  @Action(
    value = "back-to-l2-connection-config",
    results = { @Result(name = "config", location = "l2-connection-config.jsp")}
  )
  @SkipValidation
  public String back() throws Exception {
    return "config";
  }

  @Action(
    value = "l2-connection-tab-content",
    results = { @Result(name = "tab", location = "l2-connection-tab-content.jsp")}
  )
  @SkipValidation
  public String tab() throws Exception {
    return "tab";
  }

  @Action(
    value = "associate-l2-connection-with-physical-network-interfaces-tab-content",
    results = {
      @Result(
        name = "tab",
        location = "associate-l2-connection-with-physical-network-interfaces-tab-content.jsp"
      )
    }
  )
  @SkipValidation
  public String associatePhysicalNetworkInterfacesTab() throws Exception {
    return "tab";
  }

  @Action(
    value = "l2-connection-create",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  public String create() throws Exception {
    Map<String, Object> params = ActionContext.getContext().getParameters();
    Integer network_id = Integer.valueOf(((String[])params.get("network_id"))[0]);
    model.setNetwork(networkDao.findByKey(network_id));
    l2ConnectionDao.create(model);

    return "success";
  }

  @Action(
    value = "l2-connection-update",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  public String update() throws Exception {
    L2Connection update = l2ConnectionDao.findByKey(model.getId());
    update.setName(model.getName());
    l2ConnectionDao.update(update);

    return "success";
  }

  @Override
  public void validate() {
    String contextName = ActionContext.getContext().getName();

    if (contextName.equals("l2-connection")) {
      if (model.getId() == null) {
        addActionError(getText("select.a.row"));
        return;
      }
    }

    if (contextName.equals("l2-connection-update")) {
      if (model.getId() == null) {
        addActionError(getText("select.a.row"));
        return;
      }

      if (model.getName() != null) {
        Map<String, Object> params = ActionContext.getContext().getParameters();
        Integer networkId
          = Integer.valueOf(((String[])params.get("network_id"))[0]);

        L2Connection someone
          = l2ConnectionDao
              .findByNameAndNetworkId(model.getName(), networkId);
        if (someone != null && !someone.getId().equals(model.getId())) {
          uniqueDomain = getText("l2Connection.network.label");
          uniqueColumn = getText("l2Connection.name.label");
          addActionError(getText("need.to.be.unique.in.domain"));
          return;
        }
      }
    }

    if (contextName.equals("l2-connection-create")) {
      Map<String, Object> params = ActionContext.getContext().getParameters();
      Integer networkId
        = Integer.valueOf(((String[])params.get("network_id"))[0]);
      if (
           model.getName() != null
        && l2ConnectionDao
             .findByNameAndNetworkId(model.getName(), networkId) != null
      ) {
        uniqueDomain = getText("l2Connection.network.label");
        uniqueColumn = getText("l2Connection.name.label");
        addActionError(getText("need.to.be.unique.in.domain"));
        return;
      }
    }
  }

}
