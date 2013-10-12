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
  &nbsp;&gt;&nbsp;[<s:text name="breadcrumbs.node.configuration" />: <s:property value="#parameters.node_name" />]
</div>

<sj:tabbedpanel id="node_config_tabs" animate="true" cssClass="tabs">
  <sj:tab id="snmpAgent_tab" target="snmpAgent_tab_content" label="%{getText('node.config.snmpAgent.tab.label')}" />
  <s:url var="snmpAgent_tab_content_url" action="snmp-agent-tab-content">
    <s:param name="node_id" value="%{#parameters.node_id}" />
  </s:url>
  <sj:div id="snmpAgent_tab_content" href="%{snmpAgent_tab_content_url}" indicator="snmpAgent_tab_indicator" cssClass="tab-content" />

  <sj:tab id="physicalNetworkInterface_tab" target="physicalNetworkInterface_tab_content" label="%{getText('node.config.physicalNetworkInterface.tab.label')}" />
  <s:url var="physicalNetworkInterface_tab_content_url" action="physical-network-interface-tab-content">
    <s:param name="node_id" value="%{#parameters.node_id}" />
  </s:url>
  <sj:div id="physicalNetworkInterface_tab_content" href="%{physicalNetworkInterface_tab_content_url}" indicator="physicalNetworkInterface_tab_indicator" cssClass="tab-content" />

  <sj:tab id="realNetworkInterface_tab" target="realNetworkInterface_tab_content" label="%{getText('node.config.realNetworkInterface.tab.label')}" />
  <s:url var="realNetworkInterface_tab_content_url" action="real-network-interface-tab-content" escapeAmp="false">
    <s:param name="node_id" value="%{#parameters.node_id}" />
    <s:param name="node_name" value="%{#parameters.node_name}" />
  </s:url>
  <sj:div id="realNetworkInterface_tab_content" href="%{realNetworkInterface_tab_content_url}" indicator="realNetworkInterface_tab_indicator" cssClass="tab-content" />

  <sj:tab id="vlan_tab" target="vlan_tab_content" label="%{getText('node.config.vlan.tab.label')}" />
  <s:url var="vlan_tab_content_url" action="vlan-tab-content">
    <s:param name="node_id" value="%{#parameters.node_id}" />
  </s:url>
  <sj:div id="vlan_tab_content" href="%{vlan_tab_content_url}" indicator="vlan_tab_indicator" cssClass="tab-content" />

  <sj:tab id="fixedIpV4Route_tab" target="fixedIpV4Route_tab_content" label="%{getText('node.config.fixedIpV4Route.tab.label')}" />
  <s:url var="fixedIpV4Route_tab_content_url" action="fixed-ip-v4-route-tab-content">
    <s:param name="node_id" value="%{#parameters.node_id}" />
  </s:url>
  <sj:div id="fixedIpV4Route_tab_content" href="%{fixedIpV4Route_tab_content_url}" indicator="fixedIpV4Route_tab_indicator" cssClass="tab-content" />
</sj:tabbedpanel>

<img id="snmpAgent_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="tab-indicator" />
<img id="physicalNetworkInterface_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="tab-indicator" />
<img id="realNetworkInterface_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="tab-indicator" />
<img id="vlan_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="tab-indicator" />
<img id="fixedIpV4Route_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="tab-indicator" />
