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
import com.github.kaitoy.sneo.giane.action.message.ExceptionMessage;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class ExceptionAction extends ActionSupport implements ExceptionMessage {

  /**
   *
   */
  private static final long serialVersionUID = -5818597703027952674L;

  private String dialogTitleKey;
  private String dialogTextKey;

  public String getDialogTitleKey() {
    return dialogTitleKey;
  }

  public String getDialogTextKey() {
    return dialogTextKey;
  }

  @Override
  @Action(
    results = { @Result(name = "error", location = "dialog.jsp") }
  )
  @SkipValidation
  public String execute() throws Exception {
    dialogTitleKey = "exception.dialog.title";
    dialogTextKey = "exception.dialog.text";
    return "error";
  }

}
