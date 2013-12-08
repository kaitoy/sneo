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
import com.github.kaitoy.sneo.giane.action.message.ToolHomeMessage;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class ToolHomeAction extends ActionSupport implements ToolHomeMessage {

  /**
   *
   */
  private static final long serialVersionUID = 8710222172547190331L;

  @Override
  @Action(
    results = {
      @Result(name = "success", location = "tool-home.jsp")
    }
  )
  @SkipValidation
  public String execute() throws Exception {
    return "success";
  }

}
