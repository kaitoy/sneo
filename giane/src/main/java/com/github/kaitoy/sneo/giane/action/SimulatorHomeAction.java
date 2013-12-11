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
import com.github.kaitoy.sneo.giane.action.message.SimulatorHomeMessage;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class SimulatorHomeAction extends ActionSupport implements SimulatorHomeMessage {

  /**
   *
   */
  private static final long serialVersionUID = -370332765746898506L;

  @Override
  @Action(
    results = {
      @Result(name = "success", location = "simulator-home.jsp")
    }
  )
  @SkipValidation
  public String execute() throws Exception {
    return "success";
  }

}
