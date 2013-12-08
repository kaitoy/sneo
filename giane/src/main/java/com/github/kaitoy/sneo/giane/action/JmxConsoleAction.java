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
import com.github.kaitoy.sneo.giane.action.message.JmxConsoleMessage;
import com.github.kaitoy.sneo.jmx.HttpJmxAgent;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class JmxConsoleAction extends ActionSupport
implements ApplicationAware, JmxConsoleMessage {

  /**
   *
   */
  private static final long serialVersionUID = 154325051805252490L;

  private String url;
  private Map<String, Object> application;

  public String getUrl() {
    return url;
  }

  public void setApplication(Map<String, Object> application) {
    this.application = application;
  }

  @Action(
    value = "jmx-console-tab-content",
    results = { @Result(name = "tab", location = "jmx-console-tab-content.jsp") }
  )
  @SkipValidation
  public String tab() throws Exception {
    URL urlObj = new URL(ServletActionContext.getRequest().getRequestURL().toString());
    StringBuffer sb = new StringBuffer();
    sb.append(urlObj.getProtocol())
      .append("://")
      .append(urlObj.getHost())
      .append(":")
      .append(((HttpJmxAgent)application.get("jmxAgent")).getJmxPort())
      .append("/");
    url = sb.toString();
    return "tab";
  }

}
