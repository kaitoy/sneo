/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class ConfigTopAction extends ActionSupport {

  /**
   *
   */
  private static final long serialVersionUID = -5818597703027952674L;

  @Action(
    results = {
      @Result(name = "success", location = "config-top.jsp")
    }
  )
  @SkipValidation
  public String execute() throws Exception {
    return "success";
  }

}
