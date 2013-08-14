/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import com.opensymphony.xwork2.ActionSupport;

@Deprecated
public class SignOffAction extends ActionSupport {

  /**
   *
   */
  private static final long serialVersionUID = 7094063049596820079L;

  @Action(
    results = {
      @Result(
        name = "success",
        type = "chain",
        params = {
          "actionName", "index.action"
        }
      )
    }
  )
  public String execute() throws Exception {
    return "success";
  }

}
