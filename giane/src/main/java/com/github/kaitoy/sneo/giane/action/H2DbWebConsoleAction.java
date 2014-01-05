/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.action;

import java.net.URL;
import java.util.Map;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.h2.tools.Server;
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.github.kaitoy.sneo.giane.action.message.H2DbWebConsoleMessage;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class H2DbWebConsoleAction extends ActionSupport
implements ApplicationAware, H2DbWebConsoleMessage, FormMessage {

  /**
   *
   */
  private static final long serialVersionUID = 830216491132312506L;

  private Integer port;
  private boolean h2DbWebServerRunning;
  private String url;
  private Map<String, Object> application;
  private static Object lock = new Object();

  public Integer getPort() {
    return port;
  }

  @RequiredFieldValidator(
    key = "RequiredFieldValidator.error",
    shortCircuit = true
  )
  @IntRangeFieldValidator(
    key = "IntRangeFieldValidator.error.min.max",
    min = "1",
    max = "65535",
    shortCircuit = true
  )
  public void setPort(Integer port) {
    this.port = port;
  }

  public boolean isH2DbWebServerRunning() {
    return h2DbWebServerRunning;
  }

  public String getUrl() {
    return url;
  }

  public void setApplication(Map<String, Object> application) {
    this.application = application;
  }

  @Action(
    value = "h2-db-web-console-tab-content",
    results = { @Result(name = "tab", location = "h2-db-web-console-tab-content.jsp") }
  )
  @SkipValidation
  public String tab() throws Exception {
    Server server = (Server)application.get("h2DbWebServer");
    if (server != null && server.isRunning(false)) {
      h2DbWebServerRunning = true;
    }
    else {
      h2DbWebServerRunning = false;
    }

    return "tab";
  }

  @Action(
    value = "h2-db-web-console-start",
    results = { @Result(name = "sucess", location = "h2-db-web-console-stop.jsp") }
  )
  public String start() throws Exception {
    synchronized (lock) {
      Server server = (Server)application.get("h2DbWebServer");
      if (server != null && server.isRunning(false)) {
        URL urlObj = new URL(ServletActionContext.getRequest().getRequestURL().toString());
        StringBuffer sb = new StringBuffer();
        sb.append(urlObj.getProtocol())
          .append("://")
          .append(urlObj.getHost())
          .append(":")
          .append(server.getPort())
          .append("/");
        url = sb.toString();
      }
      else {
        server = Server.createWebServer(
          "-webPort", port.toString(),
          "-baseDir", ".",
          "-webAllowOthers",
          "-webDaemon"
        );
        server.start();
        application.put("h2DbWebServer", server);

        URL urlObj = new URL(ServletActionContext.getRequest().getRequestURL().toString());
        StringBuffer sb = new StringBuffer();
        sb.append(urlObj.getProtocol())
          .append("://")
          .append(urlObj.getHost())
          .append(":")
          .append(port)
          .append("/");
        url = sb.toString();
      }
    }

    return "sucess";
  }

  @Action(
    value = "h2-db-web-console-stop",
    results = { @Result(name = "sucess", location = "h2-db-web-console-start.jsp") }
  )
  @SkipValidation
  public String stop() throws Exception {
    synchronized (lock) {
      Server server = (Server)application.get("h2DbWebServer");
      if (server != null && server.isRunning(false)) {
        server.stop();
      }
    }

    return "sucess";
  }

}
