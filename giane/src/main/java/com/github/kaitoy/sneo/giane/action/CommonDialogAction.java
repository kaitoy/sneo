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
import org.apache.struts2.interceptor.validation.SkipValidation;
import com.github.kaitoy.sneo.giane.action.message.DialogMessage;
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class CommonDialogAction extends ActionSupport
implements DialogMessage, FormMessage {

  /**
   *
   */
  private static final long serialVersionUID = 3798270545666678810L;

  private String dialogTitleKey;
  private String dialogTextKey;

  public String getDialogTitleKey() {
    return dialogTitleKey;
  }

  public void setDialogTitleKey(String dialogTitleKey) {
    this.dialogTitleKey = dialogTitleKey;
  }

  public String getDialogTextKey() {
    return dialogTextKey;
  }

  public void setDialogTextKey(String dialogTextKey) {
    this.dialogTextKey = dialogTextKey;
  }

  @Override
  @Action(results = { @Result(name = "success", location = "dialog.jsp") })
  @SkipValidation
  public String execute() throws Exception {
    return "success";
  }

}
