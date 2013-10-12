<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div id="breadcrumbs">
  <s:url var="config_top_url" action="config-top" />

  <sj:a
    href="%{config_top_url}"
    targets="main"
    replaceTarget="false"
    button="false"
    indicator="main_indicator"
  >
    [<s:text name="breadcrumbs.config.top" />]
  </sj:a>
  &nbsp;&gt;&nbsp;[<s:text name="breadcrumbs.network.configuration" />: <s:property value="#parameters.network_name" />]
</div>

<sj:tabbedpanel id="network_config_tabs" animate="true" cssClass="tabs">
  <sj:tab id="node_tab" target="node_tab_content" label="%{getText('network.config.node.tab.label')}" />
  <s:url var="node_tab_content_url" action="node-tab-content">
    <s:param name="network_id" value="%{#parameters.network_id}" />
  </s:url>
  <sj:div id="node_tab_content" href="%{node_tab_content_url}" indicator="node_tab_indicator" cssClass="tab-content" />

  <sj:tab id="l2Connection_tab" target="l2Connection_tab_content" label="%{getText('network.config.l2Connection.tab.label')}" />
  <s:url var="l2Connection_tab_content_url" action="l2-connection-tab-content">
    <s:param name="network_id" value="%{#parameters.network_id}" />
  </s:url>
  <sj:div id="l2Connection_tab_content" href="%{l2Connection_tab_content_url}" indicator="l2Connection_tab_indicator" cssClass="tab-content" />
</sj:tabbedpanel>

<img id="node_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="tab-indicator" />
<img id="l2Connection_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="tab-indicator" />

