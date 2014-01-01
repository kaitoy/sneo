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
import com.github.kaitoy.sneo.giane.model.AdditionalIpV4Route;
import com.github.kaitoy.sneo.giane.model.dao.AdditionalIpV4RouteDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class AdditionalIpV4RouteAction
extends ActionSupport
implements ModelDriven<AdditionalIpV4Route>, ParameterAware,
  FormMessage, IpV4RouteMessage, DialogMessage {

  /**
   *
   */
  private static final long serialVersionUID = -6637666624136765566L;

  private AdditionalIpV4Route model = new AdditionalIpV4Route();
  private Map<String, String[]> parameters;
  private AdditionalIpV4RouteDao additionalIpV4RouteDao;
  private String uniqueColumn;
  private String deletingIdList;

  public AdditionalIpV4Route getModel() { return model; }

  public void setParameters(Map<String, String[]> parameters) {
    this.parameters = parameters;
  }

  @VisitorFieldValidator(appendPrefix = true)
  public void setModel(AdditionalIpV4Route model) { this.model = model; }

  // for DI
  public void setAdditionalIpV4RouteDao(
    AdditionalIpV4RouteDao additionalIpV4RouteDao
  ) {
    this.additionalIpV4RouteDao = additionalIpV4RouteDao;
  }

  public String getUniqueColumn() {
    return uniqueColumn;
  }

  public void setDeletingIdList(String deletingIdList) {
    this.deletingIdList = deletingIdList;
  }

  @Override
  @GoingForward
  public String execute() throws Exception {
    ValueStack stack = ActionContext.getContext().getValueStack();
    Map<String, Object> valueMap = new HashMap<String, Object>();
    valueMap.put("additionalIpV4Route_id", model.getId());
    valueMap.put("additionalIpV4Route_name", model.getName());
    stack.push(valueMap);

    return "config";
  }

  @Action(
    value = "back-to-additional-ip-v4-route-config",
    results = { @Result(name = "config", location = "additional-ip-v4-route-config.jsp")}
  )
  @SkipValidation
  @GoingBackward
  public String back() throws Exception {
    ValueStack stack = ActionContext.getContext().getValueStack();
    Map<String, Object> valueMap = new HashMap<String, Object>();
    valueMap.put("additionalIpV4Route_id", parameters.get("additionalIpV4Route_id")[0]);
    valueMap.put("additionalIpV4Route_name", parameters.get("additionalIpV4Route_name")[0]);
    stack.push(valueMap);

    return "config";
  }

  @Action(
    value = "additional-ip-v4-route-grid-box",
    results = { @Result(name = "success", location = "ip-v4-route-grid.jsp")}
  )
  @SkipValidation
  public String gridBox() throws Exception {
    return "success";
  }

  @Action(
    value = "additional-ip-v4-route-tab-content",
    results = {
      @Result(name = "tab", location = "additional-ip-v4-route-tab-content.jsp")
    }
  )
  @SkipValidation
  public String tab() throws Exception {
    return "tab";
  }

  @Action(
    value = "additional-ip-v4-route-create",
    results = {@Result(name = "success", location = "empty.jsp")}
  )
  public String create() throws Exception {
    additionalIpV4RouteDao.create(model);
    return "success";
  }

  @Action(
    value = "additional-ip-v4-route-update",
    results = {@Result(name = "success", location = "empty.jsp")}
  )
  public String update() throws Exception {
    AdditionalIpV4Route update = additionalIpV4RouteDao.findByKey(model.getId());
    update.setName(model.getName());
    update.setNetworkDestination(model.getNetworkDestination());
    update.setNetmask(model.getNetmask());
    update.setGateway(model.getGateway());
    update.setMetric(model.getMetric());
    update.setDescr(model.getDescr());
    additionalIpV4RouteDao.update(update);

    return "success";
  }

  @Action(
    value = "additional-ip-v4-route-delete",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  @SkipValidation
  public String delete() throws Exception {
    List<AdditionalIpV4Route> deletingList = new ArrayList<AdditionalIpV4Route>();
    for (String idStr: deletingIdList.split(",")) {
      deletingList.add(additionalIpV4RouteDao.findByKey(Integer.valueOf(idStr)));
    }
    additionalIpV4RouteDao.delete(deletingList);

    return "success";
  }

  @Override
  public void validate() {
    String contextName = ActionContext.getContext().getName();

    if (contextName.equals("additional-ip-v4-route-update")) {
      if (model.getName() != null) {
        AdditionalIpV4Route someone
          = additionalIpV4RouteDao.findByName(model.getName());
        if (someone != null && !someone.getId().equals(model.getId())) {
          uniqueColumn = getText("ipV4Route.name.label");
          addFieldError("name", getText("need.to.be.unique"));
          return;
        }
      }
    }

    if (contextName.equals("additional-ip-v4-route-create")) {
      if (
           model.getName() != null
        && additionalIpV4RouteDao.findByName(model.getName()) != null
      ) {
        uniqueColumn = getText("ipV4Route.name.label");
        addFieldError("name", getText("need.to.be.unique"));
        return;
      }
    }
  }

}
