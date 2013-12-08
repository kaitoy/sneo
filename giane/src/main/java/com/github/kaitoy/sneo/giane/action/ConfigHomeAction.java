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
import com.github.kaitoy.sneo.giane.action.message.ConfigHomeMessage;
import com.github.kaitoy.sneo.giane.interceptor.GoingBackward;
import com.github.kaitoy.sneo.giane.interceptor.GoingForward;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class ConfigHomeAction extends ActionSupport
implements ConfigHomeMessage, BreadCrumbsMessage {

  /**
   *
   */
  private static final long serialVersionUID = -5818597703027952674L;

  @Override
  @Action(
    results = {
      @Result(name = "success", location = "config-home.jsp")
    }
  )
  @SkipValidation
  @GoingForward
  public String execute() throws Exception {
    return "success";
  }

  @Action(
    value = "back-to-config-home",
    results = {
      @Result(name = "success", location = "config-home.jsp")
    }
  )
  @SkipValidation
  @GoingBackward
  public String back() throws Exception {
    return "success";
  }

}
