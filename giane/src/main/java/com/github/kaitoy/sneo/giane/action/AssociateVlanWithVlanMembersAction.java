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

import com.github.kaitoy.sneo.giane.model.Vlan;
import com.github.kaitoy.sneo.giane.model.VlanMember;
import com.github.kaitoy.sneo.giane.model.dao.VlanDao;
import com.github.kaitoy.sneo.giane.model.dao.VlanMemberDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class AssociateVlanWithVlanMembersAction extends ActionSupport {

  /**
   *
   */
  private static final long serialVersionUID = 433422932342221678L;

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

  @Action(
    results = {
      @Result(name = "success", location = "dialog.jsp"),
      @Result(name = "noChange", location = "dialog.jsp")
    }
  )
  public String execute() throws Exception {
    if (idList.equals("undefined")) {
      dialogTitleKey
        = "associate.vlan.with.vlanMembers.noChange.dialog.title";
      dialogTextKey
        = "associate.vlan.with.vlanMembers.noChange.dialog.text";
      return "noChange";
    }

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
        = "associate.vlan.with.vlanMembers.noChange.dialog.title";
      dialogTextKey
        = "associate.vlan.with.vlanMembers.noChange.dialog.text";
      return "noChange";
    }

    vlan.setVlanMembers(members);
    vlanDao.update(vlan);

    dialogTitleKey
      = "associate.vlan.with.vlanMembers.success.dialog.title";
    dialogTextKey
      = "associate.vlan.with.vlanMembers.success.dialog.text";
    return "success";
  }

}
