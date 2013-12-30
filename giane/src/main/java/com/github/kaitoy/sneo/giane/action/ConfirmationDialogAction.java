/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import com.github.kaitoy.sneo.giane.action.message.ConfirmationDialogMessage;
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class ConfirmationDialogAction extends ActionSupport
implements ConfirmationDialogMessage, FormMessage {

  /**
   *
   */
  private static final long serialVersionUID = 8986143503120527708L;

  private String deletingIdList;
  private int numDeletingItems;

  public void setDeletingIdList(String deletingIdList) {
    this.deletingIdList = deletingIdList;
  }

  public int getNumDeletingItems() {
    return numDeletingItems;
  }

  @Override
  @Action(
    results = {
      @Result(name = "success", location = "confirmation-dialog.jsp")
    }
  )
  public String execute() throws Exception {
    numDeletingItems = deletingIdList.split(",").length;
    return "success";
  }

  @Override
  public void validate() {
    if (deletingIdList == null || deletingIdList.length() == 0) {
      addActionError(getText("select.a.row"));
      return;
    }
  }

}
