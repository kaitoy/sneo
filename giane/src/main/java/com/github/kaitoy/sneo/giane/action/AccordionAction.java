/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.action;

import java.rmi.server.UID;
import java.util.HashMap;
import java.util.Map;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import com.github.kaitoy.sneo.giane.action.message.AccordionMessage;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.ValueStack;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class AccordionAction extends ActionSupport
implements AccordionMessage {

  /**
   *
   */
  private static final long serialVersionUID = 1802077533332709848L;

  @Override
  @Action(
    results = { @Result(name = "success", location = "accordion.jsp") }
  )
  public String execute() throws Exception {
    ValueStack stack = ActionContext.getContext().getValueStack();
    Map<String, Object> valueMap = new HashMap<String, Object>();
    valueMap.put("breadcrumbsIdSuffix", new UID().toString());
    stack.push(valueMap);
    return "success";
  }

}
