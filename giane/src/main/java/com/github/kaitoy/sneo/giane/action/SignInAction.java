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
  private static Object lock = new Object();

  public void setApplication(Map<String, Object> application) {
    this.application = application;
  }

  @Override
  @Action(
    results = { @Result(name = "success", location = "sign-in.jsp") }
  )
  public String execute() throws Exception {
    synchronized (lock) {
      if (application.get("jmxAgent") == null) {
        int httpPort = getPortFromSystemProperty("com.github.kaitoy.sneo.giane.jmx.httpPort", 8090);
        int rmiPort = getPortFromSystemProperty("com.github.kaitoy.sneo.giane.jmx.rmiPort", 10099);
        HttpJmxAgent jmxAgent = new HttpJmxAgent(httpPort, rmiPort);
        application.put("jmxAgent", jmxAgent);
        jmxAgent.start();
      }
    }

    return "success";
  }

  private int getPortFromSystemProperty(String key, int dflt) {
    int port;
    try {
      port = Integer.parseInt(System.getProperty(key));
      if (port < 1 || port > 65535) {
        port = dflt;
      }
    } catch (Exception e) {
      port = dflt;
    }
    return port;
  }

}
