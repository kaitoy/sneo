/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
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
import com.github.kaitoy.sneo.giane.action.message.DialogMessage;
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.github.kaitoy.sneo.giane.action.message.IpV4RouteMessage;
import com.github.kaitoy.sneo.giane.interceptor.GoingBackward;
import com.github.kaitoy.sneo.giane.interceptor.GoingForward;
import com.github.kaitoy.sneo.giane.model.FixedIpV4Route;
import com.github.kaitoy.sneo.giane.model.dao.FixedIpV4RouteDao;
import com.github.kaitoy.sneo.giane.model.dao.NodeDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class FixedIpV4RouteAction
extends ActionSupport
implements ModelDriven<FixedIpV4Route>, ParameterAware,
  FormMessage, IpV4RouteMessage, DialogMessage {

  /**
   *
   */
  private static final long serialVersionUID = -6637666624136765566L;

  private FixedIpV4Route model = new FixedIpV4Route();
  private Map<String, String[]> parameters;
  private FixedIpV4RouteDao fixedIpV4RouteDao;
  private NodeDao nodeDao;
  private String deletingIdList;

  public FixedIpV4Route getModel() { return model; }

  @VisitorFieldValidator(appendPrefix = true)
  public void setModel(FixedIpV4Route model) { this.model = model; }

  public void setParameters(Map<String, String[]> parameters) {
    this.parameters = parameters;
  }

  // for DI
  public void setFixedIpV4RouteDao(FixedIpV4RouteDao fixedIpV4RouteDao) {
    this.fixedIpV4RouteDao = fixedIpV4RouteDao;
  }

  // for DI
  public void setNodeDao(NodeDao nodeDao) {
    this.nodeDao = nodeDao;
  }

  public void setDeletingIdList(String deletingIdList) {
    this.deletingIdList = deletingIdList;
  }

  @Override
  @GoingForward
  public String execute() throws Exception {
    ValueStack stack = ActionContext.getContext().getValueStack();
    Map<String, Object> valueMap = new HashMap<String, Object>();
    valueMap.put("fixedIpV4Route_id", model.getId());
    stack.push(valueMap);

    return "config";
  }

  @Action(
    value = "back-to-fixed-ip-v4-route-config",
    results = { @Result(name = "config", location = "fixed-ip-v4-route-config.jsp")}
  )
  @SkipValidation
  @GoingBackward
  public String back() throws Exception {
    ValueStack stack = ActionContext.getContext().getValueStack();
    Map<String, Object> valueMap = new HashMap<String, Object>();
    valueMap.put("fixedIpV4Route_id", parameters.get("fixedIpV4Route_id")[0]);
    stack.push(valueMap);

    return "config";
  }

  @Action(
    value = "fixed-ip-v4-route-grid-box",
    results = { @Result(name = "success", location = "ip-v4-route-grid.jsp")}
  )
  @SkipValidation
  public String gridBox() throws Exception {
    return "success";
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
    Integer node_id = Integer.valueOf(parameters.get("node_id")[0]);
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

  @Action(
    value = "fixed-ip-v4-route-delete",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  @SkipValidation
  public String delete() throws Exception {
    List<FixedIpV4Route> deletingList = new ArrayList<FixedIpV4Route>();
    for (String idStr: deletingIdList.split(",")) {
      deletingList.add(fixedIpV4RouteDao.findByKey(Integer.valueOf(idStr)));
    }
    fixedIpV4RouteDao.delete(deletingList);
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
