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

import com.github.kaitoy.sneo.giane.model.AdditionalIpV4Route;
import com.github.kaitoy.sneo.giane.model.dao.AdditionalIpV4RouteDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class AdditionalIpV4RouteAction
extends ActionSupport implements ModelDriven<AdditionalIpV4Route> {

  /**
   *
   */
  private static final long serialVersionUID = -6637666624136765566L;

  private AdditionalIpV4Route model = new AdditionalIpV4Route();
  private AdditionalIpV4RouteDao additionalIpV4RouteDao;
  private String uniqueColumn;

  public AdditionalIpV4Route getModel() { return model; }

  @VisitorFieldValidator(appendPrefix = false)
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

  @SkipValidation
  public String execute() throws Exception {
    @SuppressWarnings("unchecked")
    Map<String, Object> parameters
      = (Map<String, Object>)ActionContext.getContext().get("parameters");
    if (parameters.get("network_id") == null) {
      setModel(additionalIpV4RouteDao.findByKey(model.getId()));
      parameters.put("additionalIpV4Route_id", model.getId());
      parameters.put("additionalIpV4Route_name", model.getName());
    }

    return "config";
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
      value = "additional-ip-v4-route-group-associated-additional-ip-v4-route-grid-box",
      results = { @Result(name = "grid", location = "additional-ip-v4-route-group-associated-additional-ip-v4-route-grid.jsp")}
    )
    @SkipValidation
    public String additionalIpV4RouteGroupAssociatedGrid() throws Exception {
      return "grid";
    }

    @Action(
      value = "additional-ip-v4-route-group-unassociated-additional-ip-v4-route-grid-box",
      results = { @Result(name = "grid", location = "additional-ip-v4-route-group-unassociated-additional-ip-v4-route-grid.jsp")}
    )
    @SkipValidation
    public String additionalIpV4RouteGroupUnassociatedGrid() throws Exception {
      return "grid";
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
    additionalIpV4RouteDao.update(update);

    return "success";
  }

  public void validate() {
    String contextName = ActionContext.getContext().getName();

    if (contextName.equals("additional-ip-v4-route-update")) {
      if (model.getId() == null) {
        addActionError(getText("select.a.row"));
        return;
      }

      if (model.getName() != null) {
        AdditionalIpV4Route someone
          = additionalIpV4RouteDao.findByName(model.getName());
        if (someone != null && !someone.getId().equals(model.getId())) {
          uniqueColumn = getText("additionalIpV4Route.name.label");
          addActionError(getText("need.to.be.unique"));
          return;
        }
      }
    }

    if (contextName.equals("additional-ip-v4-route-create")) {
      if (
           model.getName() != null
        && additionalIpV4RouteDao.findByName(model.getName()) != null
      ) {
        uniqueColumn = getText("additionalIpV4Route.name.label");
        addActionError(getText("need.to.be.unique"));
        return;
      }
    }
  }

}
