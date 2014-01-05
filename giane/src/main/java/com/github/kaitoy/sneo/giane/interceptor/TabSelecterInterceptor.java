/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.interceptor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.util.ValueStack;

public class TabSelecterInterceptor extends AbstractInterceptor {

  /**
   *
   */
  private static final long serialVersionUID = 2344807262906728104L;

  @Override
  public String intercept(ActionInvocation invocation) throws Exception {
    Object action = invocation.getAction();
    Method method = action.getClass().getMethod(invocation.getProxy().getMethod());

    if (
         method.isAnnotationPresent(GoingForward.class)
      || method.isAnnotationPresent(GoingBackward.class)
    ) {
      ValueStack stack = ActionContext.getContext().getValueStack();
      Map<String, Object> session = ActionContext.getContext().getSession();
      Map<String, Object> valueMap = new HashMap<String, Object>();
      Map<String, Object> parameters = ActionContext.getContext().getParameters();
      String breadcrumsId = ((String[])parameters.get("breadcrumbsId"))[0];

      if (method.isAnnotationPresent(GoingForward.class)) {
        @SuppressWarnings("unchecked")
        Map<String, Map<String, String>> selectedTabMaps
          = (Map<String, Map<String, String>>)session.get("selectedTabMaps");
        if (selectedTabMaps == null) {
          selectedTabMaps = new ConcurrentHashMap<String, Map<String, String>>();
          session.put("selectedTabMaps", selectedTabMaps);
        }

        String currentAction = (String)session.get("currentAction");
        String[] tabIndex = (String[])parameters.get("tabIndex");
        if (currentAction != null && tabIndex != null && tabIndex.length != 0) {
          Map<String, String> selectedTabMap = selectedTabMaps.get(breadcrumsId);
          if (selectedTabMap == null) {
            selectedTabMap = new ConcurrentHashMap<String, String>();
            selectedTabMaps.put(breadcrumsId, selectedTabMap);
          }
          selectedTabMap.put(currentAction, tabIndex[0]);
        }

        valueMap.put("selectedTab", "0");
      }
      else if (method.isAnnotationPresent(GoingBackward.class)) {
        @SuppressWarnings("unchecked")
        Map<String, Map<String, String>> selectedTabMaps
          = (Map<String, Map<String, String>>)session.get("selectedTabMaps");
        Map<String, String> selectedTabMap = selectedTabMaps.get(breadcrumsId);
        String selectedTab = selectedTabMap.get(action.getClass().getName());
        valueMap.put("selectedTab", selectedTab);
      }

      stack.push(valueMap);
      session.put("currentAction", action.getClass().getName());
    }

    return invocation.invoke();
  }

}
