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
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.github.kaitoy.sneo.giane.model.Network;
import com.github.kaitoy.sneo.giane.model.dao.NetworkDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class NetworkAction
extends ActionSupport implements ModelDriven<Network>, FormMessage {

  /**
   *
   */
  private static final long serialVersionUID = -80416172987212604L;

  private Network model = new Network();
  private NetworkDao networkDao;
  private String uniqueColumn;

  public Network getModel() { return model; }

  @VisitorFieldValidator(appendPrefix = false)
  public void setModel(Network model) { this.model = model; }

  // for DI
  public void setNetworkDao(NetworkDao networkDao) {
    this.networkDao = networkDao;
  }

  public String getUniqueColumn() {
    return uniqueColumn;
  }

  @Override
  @SkipValidation
  public String execute() throws Exception {
    @SuppressWarnings("unchecked")
    Map<String, Object> parameters
      = (Map<String, Object>)ActionContext.getContext().get("parameters");
    if (parameters.get("network_id") == null) {
      parameters.put("network_id", model.getId());
      parameters.put("network_name", model.getName());
    }

    return "config";
  }

  @Action(
    value = "network-tab-content",
    results = { @Result(name = "tab", location = "network-tab-content.jsp")}
  )
  @SkipValidation
  public String tab() throws Exception {
    return "tab";
  }

  @Action(
    value = "network-create",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  public String create() throws Exception {
    networkDao.create(model);
    return "success";
  }

  @Action(
    value = "network-update",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  public String update() throws Exception {
    Network update = networkDao.findByKey(model.getId());

    update.setName(model.getName());
    update.setDescr(model.getDescr());
    networkDao.update(update);

    return "success";
  }

  @Override
  public void validate() {
    String contextName = ActionContext.getContext().getName();

    if (contextName.equals("network-update")) {
      if (model.getId() == null) {
        addActionError(getText("select.a.row"));
        return;
      }

      if (model.getName() != null) {
        Network someone
          = networkDao.findByName(model.getName());
        if (someone != null && !someone.getId().equals(model.getId())) {
          uniqueColumn = getText("network.name.label");
          addActionError(getText("need.to.be.unique"));
          return;
        }
      }
    }

    if (contextName.equals("network-create")) {
      if (
           model.getName() != null
        && networkDao.findByName(model.getName()) != null
      ) {
        uniqueColumn = getText("network.name.label");
        addActionError(getText("need.to.be.unique"));
        return;
      }
    }
  }

}
