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
import com.github.kaitoy.sneo.giane.action.message.FixedIpV4RouteMessage;
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.github.kaitoy.sneo.giane.model.FixedIpV4Route;
import com.github.kaitoy.sneo.giane.model.dao.FixedIpV4RouteDao;
import com.github.kaitoy.sneo.giane.model.dao.NodeDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class FixedIpV4RouteAction
extends ActionSupport
implements ModelDriven<FixedIpV4Route>, FormMessage, FixedIpV4RouteMessage {

  /**
   *
   */
  private static final long serialVersionUID = -6637666624136765566L;

  private FixedIpV4Route model = new FixedIpV4Route();
  private FixedIpV4RouteDao fixedIpV4RouteDao;
  private NodeDao nodeDao;

  public FixedIpV4Route getModel() { return model; }

  @VisitorFieldValidator(appendPrefix = false)
  public void setModel(FixedIpV4Route model) { this.model = model; }

  // for DI
  public void setFixedIpV4RouteDao(FixedIpV4RouteDao fixedIpV4RouteDao) {
    this.fixedIpV4RouteDao = fixedIpV4RouteDao;
  }

  // for DI
  public void setNodeDao(NodeDao nodeDao) {
    this.nodeDao = nodeDao;
  }

  @Override
  public String execute() throws Exception {
    @SuppressWarnings("unchecked")
    Map<String, Object> parameters
      = (Map<String, Object>)ActionContext.getContext().get("parameters");
    parameters.put("fixedIpV4Route_id", model.getId());

    return "config";
  }

  @Action(
    value = "back-to-fixed-ip-v4-route-config",
    results = { @Result(name = "config", location = "fixed-ip-v4-route-config.jsp")}
  )
  @SkipValidation
  public String back() throws Exception {
    return "config";
  }

  @Action(
    value = "fixed-ip-v4-route-tab-content",
    results = { @Result(name = "tab", location = "fixed-ip-v4-route-tab-content.jsp")}
  )
  @SkipValidation
  public String tab() throws Exception {
    return "tab";
  }

  @Action(
    value = "fixed-ip-v4-route-create",
    results = {
      @Result(name = "success", location = "empty.jsp")
    }
  )
  public String create() throws Exception {
    Map<String, Object> params = ActionContext.getContext().getParameters();
    Integer node_id = Integer.valueOf(((String[])params.get("node_id"))[0]);
    model.setNode(nodeDao.findByKey(node_id));
    fixedIpV4RouteDao.create(model);

    return "success";
  }

  @Action(
    value = "fixed-ip-v4-route-update",
    results = {
      @Result(name = "success", location = "empty.jsp")
    }
  )
  public String update() throws Exception {
    FixedIpV4Route update = fixedIpV4RouteDao.findByKey(model.getId());
    update.setNetworkDestination(model.getNetworkDestination());
    update.setNetmask(model.getNetmask());
    update.setGateway(model.getGateway());
    update.setMetric(model.getMetric());
    fixedIpV4RouteDao.update(update);

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

    if (contextName.equals("fixed-ip-v4-route-update")) {
      if (model.getId() == null) {
        addActionError(getText("select.a.row"));
      }
    }
  }

}
