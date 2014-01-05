<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<sj:accordion autoHeight="false">
  <sj:accordionItem title="%{getText('accordion.item.label.simulator')}">
    <div class="workspace-root">
      <ol id="simulator_<s:property value="breadcrumbsIdSuffix" />" class="breadcrumbs"><li class="next-breadcrumb" /></ol>
      
      <s:url var="simulatorHome_url" action="simulator-home" escapeAmp="false">
        <s:param name="breadcrumbsId" value="'simulator_' + breadcrumbsIdSuffix" />
      </s:url>
      <sj:div
        id="simulator_main"
        href="%{simulatorHome_url}"
        indicator="simulator_main_indicator"
        onCompleteTopics="mainPaneCompleted"
        cssClass="giane-main"
       />
      
      <img id="simulator_main_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="main-indicator"/>
    </div>
  </sj:accordionItem>

  <sj:accordionItem title="%{getText('accordion.item.label.config')}">
    <div class="workspace-root">
      <ol id="config_<s:property value="breadcrumbsIdSuffix" />" class="breadcrumbs"><li class="next-breadcrumb" /></ol>
      
      <s:url var="configHome_url" action="config-home" escapeAmp="false">
        <s:param name="breadcrumbsId" value="'config_' + breadcrumbsIdSuffix" />
      </s:url>
      <sj:div
        id="config_main"
        href="%{configHome_url}"
        indicator="config_main_indicator"
        onCompleteTopics="mainPaneCompleted"
        cssClass="giane-main"
       />
      
      <img id="config_main_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="main-indicator" />
    </div>
  </sj:accordionItem>

  <sj:accordionItem title="%{getText('accordion.item.label.tool')}">
    <div class="workspace-root">
      <ol id="tool_<s:property value="breadcrumbsIdSuffix" />" class="breadcrumbs"><li class="next-breadcrumb" /></ol>
      
      <s:url var="toolHome_url" action="tool-home" escapeAmp="false">
        <s:param name="breadcrumbsId" value="'tool_' + breadcrumbsIdSuffix" />
      </s:url>
      <sj:div
        id="tool_main"
        href="%{toolHome_url}"
        indicator="tool_main_indicator"
        onCompleteTopics="mainPaneCompleted"
        cssClass="giane-main"
       />
      
      <img id="tool_main_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="main-indicator"/>
    </div>
  </sj:accordionItem>
</sj:accordion>
