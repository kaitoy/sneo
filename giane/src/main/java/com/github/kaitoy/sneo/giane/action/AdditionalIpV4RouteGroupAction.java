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
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.github.kaitoy.sneo.giane.model.AdditionalIpV4RouteGroup;
import com.github.kaitoy.sneo.giane.model.dao.AdditionalIpV4RouteGroupDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class AdditionalIpV4RouteGroupAction
extends ActionSupport implements ModelDriven<AdditionalIpV4RouteGroup>, FormMessage {

  /**
   *
   */
  private static final long serialVersionUID = -6073538245923614284L;

  private AdditionalIpV4RouteGroup model = new AdditionalIpV4RouteGroup();

  private AdditionalIpV4RouteGroupDao additionalIpV4RouteGroupDao;
  private String uniqueColumn;

  public AdditionalIpV4RouteGroup getModel() { return model; }

  @VisitorFieldValidator(appendPrefix = false)
  public void setModel(AdditionalIpV4RouteGroup model) { this.model = model; }

  // for DI
  public void setAdditionalIpV4RouteGroupDao(AdditionalIpV4RouteGroupDao additionalIpV4RouteGroupDao) {
    this.additionalIpV4RouteGroupDao = additionalIpV4RouteGroupDao;
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
    if (parameters.get("additionalIpV4RouteGroup_id") == null) {
      parameters.put("additionalIpV4RouteGroup_id", model.getId());
      parameters.put("additionalIpV4RouteGroup_name", model.getName());
    }

    return "config";
  }

  @Action(
    value = "additional-ip-v4-route-group-tab-content",
    results = { @Result(name = "tab", location = "additional-ip-v4-route-group-tab-content.jsp")}
  )
  @SkipValidation
  public String tab() throws Exception {
    return "tab";
  }

  @Action(
    value = "associate-additional-ip-v4-route-group-with-additional-ip-v4-routes-tab-content",
    results = {
      @Result(
        name = "tab",
        location = "associate-additional-ip-v4-route-group-with-additional-ip-v4-routes-tab-content.jsp"
      )
    }
  )
  @SkipValidation
  public String associateAdditionalIpV4RoutesTab() throws Exception {
    return "tab";
  }

  @Action(
    value = "node-associated-additional-ip-v4-route-group-grid-box",
    results = {
      @Result(
        name = "grid",
        location = "node-associated-additional-ip-v4-route-group-grid.jsp"
      )
    }
  )
  @SkipValidation
  public String nodeAssociatedGrid() throws Exception {
    return "grid";
  }

  @Action(
    value = "node-unassociated-additional-ip-v4-route-group-grid-box",
    results = {
      @Result(
        name = "grid",
        location = "node-unassociated-additional-ip-v4-route-group-grid.jsp"
      )
    }
  )
  @SkipValidation
  public String nodeUnassociatedGrid() throws Exception {
    return "grid";
  }

  @Action(
    value = "additional-ip-v4-route-group-create",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  public String create() throws Exception {
    additionalIpV4RouteGroupDao.create(model);
    return "success";
  }

  @Action(
    value = "additional-ip-v4-route-group-update",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  public String update() throws Exception {
    AdditionalIpV4RouteGroup update = additionalIpV4RouteGroupDao.findByKey(model.getId());
    update.setName(model.getName());
    update.setDescr(model.getDescr());
    additionalIpV4RouteGroupDao.update(update);

    return "success";
  }

  @Override
  public void validate() {
    String contextName = ActionContext.getContext().getName();

    if (contextName.equals("additional-ip-v4-route-group-update")) {
      if (model.getId() == null) {
        addActionError(getText("select.a.row"));
        return;
      }

      if (model.getName() != null) {
        AdditionalIpV4RouteGroup someone
          = additionalIpV4RouteGroupDao.findByName(model.getName());
        if (someone != null && !someone.getId().equals(model.getId())) {
          uniqueColumn = getText("additionalIpV4RouteGroup.name.label");
          addActionError(getText("need.to.be.unique"));
          return;
        }
      }
    }

    if (contextName.equals("additional-ip-v4-route-group-create")) {
      if (
           model.getName() != null
        && additionalIpV4RouteGroupDao.findByName(model.getName()) != null
      ) {
        uniqueColumn = getText("additionalIpV4RouteGroup.name.label");
        addActionError(getText("need.to.be.unique"));
        return;
      }
    }
  }

}
