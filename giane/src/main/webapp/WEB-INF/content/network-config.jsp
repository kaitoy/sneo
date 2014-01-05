<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="breadcrumb-label" style="display: none;">
  <s:label
    title="%{getText('breadcrumbs.network.configuration.title')}"
    value="%{getText('breadcrumbs.network.configuration.label')}"
  />
</div>

<div class="breadcrumb-link" style="display: none;">
  <s:url var="back_to_network_config_url" action="back-to-network-config" escapeAmp="false">
    <s:param name="breadcrumbsId" value="%{#parameters.breadcrumbsId}" />
    <s:param name="network_id" value="%{network_id}" />
    <s:param name="network_name" value="%{network_name}" />
  </s:url>
  <sj:a
    id="back_to_network_config_button"
    name="back_to_network_config_button"
    href="%{back_to_network_config_url}"
    title="%{getText('breadcrumbs.network.configuration.title')}"
    targets="config_main"
    replaceTarget="false"
    button="true"
    indicator="config_main_indicator"
    onBeforeTopics="mainPaneGoingBack"
    onCompleteTopics="mainPaneCompleted"
  >
    <s:text name="breadcrumbs.network.configuration.label" />
  </sj:a>
</div>

<sj:tabbedpanel id="network_config_tabs" animate="true" cssClass="giane-tab-container" selectedTab="%{selectedTab}">
  <sj:tab id="node_tab" target="node_tab_content" label="%{getText('network.config.node.tab.label')}" />
  <s:url var="node_tab_content_url" action="node-tab-content" escapeAmp="false">
    <s:param name="network_id" value="%{network_id}" />
    <s:param name="tabIndex" value="0" />
    <s:param name="breadcrumbsId" value="%{#parameters.breadcrumbsId}" />
  </s:url>
  <sj:div id="node_tab_content" href="%{node_tab_content_url}" indicator="node_tab_indicator" cssClass="giane-tab-content" />

  <sj:tab id="l2Connection_tab" target="l2Connection_tab_content" label="%{getText('network.config.l2Connection.tab.label')}" />
  <s:url var="l2Connection_tab_content_url" action="l2-connection-tab-content" escapeAmp="false">
    <s:param name="network_id" value="%{network_id}" />
    <s:param name="tabIndex" value="1" />
    <s:param name="breadcrumbsId" value="%{#parameters.breadcrumbsId}" />
  </s:url>
  <sj:div id="l2Connection_tab_content" href="%{l2Connection_tab_content_url}" indicator="l2Connection_tab_indicator" cssClass="giane-tab-content" />
</sj:tabbedpanel>

<img id="node_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="giane-tab-indicator" />
<img id="l2Connection_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="giane-tab-indicator" />

