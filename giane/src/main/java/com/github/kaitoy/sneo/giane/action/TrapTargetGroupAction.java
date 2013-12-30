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
import com.github.kaitoy.sneo.giane.action.message.AssociateActionMessage;
import com.github.kaitoy.sneo.giane.action.message.BreadCrumbsMessage;
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.github.kaitoy.sneo.giane.action.message.TrapTargetGroupMessage;
import com.github.kaitoy.sneo.giane.interceptor.GoingBackward;
import com.github.kaitoy.sneo.giane.interceptor.GoingForward;
import com.github.kaitoy.sneo.giane.model.TrapTargetGroup;
import com.github.kaitoy.sneo.giane.model.dao.TrapTargetGroupDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class TrapTargetGroupAction
extends ActionSupport
implements ModelDriven<TrapTargetGroup>, ParameterAware, FormMessage,
  TrapTargetGroupMessage, BreadCrumbsMessage, AssociateActionMessage {

  /**
   *
   */
  private static final long serialVersionUID = 5313821557470289182L;
  private TrapTargetGroup model = new TrapTargetGroup();
  private Map<String, String[]> parameters;
  private TrapTargetGroupDao trapTargetGroupDao;
  private String uniqueColumn;
  private String deletingIdList;

  public TrapTargetGroup getModel() { return model; }

  @VisitorFieldValidator(appendPrefix = true)
  public void setModel(TrapTargetGroup model) { this.model = model; }

  public void setParameters(Map<String, String[]> parameters) {
    this.parameters = parameters;
  }

  // for DI
  public void setTrapTargetGroupDao(TrapTargetGroupDao trapTargetGroupDao) {
    this.trapTargetGroupDao = trapTargetGroupDao;
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
    valueMap.put("trapTargetGroup_id", model.getId());
    valueMap.put("trapTargetGroup_name", model.getName());
    stack.push(valueMap);

    return "config";
  }

  @Action(
    value = "back-to-trap-target-group-config",
    results = { @Result(name = "config", location = "trap-target-group-config.jsp")}
  )
  @SkipValidation
  @GoingBackward
  public String back() throws Exception {
    ValueStack stack = ActionContext.getContext().getValueStack();
    Map<String, Object> valueMap = new HashMap<String, Object>();
    valueMap.put("trapTargetGroup_id", parameters.get("trapTargetGroup_id")[0]);
    valueMap.put("trapTargetGroup_name", parameters.get("trapTargetGroup_name")[0]);
    stack.push(valueMap);

    return "config";
  }

  @Action(
    value = "trap-target-group-tab-content",
    results = { @Result(name = "tab", location = "trap-target-group-tab-content.jsp")}
  )
  @SkipValidation
  public String tab() throws Exception {
    return "tab";
  }

  @Action(
    value = "associate-trap-target-group-with-trap-targets-tab-content",
    results = {
      @Result(
        name = "tab",
        location = "associate-trap-target-group-with-trap-targets-tab-content.jsp"
      )
    }
  )
  @SkipValidation
  public String associateTrapTargetsTab() throws Exception {
    return "tab";
  }

  @Action(
    value = "snmp-agent-associated-trap-target-group-grid-box",
    results = {
      @Result(
        name = "grid",
        location = "snmp-agent-associated-trap-target-group-grid.jsp"
      )
    }
  )
  @SkipValidation
  public String snmpAgentAssociatedGrid() throws Exception {
    return "grid";
  }

  @Action(
    value = "snmp-agent-unassociated-trap-target-group-grid-box",
    results = {
      @Result(
        name = "grid",
        location = "snmp-agent-unassociated-trap-target-group-grid.jsp"
      )
    }
  )
  @SkipValidation
  public String snmpAgentUnassociatedGrid() throws Exception {
    return "grid";
  }

  @Action(
    value = "trap-target-group-create",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  public String create() throws Exception {
    trapTargetGroupDao.create(model);
    return "success";
  }

  @Action(
    value = "trap-target-group-update",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  public String update() throws Exception {
    TrapTargetGroup update = trapTargetGroupDao.findByKey(model.getId());
    update.setName(model.getName());
    update.setDescr(model.getDescr());
    trapTargetGroupDao.update(update);

    return "success";
  }

  @Action(
    value = "trap-target-group-delete",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  @SkipValidation
  public String delete() throws Exception {
    List<TrapTargetGroup> deletingList = new ArrayList<TrapTargetGroup>();
    for (String idStr: deletingIdList.split(",")) {
      deletingList.add(trapTargetGroupDao.findByKey(Integer.valueOf(idStr)));
    }
    trapTargetGroupDao.delete(deletingList);
    return "success";
  }

  @Override
  public void validate() {
    String contextName = ActionContext.getContext().getName();

    if (contextName.equals("trap-target-group")) {
      if (model.getId() == null) {
        addActionError(getText("select.a.row"));
        return;
      }
    }

    if (contextName.equals("trap-target-group-update")) {
      if (model.getId() == null) {
        addActionError(getText("select.a.row"));
        return;
      }

      if (model.getName() != null) {
        TrapTargetGroup someone
          = trapTargetGroupDao.findByName(model.getName());
        if (someone != null && !someone.getId().equals(model.getId())) {
          uniqueColumn = getText("trapTargetGroup.name.label");
          addActionError(getText("need.to.be.unique"));
          return;
        }
      }
    }

    if (contextName.equals("trap-target-group-create")) {
      if (
           model.getName() != null
        && trapTargetGroupDao.findByName(model.getName()) != null
      ) {
        uniqueColumn = getText("trapTargetGroup.name.label");
        addActionError(getText("need.to.be.unique"));
        return;
      }
    }
  }

}
