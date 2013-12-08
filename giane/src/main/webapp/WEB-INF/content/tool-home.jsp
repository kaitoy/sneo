<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<sj:tabbedpanel id="toolHome_tabs" animate="true" cssClass="tabs">
  <sj:tab id="jmxConsole_tab" target="jmxConsole_tab_content" label="%{getText('configHome.jmxConsole.tab.label')}" />
  <s:url var="jmxConsole_tab_content_url" action="jmx-console-tab-content" escapeAmp="false">
    <s:param name="tabIndex" value="1" />
  </s:url>
  <sj:div id="jmxConsole_tab_content" href="%{jmxConsole_tab_content_url}" indicator="jmxConsole_tab_indicator" cssClass="tab-content" />
  
  <sj:tab target="h2DbWebConsole_tab_content" label="%{getText('toolHome.h2DbWebConsole.tab.label')}" />
  <s:url var="h2DbWebConsole_tab_content_url" action="h2-db-web-console-tab-content" escapeAmp="false">
    <s:param name="tabIndex" value="0" />
  </s:url>
  <sj:div id="h2DbWebConsole_tab_content" href="%{h2DbWebConsole_tab_content_url}" indicator="h2DbWebConsole_tab_indicator" cssClass="tab-content" />
</sj:tabbedpanel>

<img id="h2DbWebConsole_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="tab-indicator" />
<img id="jmxConsole_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="tab-indicator" />
