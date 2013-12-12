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
import com.github.kaitoy.sneo.giane.action.message.SignInMessage;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class SignInAction extends ActionSupport implements SignInMessage {

  /**
   *
   */
  private static final long serialVersionUID = -7647074077177570168L;

  @Override
  @Action(
    results = { @Result(name = "success", location = "sign-in.jsp") }
  )
  public String execute() throws Exception {
    return "success";
  }

}
