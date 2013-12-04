<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="breadcrumb-label" style="display: none;">
  <s:label
    title="%{getText('breadcrumbs.simulation.configuration.title')}"
    value="%{getText('breadcrumbs.simulation.configuration.label')}"
  />
</div>

<sj:tabbedpanel id="simulation_tabs" animate="true" cssClass="tabs" selectedTab="%{selectedTab}">
  <sj:tab id="set_trapTargetGroup_to_snmpAgent_tab" target="set_trapTargetGroup_to_snmpAgent_tab_content" label="%{getText('simulation.set.trapTargetGroup.to.snmpAgent.tab.label')}" />
  <s:url var="set_trapTargetGroup_to_snmpAgent_tab_content_url" action="set-trap-target-group-to-snmp-agent-tab-content" escapeAmp="false">
    <s:param name="simulation_id" value="%{#parameters.simulation_id}" />
    <s:param name="tabIndex" value="0" />
  </s:url>
  <sj:div id="set_trapTargetGroup_to_snmpAgent_tab_content" href="%{set_trapTargetGroup_to_snmpAgent_tab_content_url}" indicator="set_trapTargetGroup_to_snmpAgent_tab_indicator" cssClass="tab-content" />

  <sj:tab id="set_realNetworkInterfaceConfiguration_to_realNetworkInterface_tab" target="set_realNetworkInterfaceConfiguration_to_realNetworkInterface_tab_content" label="%{getText('simulation.set.realNetworkInterfaceConfiguration.to.realNetworkInterface.tab.label')}" />
  <s:url var="set_realNetworkInterfaceConfiguration_to_realNetworkInterface_tab_content_url" action="set-real-network-interface-configuration-to-real-network-interface-tab-content" escapeAmp="false">
    <s:param name="simulation_id" value="%{#parameters.simulation_id}" />
    <s:param name="tabIndex" value="1" />
  </s:url>
  <sj:div id="set_realNetworkInterfaceConfiguration_to_realNetworkInterface_tab_content" href="%{set_realNetworkInterfaceConfiguration_to_realNetworkInterface_tab_content_url}" indicator="set_realNetworkInterfaceConfiguration_to_realNetworkInterface_tab_indicator" cssClass="tab-content" />

  <sj:tab id="set_additionalIpV4RouteGroup_to_node_tab" target="set_additionalIpV4RouteGroup_to_node_tab_content" label="%{getText('simulation.set.additionalIpV4RouteGroup.to.node.tab.label')}" />
  <s:url var="set_additionalIpV4RouteGroup_to_node_tab_content_url" action="set-additional-ip-v4-route-group-to-node-tab-content" escapeAmp="false">
    <s:param name="simulation_id" value="%{#parameters.simulation_id}" />
    <s:param name="tabIndex" value="2" />
  </s:url>
  <sj:div id="set_additionalIpV4RouteGroup_to_node_tab_content" href="%{set_additionalIpV4RouteGroup_to_node_tab_content_url}" indicator="set_additionalIpV4RouteGroup_to_node_tab_indicator" cssClass="tab-content" />

  <sj:tab id="startSimulator_tab" target="startSimulator_tab_content" label="%{getText('simulation.startSimulator.tab.label')}" />
  <s:url var="startSimulator_tab_content_url" action="start-simulator-tab-content" escapeAmp="false">
    <s:param name="simulation_id" value="%{#parameters.simulation_id}" />
    <s:param name="tabIndex" value="3" />
  </s:url>
  <sj:div id="startSimulator_tab_content" href="%{startSimulator_tab_content_url}" indicator="startSimulator_tab_indicator" cssClass="tab-content" />
</sj:tabbedpanel>

<img id="set_trapTargetGroup_to_snmpAgent_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="tab-indicator" />
<img id="set_realNetworkInterfaceConfiguration_to_realNetworkInterface_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="tab-indicator" />
<img id="set_additionalIpV4RouteGroup_to_node_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="tab-indicator" />
<img id="startSimulator_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="tab-indicator" />
