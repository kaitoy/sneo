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
import com.github.kaitoy.sneo.giane.action.message.BreadCrumbsMessage;
import com.github.kaitoy.sneo.giane.action.message.HomeMessage;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class HomeAction extends ActionSupport
implements HomeMessage, BreadCrumbsMessage {

  /**
   *
   */
  private static final long serialVersionUID = -5818597703027952674L;

  @Override
  @Action(
    results = {
      @Result(name = "success", location = "home.jsp")
    }
  )
  @SkipValidation
  public String execute() throws Exception {
    return "success";
  }

}
