/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.action;

import java.util.Map;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import com.github.kaitoy.sneo.jmx.HttpJmxAgent;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class SignInAction extends ActionSupport {

  /**
   *
   */
  private static final long serialVersionUID = -7647074077177570168L;

  private static final Object lock = new Object();

  @Action(
    results = { @Result(name = "success", location = "sign-in.jsp") }
  )
  public String execute() throws Exception {
    @SuppressWarnings("unchecked")
    Map<String, Object> application
      = (Map<String, Object>)ActionContext.getContext().get("application");

    synchronized (lock) {
      if (application.get("jmxAgent") == null) {
        HttpJmxAgent jmxAgent = new HttpJmxAgent(8090, 10099);
        application.put("jmxAgent", jmxAgent);
        jmxAgent.start();
      }
    }

    return "success";
  }

}
