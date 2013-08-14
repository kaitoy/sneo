/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.github.kaitoy.sneo.giane.model.TrapTarget;
import com.github.kaitoy.sneo.giane.model.TrapTargetGroup;
import com.github.kaitoy.sneo.giane.model.dao.TrapTargetDao;
import com.github.kaitoy.sneo.giane.model.dao.TrapTargetGroupDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class AssociateTrapTargetGroupWithTrapTargetsAction extends ActionSupport {

  /**
   *
   */
  private static final long serialVersionUID = 433422932342221678L;

  private TrapTargetDao trapTargetDao;
  private TrapTargetGroupDao trapTargetGroupDao;
  private String idList;
  private String dialogTitleKey;
  private String dialogTextKey;

  // for DI
  public void setTrapTargetDao(TrapTargetDao trapTargetDao) {
    this.trapTargetDao = trapTargetDao;
  }

  // for DI
  public void setTrapTargetGroupDao(TrapTargetGroupDao trapTargetGroupDao) {
    this.trapTargetGroupDao = trapTargetGroupDao;
  }

  public String getIdList() {
    return idList;
  }

  public void setIdList(String idList) {
    this.idList = idList;
  }

  public String getDialogTitleKey() {
    return dialogTitleKey;
  }

  public String getDialogTextKey() {
    return dialogTextKey;
  }

  @Action(
    results = {
      @Result(name = "success", location = "dialog.jsp"),
      @Result(name = "noChange", location = "dialog.jsp")
    }
  )
  public String execute() throws Exception {
    if (idList.equals("undefined")) {
      dialogTitleKey
        = "associate.trapTargetGroup.with.trapTargets.noChange.dialog.title";
      dialogTextKey
        = "associate.trapTargetGroup.with.trapTargets.noChange.dialog.text";
      return "noChange";
    }

    List<TrapTarget> trapTargets = new ArrayList<TrapTarget>();
    if (idList != null && idList.length() != 0) {
      for (String strId: idList.split(",")) {
        trapTargets.add(trapTargetDao.findByKey(Integer.valueOf(strId)));
      }
    }

    Map<String, Object> params = ActionContext.getContext().getParameters();
    Integer trapTargetGroup_id = Integer.valueOf(((String[])params.get("trapTargetGroup_id"))[0]);
    TrapTargetGroup trapTargetGroup = trapTargetGroupDao.findByKey(trapTargetGroup_id);

    boolean foundAll = true;
    for (TrapTarget trapTarget: trapTargets) {
      boolean found = false;
      for (TrapTarget other: trapTargetGroup.getTrapTargets()) {
        if (trapTarget.getId().equals(other.getId())) {
          found = true;
          break;
        }
      }
      if (!found) {
        foundAll = false;
      }
    }

    if (foundAll && trapTargetGroup.getTrapTargets().size() == trapTargets.size()) {
      dialogTitleKey
        = "associate.trapTargetGroup.with.trapTargets.noChange.dialog.title";
      dialogTextKey
        = "associate.trapTargetGroup.with.trapTargets.noChange.dialog.text";
      return "noChange";
    }

    trapTargetGroup.setTrapTargets(trapTargets);
    trapTargetGroupDao.update(trapTargetGroup);

    dialogTitleKey
      = "associate.trapTargetGroup.with.trapTargets.success.dialog.title";
    dialogTextKey
      = "associate.trapTargetGroup.with.trapTargets.success.dialog.text";
    return "success";
  }

}
