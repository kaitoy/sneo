/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import com.github.kaitoy.sneo.giane.action.message.VlanMemberMessage;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class VlanMemberAction extends ActionSupport implements VlanMemberMessage {

  /**
   *
   */
  private static final long serialVersionUID = 695790906346600143L;

  @Action(
    value = "vlan-associated-vlan-member-grid-box",
    results = { @Result(name = "grid", location = "vlan-associated-vlan-member-grid.jsp")}
  )
  @SkipValidation
  public String vlanAssociatedGrid() throws Exception {
    return "grid";
  }

  @Action(
    value = "vlan-unassociated-vlan-member-grid-box",
    results = { @Result(name = "grid", location = "vlan-unassociated-vlan-member-grid.jsp")}
  )
  @SkipValidation
  public String vlanUnassociatedGrid() throws Exception {
    return "grid";
  }

}
