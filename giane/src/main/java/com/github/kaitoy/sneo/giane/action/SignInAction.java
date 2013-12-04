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
import org.apache.struts2.interceptor.ApplicationAware;
import com.github.kaitoy.sneo.giane.action.message.SignInMessage;
import com.github.kaitoy.sneo.jmx.HttpJmxAgent;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class SignInAction extends ActionSupport
implements ApplicationAware, SignInMessage {

  /**
   *
   */
  private static final long serialVersionUID = -7647074077177570168L;

  private Map<String, Object> application;

  public void setApplication(Map<String, Object> application) {
    this.application = application;
  }

  @Override
  @Action(
    results = { @Result(name = "success", location = "sign-in.jsp") }
  )
  public String execute() throws Exception {
    synchronized (application) {
      if (application.get("jmxAgent") == null) {
        HttpJmxAgent jmxAgent = new HttpJmxAgent(8090, 10099);
        application.put("jmxAgent", jmxAgent);
        jmxAgent.start();
      }
    }

    return "success";
  }

}
