<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<sj:tabbedpanel id="simulatorHome_tabs" animate="true" cssClass="giane-tab-container">
  <sj:tab
    id="startSimiulator_tab"
    target="startSimiulator_tab_content"
    label="%{getText('simulatorHome.startSimulator.tab.label')}"
  />
  <s:url var="startSimiulator_tab_content_url" action="start-simulator-tab-content" />
  <sj:div
    id="startSimiulator_tab_content"
    href="%{startSimiulator_tab_content_url}"
    indicator="startSimiulator_tab_indicator"
    cssClass="giane-tab-content"
  />
  
  <sj:tab
    id="jmxConsole_tab"
    target="jmxConsole_tab_content"
    label="%{getText('simulatorHome.jmxConsole.tab.label')}"
  />
  <s:url
    var="jmxConsole_tab_content_url"
    action="jmx-console-tab-content"
  />
  <sj:div
    id="jmxConsole_tab_content"
    href="%{jmxConsole_tab_content_url}"
    indicator="jmxConsole_tab_indicator"
    cssClass="giane-tab-content"
  />
</sj:tabbedpanel>

<img
  id="startSimiulator_tab_indicator"
  src="images/loading_big.gif"
  alt="Loading..."
  style="display: none;"
  class="giane-tab-indicator"
/>
<img
  id="jmxConsole_tab_indicator"
  src="images/loading_big.gif"
  alt="Loading..."
  style="display: none;"
  class="giane-tab-indicator"
/>
