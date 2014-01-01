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
import com.github.kaitoy.sneo.giane.action.message.IpV6RouteMessage;
import com.github.kaitoy.sneo.giane.interceptor.GoingBackward;
import com.github.kaitoy.sneo.giane.interceptor.GoingForward;
import com.github.kaitoy.sneo.giane.model.FixedIpV6Route;
import com.github.kaitoy.sneo.giane.model.dao.FixedIpV6RouteDao;
import com.github.kaitoy.sneo.giane.model.dao.NodeDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class FixedIpV6RouteAction
extends ActionSupport
implements ModelDriven<FixedIpV6Route>, ParameterAware,
  FormMessage, IpV6RouteMessage, DialogMessage {

  /**
   *
   */
  private static final long serialVersionUID = -2571561164474051146L;

  private FixedIpV6Route model = new FixedIpV6Route();
  private Map<String, String[]> parameters;
  private FixedIpV6RouteDao fixedIpV6RouteDao;
  private NodeDao nodeDao;
  private String deletingIdList;

  public FixedIpV6Route getModel() { return model; }

  @VisitorFieldValidator(appendPrefix = true)
  public void setModel(FixedIpV6Route model) { this.model = model; }

  public void setParameters(Map<String, String[]> parameters) {
    this.parameters = parameters;
  }

  // for DI
  public void setFixedIpV6RouteDao(FixedIpV6RouteDao fixedIpV6RouteDao) {
    this.fixedIpV6RouteDao = fixedIpV6RouteDao;
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
    valueMap.put("fixedIpV6Route_id", model.getId());
    stack.push(valueMap);

    return "config";
  }

  @Action(
    value = "back-to-fixed-ip-v6-route-config",
    results = { @Result(name = "config", location = "fixed-ip-v6-route-config.jsp")}
  )
  @SkipValidation
  @GoingBackward
  public String back() throws Exception {
    ValueStack stack = ActionContext.getContext().getValueStack();
    Map<String, Object> valueMap = new HashMap<String, Object>();
    valueMap.put("fixedIpV6Route_id", parameters.get("fixedIpV6Route_id")[0]);
    stack.push(valueMap);

    return "config";
  }

  @Action(
    value = "fixed-ip-v6-route-grid-box",
    results = { @Result(name = "success", location = "ip-v6-route-grid.jsp")}
  )
  @SkipValidation
  public String gridBox() throws Exception {
    return "success";
  }

  @Action(
    value = "fixed-ip-v6-route-tab-content",
    results = { @Result(name = "tab", location = "fixed-ip-v6-route-tab-content.jsp")}
  )
  @SkipValidation
  public String tab() throws Exception {
    return "tab";
  }

  @Action(
    value = "fixed-ip-v6-route-create",
    results = {
      @Result(name = "success", location = "empty.jsp")
    }
  )
  public String create() throws Exception {
    Integer node_id = Integer.valueOf(parameters.get("node_id")[0]);
    model.setNode(nodeDao.findByKey(node_id));
    fixedIpV6RouteDao.create(model);

    return "success";
  }

  @Action(
    value = "fixed-ip-v6-route-update",
    results = {
      @Result(name = "success", location = "empty.jsp")
    }
  )
  public String update() throws Exception {
    FixedIpV6Route update = fixedIpV6RouteDao.findByKey(model.getId());
    update.setNetworkDestination(model.getNetworkDestination());
    update.setPrefixLength(model.getPrefixLength());
    update.setGateway(model.getGateway());
    update.setMetric(model.getMetric());
    fixedIpV6RouteDao.update(update);

    return "success";
  }

  @Action(
    value = "fixed-ip-v6-route-delete",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  @SkipValidation
  public String delete() throws Exception {
    List<FixedIpV6Route> deletingList = new ArrayList<FixedIpV6Route>();
    for (String idStr: deletingIdList.split(",")) {
      deletingList.add(fixedIpV6RouteDao.findByKey(Integer.valueOf(idStr)));
    }
    fixedIpV6RouteDao.delete(deletingList);
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

    if (contextName.equals("fixed-ip-v6-route-update")) {
      if (model.getId() == null) {
        addActionError(getText("select.a.row"));
      }
    }
  }

}
