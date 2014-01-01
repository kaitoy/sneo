/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
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
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.kaitoy.sneo.giane.action.message.AssociateActionMessage;
import com.github.kaitoy.sneo.giane.action.message.DialogMessage;
import com.github.kaitoy.sneo.giane.action.message.VlanMemberMessage;
import com.github.kaitoy.sneo.giane.model.Vlan;
import com.github.kaitoy.sneo.giane.model.VlanMember;
import com.github.kaitoy.sneo.giane.model.dao.VlanDao;
import com.github.kaitoy.sneo.giane.model.dao.VlanMemberDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class AssociateVlanWithVlanMembersAction extends ActionSupport
implements AssociateActionMessage, VlanMemberMessage, DialogMessage {

  /**
   *
   */
  private static final long serialVersionUID = 433422932342221678L;

  private static final Logger logger
    = LoggerFactory.getLogger(AssociateVlanWithVlanMembersAction.class);

  private VlanMemberDao vlanMemberDao;
  private VlanDao vlanDao;
  private String idList;
  private String dialogTitleKey;
  private String dialogTextKey;

  // for DI
  public void setVlanMemberDao(VlanMemberDao vlanMemberDao) {
    this.vlanMemberDao = vlanMemberDao;
  }

  // for DI
  public void setVlanDao(VlanDao vlanDao) {
    this.vlanDao = vlanDao;
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

  @Override
  @Action(
    results = {
      @Result(name = "success", location = "dialog.jsp"),
      @Result(name = "noChange", location = "dialog.jsp"),
      @Result(name = "error", location = "dialog.jsp")
    }
  )
  public String execute() throws Exception {
    if (idList.equals("undefined")) {
      dialogTitleKey
        = "dialog.title.result";
      dialogTextKey
        = "dialog.text.association.noChange";
      return "noChange";
    }

    try {
      List<VlanMember> members = new ArrayList<VlanMember>();
      if (idList != null && idList.length() != 0) {
        for (String strId: idList.split(",")) {
          members.add(vlanMemberDao.findByKey(Integer.valueOf(strId)));
        }
      }

      Map<String, Object> params = ActionContext.getContext().getParameters();
      Integer vlan_id = Integer.valueOf(((String[])params.get("vlan_id"))[0]);
      Vlan vlan = vlanDao.findByKey(vlan_id);

      boolean foundAll = true;
      for (VlanMember member: members) {
        boolean found = false;
        for (VlanMember other: vlan.getVlanMembers()) {
          if (member.getId().equals(other.getId())) {
            found = true;
            break;
          }
        }
        if (!found) {
          foundAll = false;
        }
      }

      if (foundAll && vlan.getVlanMembers().size() == members.size()) {
        dialogTitleKey
          = "dialog.title.result";
        dialogTextKey
          = "dialog.text.association.noChange";
        return "noChange";
      }

      vlan.setVlanMembers(members);
      vlanDao.update(vlan);

      dialogTitleKey
        = "dialog.title.result";
      dialogTextKey
        = "dialog.text.association.success";
      return "success";
    } catch (Exception e) {
      logger.error("An error occurred: ", e);
      dialogTitleKey = "dialog.title.result";
      dialogTextKey = "dialog.text.association.error";
      return "error";
    }
  }

  @Action(
    value = "associate-vlan-with-vlan-members-grid-box",
    results = { @Result(name = "grid", location = "associate-vlan-with-vlan-members-grid.jsp")}
  )
  @SkipValidation
  public String associationGrid() throws Exception {
    return "grid";
  }

}
