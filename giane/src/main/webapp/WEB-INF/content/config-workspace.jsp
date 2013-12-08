<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<ol id="breadcrumbs"><li class="next-breadcrumb" /></ol>

<s:url var="configHome_url" action="config-home" />
<sj:div
  id="config_main"
  href="%{configHome_url}"
  indicator="config_main_indicator"
  onCompleteTopics="configMainPaneCompleted"
  cssClass="giane-main"
 />

<img id="config_main_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="main-indicator" />
