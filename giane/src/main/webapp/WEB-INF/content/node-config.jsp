<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="breadcrumb-label" style="display: none;">
  <s:label
    title="%{getText('breadcrumbs.node.configuration.title')}"
    value="%{getText('breadcrumbs.node.configuration.label')}"
  />
</div>

<div class="breadcrumb-link" style="display: none;">
  <s:url var="back_to_node_config_url" action="back-to-node-config" escapeAmp="false">
    <s:param name="breadcrumbsId" value="%{#parameters.breadcrumbsId}" />
    <s:param name="node_id" value="%{node_id}" />
    <s:param name="node_name" value="%{node_name}" />
  </s:url>
  <sj:a
    id="back_to_node_config_button"
    href="%{back_to_node_config_url}"
    title="%{getText('breadcrumbs.node.configuration.title')}"
    targets="config_main"
    replaceTarget="false"
    button="true"
    indicator="config_main_indicator"
    onBeforeTopics="mainPaneGoingBack"
    onCompleteTopics="mainPaneCompleted"
  >
    <s:text name="breadcrumbs.node.configuration.label" />
  </sj:a>
</div>

<sj:tabbedpanel id="node_config_tabs" animate="true" cssClass="giane-tab-container" selectedTab="%{selectedTab}">
  <sj:tab id="snmpAgent_tab" target="snmpAgent_tab_content" label="%{getText('node.config.snmpAgent.tab.label')}" />
  <s:url var="snmpAgent_tab_content_url" action="snmp-agent-tab-content" escapeAmp="false">
    <s:param name="node_id" value="%{node_id}" />
    <s:param name="tabIndex" value="0" />
    <s:param name="breadcrumbsId" value="%{#parameters.breadcrumbsId}" />
  </s:url>
  <sj:div id="snmpAgent_tab_content" href="%{snmpAgent_tab_content_url}" indicator="snmpAgent_tab_indicator" cssClass="giane-tab-content" />

  <sj:tab id="physicalNetworkInterface_tab" target="physicalNetworkInterface_tab_content" label="%{getText('node.config.physicalNetworkInterface.tab.label')}" />
  <s:url var="physicalNetworkInterface_tab_content_url" action="physical-network-interface-tab-content" escapeAmp="false">
    <s:param name="node_id" value="%{node_id}" />
    <s:param name="tabIndex" value="1" />
    <s:param name="breadcrumbsId" value="%{#parameters.breadcrumbsId}" />
  </s:url>
  <sj:div id="physicalNetworkInterface_tab_content" href="%{physicalNetworkInterface_tab_content_url}" indicator="physicalNetworkInterface_tab_indicator" cssClass="giane-tab-content" />

  <sj:tab id="realNetworkInterface_tab" target="realNetworkInterface_tab_content" label="%{getText('node.config.realNetworkInterface.tab.label')}" />
  <s:url var="realNetworkInterface_tab_content_url" action="real-network-interface-tab-content" escapeAmp="false">
    <s:param name="node_id" value="%{node_id}" />
    <s:param name="node_name" value="%{node_name}" />
    <s:param name="tabIndex" value="2" />
    <s:param name="breadcrumbsId" value="%{#parameters.breadcrumbsId}" />
  </s:url>
  <sj:div id="realNetworkInterface_tab_content" href="%{realNetworkInterface_tab_content_url}" indicator="realNetworkInterface_tab_indicator" cssClass="giane-tab-content" />

  <sj:tab id="vlan_tab" target="vlan_tab_content" label="%{getText('node.config.vlan.tab.label')}" />
  <s:url var="vlan_tab_content_url" action="vlan-tab-content" escapeAmp="false">
    <s:param name="node_id" value="%{node_id}" />
    <s:param name="tabIndex" value="3" />
    <s:param name="breadcrumbsId" value="%{#parameters.breadcrumbsId}" />
  </s:url>
  <sj:div id="vlan_tab_content" href="%{vlan_tab_content_url}" indicator="vlan_tab_indicator" cssClass="giane-tab-content" />

  <sj:tab id="lag_tab" target="lag_tab_content" label="%{getText('node.config.lag.tab.label')}" />
  <s:url var="lag_tab_content_url" action="lag-tab-content" escapeAmp="false">
    <s:param name="node_id" value="%{node_id}" />
    <s:param name="tabIndex" value="4" />
    <s:param name="breadcrumbsId" value="%{#parameters.breadcrumbsId}" />
  </s:url>
  <sj:div id="lag_tab_content" href="%{lag_tab_content_url}" indicator="lag_tab_indicator" cssClass="giane-tab-content" />

  <sj:tab id="fixedIpV4Route_tab" target="fixedIpV4Route_tab_content" label="%{getText('node.config.fixedIpV4Route.tab.label')}" />
  <s:url var="fixedIpV4Route_tab_content_url" action="fixed-ip-v4-route-tab-content" escapeAmp="false">
    <s:param name="node_id" value="%{node_id}" />
    <s:param name="tabIndex" value="5" />
    <s:param name="breadcrumbsId" value="%{#parameters.breadcrumbsId}" />
  </s:url>
  <sj:div id="fixedIpV4Route_tab_content" href="%{fixedIpV4Route_tab_content_url}" indicator="fixedIpV4Route_tab_indicator" cssClass="giane-tab-content" />
  
  <sj:tab id="fixedIpV6Route_tab" target="fixedIpV6Route_tab_content" label="%{getText('node.config.fixedIpV6Route.tab.label')}" />
  <s:url var="fixedIpV6Route_tab_content_url" action="fixed-ip-v6-route-tab-content" escapeAmp="false">
    <s:param name="node_id" value="%{node_id}" />
    <s:param name="tabIndex" value="6" />
    <s:param name="breadcrumbsId" value="%{#parameters.breadcrumbsId}" />
  </s:url>
  <sj:div id="fixedIpV6Route_tab_content" href="%{fixedIpV6Route_tab_content_url}" indicator="fixedIpV6Route_tab_indicator" cssClass="giane-tab-content" />
</sj:tabbedpanel>

<img id="snmpAgent_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="giane-tab-indicator" />
<img id="physicalNetworkInterface_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="giane-tab-indicator" />
<img id="realNetworkInterface_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="giane-tab-indicator" />
<img id="vlan_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="giane-tab-indicator" />
<img id="lag_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="giane-tab-indicator" />
<img id="fixedIpV4Route_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="giane-tab-indicator" />
<img id="fixedIpV6Route_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="giane-tab-indicator" />
