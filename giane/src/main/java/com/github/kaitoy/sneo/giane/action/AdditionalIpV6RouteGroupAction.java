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
import com.github.kaitoy.sneo.giane.action.message.AdditionalIpV6RouteGroupMessage;
import com.github.kaitoy.sneo.giane.action.message.AssociateActionMessage;
import com.github.kaitoy.sneo.giane.action.message.BreadCrumbsMessage;
import com.github.kaitoy.sneo.giane.action.message.DialogMessage;
import com.github.kaitoy.sneo.giane.action.message.EntityGroupMessage;
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.github.kaitoy.sneo.giane.interceptor.GoingBackward;
import com.github.kaitoy.sneo.giane.interceptor.GoingForward;
import com.github.kaitoy.sneo.giane.model.AdditionalIpV6RouteGroup;
import com.github.kaitoy.sneo.giane.model.dao.AdditionalIpV6RouteGroupDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class AdditionalIpV6RouteGroupAction
extends ActionSupport
implements ModelDriven<AdditionalIpV6RouteGroup>, ParameterAware, FormMessage,
  AdditionalIpV6RouteGroupMessage, BreadCrumbsMessage, AssociateActionMessage, EntityGroupMessage,
  DialogMessage {

  /**
   *
   */
  private static final long serialVersionUID = -6073538245923614284L;

  private AdditionalIpV6RouteGroup model = new AdditionalIpV6RouteGroup();
  private Map<String, String[]> parameters;
  private AdditionalIpV6RouteGroupDao additionalIpV6RouteGroupDao;
  private String uniqueColumn;
  private String deletingIdList;

  public AdditionalIpV6RouteGroup getModel() { return model; }

  @VisitorFieldValidator(appendPrefix = true)
  public void setModel(AdditionalIpV6RouteGroup model) { this.model = model; }

  public void setParameters(Map<String, String[]> parameters) {
    this.parameters = parameters;
  }

  // for DI
  public void setAdditionalIpV6RouteGroupDao(AdditionalIpV6RouteGroupDao additionalIpV6RouteGroupDao) {
    this.additionalIpV6RouteGroupDao = additionalIpV6RouteGroupDao;
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
    valueMap.put("additionalIpV6RouteGroup_id", model.getId());
    valueMap.put("additionalIpV6RouteGroup_name", model.getName());
    stack.push(valueMap);

    return "config";
  }

  @Action(
    value = "back-to-additional-ip-v6-route-group-config",
    results = { @Result(name = "config", location = "additional-ip-v6-route-group-config.jsp")}
  )
  @SkipValidation
  @GoingBackward
  public String back() throws Exception {
    ValueStack stack = ActionContext.getContext().getValueStack();
    Map<String, Object> valueMap = new HashMap<String, Object>();
    valueMap.put("additionalIpV6RouteGroup_id", parameters.get("additionalIpV6RouteGroup_id")[0]);
    valueMap.put("additionalIpV6RouteGroup_name", parameters.get("additionalIpV6RouteGroup_name")[0]);
    stack.push(valueMap);

    return "config";
  }

  @Action(
    value = "additional-ip-v6-route-group-tab-content",
    results = { @Result(name = "tab", location = "additional-ip-v6-route-group-tab-content.jsp")}
  )
  @SkipValidation
  public String tab() throws Exception {
    return "tab";
  }

  @Action(
    value = "additional-ip-v6-route-group-grid-box",
    results = {
      @Result(
        name = "grid",
        location = "entity-group-grid.jsp"
      )
    }
  )
  @SkipValidation
  public String gridBox() throws Exception {
    return "grid";
  }

  @Action(
    value = "associate-additional-ip-v6-route-group-with-additional-ip-v6-routes-tab-content",
    results = {
      @Result(
        name = "tab",
        location = "associate-additional-ip-v6-route-group-with-additional-ip-v6-routes-tab-content.jsp"
      )
    }
  )
  @SkipValidation
  public String associateAdditionalIpV6RoutesTab() throws Exception {
    return "tab";
  }

  @Action(
    value = "node-associated-additional-ip-v6-route-group-grid-box",
    results = {
      @Result(
        name = "grid",
        location = "node-associated-additional-ip-v6-route-group-grid.jsp"
      )
    }
  )
  @SkipValidation
  public String nodeAssociatedGrid() throws Exception {
    return "grid";
  }

  @Action(
    value = "node-unassociated-additional-ip-v6-route-group-grid-box",
    results = {
      @Result(
        name = "grid",
        location = "node-unassociated-additional-ip-v6-route-group-grid.jsp"
      )
    }
  )
  @SkipValidation
  public String nodeUnassociatedGrid() throws Exception {
    return "grid";
  }

  @Action(
    value = "additional-ip-v6-route-group-create",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  public String create() throws Exception {
    additionalIpV6RouteGroupDao.create(model);
    return "success";
  }

  @Action(
    value = "additional-ip-v6-route-group-update",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  public String update() throws Exception {
    AdditionalIpV6RouteGroup update = additionalIpV6RouteGroupDao.findByKey(model.getId());
    update.setName(model.getName());
    update.setDescr(model.getDescr());
    additionalIpV6RouteGroupDao.update(update);

    return "success";
  }

  @Action(
    value = "additional-ip-v6-route-group-delete",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  @SkipValidation
  public String delete() throws Exception {
    List<AdditionalIpV6RouteGroup> deletingList = new ArrayList<AdditionalIpV6RouteGroup>();
    for (String idStr: deletingIdList.split(",")) {
      deletingList.add(additionalIpV6RouteGroupDao.findByKey(Integer.valueOf(idStr)));
    }
    additionalIpV6RouteGroupDao.delete(deletingList);
    return "success";
  }

  @Override
  public void validate() {
    String contextName = ActionContext.getContext().getName();

    if (contextName.equals("additional-ip-v6-route-group")) {
      if (model.getId() == null) {
        addActionError(getText("select.a.row"));
        return;
      }
    }

    if (contextName.equals("additional-ip-v6-route-group-update")) {
      if (model.getId() == null) {
        addActionError(getText("select.a.row"));
        return;
      }

      if (model.getName() != null) {
        AdditionalIpV6RouteGroup someone
          = additionalIpV6RouteGroupDao.findByName(model.getName());
        if (someone != null && !someone.getId().equals(model.getId())) {
          uniqueColumn = getText("entityGroup.name.label");
          addFieldError("name", getText("need.to.be.unique"));
          return;
        }
      }
    }

    if (contextName.equals("additional-ip-v6-route-group-create")) {
      if (
           model.getName() != null
        && additionalIpV6RouteGroupDao.findByName(model.getName()) != null
      ) {
        uniqueColumn = getText("entityGroup.name.label");
        addFieldError("name", getText("need.to.be.unique"));
        return;
      }
    }
  }

}
