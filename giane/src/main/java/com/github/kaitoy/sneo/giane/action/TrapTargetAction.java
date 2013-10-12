/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
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
import com.github.kaitoy.sneo.giane.model.TrapTarget;
import com.github.kaitoy.sneo.giane.model.dao.TrapTargetDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class TrapTargetAction
extends ActionSupport implements ModelDriven<TrapTarget>, FormMessage {

  /**
   *
   */
  private static final long serialVersionUID = 863806087262173522L;

  private TrapTarget model = new TrapTarget();
  private TrapTargetDao trapTargetDao;
  private String uniqueColumn;

  public TrapTarget getModel() { return model; }

  @VisitorFieldValidator(appendPrefix = false)
  public void setModel(TrapTarget model) { this.model = model; }

  // for DI
  public void setTrapTargetDao(TrapTargetDao trapTargetDao) {
    this.trapTargetDao = trapTargetDao;
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
    if (parameters.get("trapTarget_id") == null) {
      parameters.put("trapTarget_id", model.getId());
      parameters.put("trapTarget_name", model.getName());
    }

    return "config";
  }

  @Action(
    value = "trap-target-tab-content",
    results = { @Result(name = "tab", location = "trap-target-tab-content.jsp")}
  )
  @SkipValidation
  public String tab() throws Exception {
    return "tab";
  }

  @Action(
    value = "trap-target-group-associated-trap-target-grid-box",
    results = { @Result(name = "grid", location = "trap-target-group-associated-trap-target-grid.jsp")}
  )
  @SkipValidation
  public String trapTargetGroupAssociatedGrid() throws Exception {
    return "grid";
  }

  @Action(
    value = "trap-target-group-unassociated-trap-target-grid-box",
    results = { @Result(name = "grid", location = "trap-target-group-unassociated-trap-target-grid.jsp")}
  )
  @SkipValidation
  public String trapTargetGroupUnassociatedGrid() throws Exception {
    return "grid";
  }

  @Action(
    value = "trap-target-create",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  public String create() throws Exception {
    trapTargetDao.create(model);
    return "success";
  }

  @Action(
    value = "trap-target-update",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  public String update() throws Exception {
    TrapTarget update = trapTargetDao.findByKey(model.getId());
    update.setName(model.getName());
    update.setAddress(model.getAddress());
    update.setPort(model.getPort());
    update.setDescr(model.getDescr());
    trapTargetDao.update(update);

    return "success";
  }

  @Override
  public void validate() {
    String contextName = ActionContext.getContext().getName();

    if (contextName.equals("trap-target-update")) {
      if (model.getId() == null) {
        addActionError(getText("select.a.row"));
        return;
      }

      if (model.getName() != null) {
        TrapTarget someone
          = trapTargetDao.findByName(model.getName());
        if (someone != null && !someone.getId().equals(model.getId())) {
          uniqueColumn = getText("trapTarget.name.label");
          addActionError(getText("need.to.be.unique"));
          return;
        }
      }
    }

    if (contextName.equals("trap-target-create")) {
      if (
           model.getName() != null
        && trapTargetDao.findByName(model.getName()) != null
      ) {
        uniqueColumn = getText("trapTarget.name.label");
        addActionError(getText("need.to.be.unique"));
        return;
      }
    }
  }

}
