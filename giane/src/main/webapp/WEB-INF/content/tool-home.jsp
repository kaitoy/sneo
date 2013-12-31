<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="breadcrumb-label" style="display: none;">
  <s:label
    title="%{getText('breadcrumbs.toolHome.title')}"
    value="%{getText('breadcrumbs.toolHome.label')}"
  />
</div>

<sj:tabbedpanel id="toolHome_tabs" animate="true" cssClass="giane-tab-container">
  <sj:tab target="h2DbWebConsole_tab_content" label="%{getText('toolHome.h2DbWebConsole.tab.label')}" />
  <s:url var="h2DbWebConsole_tab_content_url" action="h2-db-web-console-tab-content" />
  <sj:div id="h2DbWebConsole_tab_content" href="%{h2DbWebConsole_tab_content_url}" indicator="h2DbWebConsole_tab_indicator" cssClass="giane-tab-content" />
</sj:tabbedpanel>

<img id="h2DbWebConsole_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="giane-tab-indicator" />
