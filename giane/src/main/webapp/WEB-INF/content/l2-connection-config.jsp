<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div id="breadcrumbs">
  <s:url var="config_top_url" action="config-top" />
  <s:url var="network_url" action="network" escapeAmp="false">
    <s:param name="network_id" value="%{#parameters.network_id}" />
    <s:param name="network_name" value="%{#parameters.network_name}" />
  </s:url>

  <sj:a
    href="%{config_top_url}"
    targets="main"
    replaceTarget="false"
    button="false"
    indicator="main_indicator"
  >
    [<s:text name="breadcrumbs.config.top" />]
  </sj:a>
  &nbsp;&gt;&nbsp;
  <sj:a
    href="%{network_url}"
    targets="main"
    replaceTarget="false"
    button="false"
    indicator="main_indicator"
  >
    [<s:text name="breadcrumbs.network.configuration" />: <s:property value="#parameters.network_name" />]
  </sj:a>
  &nbsp;&gt;&nbsp;[<s:text name="breadcrumbs.l2Connection.configuration" />: <s:property value="#parameters.l2Connection_name" />]
</div>

<sj:tabbedpanel id="l2Connection_config_tabs" animate="true" cssClass="tabs">
  <sj:tab
    id="associate_l2Connection_with_physicalNetworkInterfaces_tab"
    target="associate_l2Connection_with_physicalNetworkInterfaces_tab_content"
    label="%{getText('l2Connection.config.associate.l2Connection.with.physicalNetworkInterfaces.tab.label')}"
  />
  <s:url
    var="associate_l2Connection_with_physicalNetworkInterfaces_tab_content_url"
    action="associate-l2-connection-with-physical-network-interfaces-tab-content"
    escapeAmp="false"
  >
    <s:param name="network_id" value="%{#parameters.network_id}" />
    <s:param name="l2Connection_id" value="%{#parameters.l2Connection_id}" />
    <s:param name="l2Connection_name" value="%{#parameters.l2Connection_name}" />
  </s:url>
  <sj:div
    id="associate_l2Connection_with_physicalNetworkInterfaces_tab_content"
    href="%{associate_l2Connection_with_physicalNetworkInterfaces_tab_content_url}"
    indicator="associate_l2Connection_with_physicalNetworkInterfaces_tab_indicator"
    cssClass="tab-content"
  />
</sj:tabbedpanel>

<img
  id="associate_l2Connection_with_physicalNetworkInterfaces_tab_indicator"
  src="images/loading_big.gif"
  alt="Loading..."
  style="display:none"
  class="tab-indicator"
/>

