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
  &nbsp;&gt;&nbsp;[<s:text name="breadcrumbs.simulation" />: <s:property value="#parameters.simulation_name" />]
</div>

<sj:tabbedpanel id="simulation_tabs" animate="true" cssClass="tabs">
  <sj:tab id="set_trapTargetGroup_to_snmpAgent_tab" target="set_trapTargetGroup_to_snmpAgent_tab_content" label="%{getText('simulation.set.trapTargetGroup.to.snmpAgent.tab.label')}" />
  <s:url var="set_trapTargetGroup_to_snmpAgent_tab_content_url" action="set-trap-target-group-to-snmp-agent-tab-content">
    <s:param name="simulation_id" value="%{#parameters.simulation_id}" />
  </s:url>
  <sj:div id="set_trapTargetGroup_to_snmpAgent_tab_content" href="%{set_trapTargetGroup_to_snmpAgent_tab_content_url}" indicator="set_trapTargetGroup_to_snmpAgent_tab_indicator" cssClass="tab-content" />

  <sj:tab id="set_realNetworkInterfaceConfiguration_to_realNetworkInterface_tab" target="set_realNetworkInterfaceConfiguration_to_realNetworkInterface_tab_content" label="%{getText('simulation.set.realNetworkInterfaceConfiguration.to.realNetworkInterface.tab.label')}" />
  <s:url var="set_realNetworkInterfaceConfiguration_to_realNetworkInterface_tab_content_url" action="set-real-network-interface-configuration-to-real-network-interface-tab-content">
    <s:param name="simulation_id" value="%{#parameters.simulation_id}" />
  </s:url>
  <sj:div id="set_realNetworkInterfaceConfiguration_to_realNetworkInterface_tab_content" href="%{set_realNetworkInterfaceConfiguration_to_realNetworkInterface_tab_content_url}" indicator="set_realNetworkInterfaceConfiguration_to_realNetworkInterface_tab_indicator" cssClass="tab-content" />

  <sj:tab id="set_additionalIpV4RouteGroup_to_node_tab" target="set_additionalIpV4RouteGroup_to_node_tab_content" label="%{getText('simulation.set.additionalIpV4RouteGroup.to.node.tab.label')}" />
  <s:url var="set_additionalIpV4RouteGroup_to_node_tab_content_url" action="set-additional-ip-v4-route-group-to-node-tab-content" escapeAmp="false">
    <s:param name="simulation_id" value="%{#parameters.simulation_id}" />
    <s:param name="network_id" value="%{#parameters.network_id}" />
  </s:url>
  <sj:div id="set_additionalIpV4RouteGroup_to_node_tab_content" href="%{set_additionalIpV4RouteGroup_to_node_tab_content_url}" indicator="set_additionalIpV4RouteGroup_to_node_tab_indicator" cssClass="tab-content" />

  <sj:tab id="startSimulator_tab" target="startSimulator_tab_content" label="%{getText('simulation.startSimulator.tab.label')}" />
  <s:url var="startSimulator_tab_content_url" action="start-simulator-tab-content">
    <s:param name="simulation_id" value="%{#parameters.simulation_id}" />
  </s:url>
  <sj:div id="startSimulator_tab_content" href="%{startSimulator_tab_content_url}" indicator="startSimulator_tab_indicator" cssClass="tab-content" />
</sj:tabbedpanel>

<img id="set_trapTargetGroup_to_snmpAgent_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="tab-indicator" />
<img id="set_realNetworkInterfaceConfiguration_to_realNetworkInterface_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="tab-indicator" />
<img id="set_additionalIpV4RouteGroup_to_node_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="tab-indicator" />
<img id="startSimulator_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="tab-indicator" />
