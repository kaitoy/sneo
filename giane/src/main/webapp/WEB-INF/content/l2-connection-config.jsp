<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="breadcrumb-label" style="display: none;">
  <s:label
    title="%{getText('breadcrumbs.l2Connection.configuration.title')}"
    value="%{getText('breadcrumbs.l2Connection.configuration.label')}"
  />
</div>

<sj:tabbedpanel id="l2Connection_config_tabs" animate="true" cssClass="giane-tab-container" selectedTab="%{selectedTab}">
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
    <s:param name="network_id" value="%{network_id}" />
    <s:param name="l2Connection_id" value="%{l2Connection_id}" />
    <s:param name="l2Connection_name" value="%{l2Connection_name}" />
    <s:param name="tabIndex" value="0" />
  </s:url>
  <sj:div
    id="associate_l2Connection_with_physicalNetworkInterfaces_tab_content"
    href="%{associate_l2Connection_with_physicalNetworkInterfaces_tab_content_url}"
    indicator="associate_l2Connection_with_physicalNetworkInterfaces_tab_indicator"
    cssClass="giane-tab-content clearfix"
  />
</sj:tabbedpanel>

<img
  id="associate_l2Connection_with_physicalNetworkInterfaces_tab_indicator"
  src="images/loading_big.gif"
  alt="Loading..."
  style="display: none;"
  class="giane-tab-indicator"
/>

