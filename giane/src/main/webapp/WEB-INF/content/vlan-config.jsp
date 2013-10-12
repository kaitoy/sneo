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
  <s:url var="node_url" action="node" escapeAmp="false">
    <s:param name="network_id" value="%{#parameters.network_id}" />
    <s:param name="network_name" value="%{#parameters.network_name}" />
    <s:param name="node_id" value="%{#parameters.node_id}" />
    <s:param name="node_name" value="%{#parameters.node_name}" />
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
  &nbsp;&gt;&nbsp;
  <sj:a
    href="%{node_url}"
    targets="main"
    replaceTarget="false"
    button="false"
    indicator="main_indicator"
  >
    [<s:text name="breadcrumbs.node.configuration" />: <s:property value="#parameters.node_name" />]
  </sj:a>
  &nbsp;&gt;&nbsp;[<s:text name="breadcrumbs.vlan.configuration" />: <s:property value="#parameters.vlan_vid" />]
</div>

<sj:tabbedpanel id="vlan_config_tabs" animate="true" cssClass="tabs">
  <sj:tab id="associate_vlan_with_vlanMembers_tab" target="associate_vlan_with_vlanMembers_tab_content" label="%{getText('vlan.config.associate.vlan.with.vlanMembers.tab.label')}" />
  <s:url var="associate_vlan_with_vlanMembers_tab_content_url" action="associate-vlan-with-vlan-members-tab-content" escapeAmp="false">
    <s:param name="node_id" value="%{#parameters.node_id}" />
    <s:param name="vlan_id" value="%{#parameters.vlan_id}" />
    <s:param name="vlan_vid" value="%{#parameters.vlan_vid}" />
  </s:url>
  <sj:div id="associate_vlan_with_vlanMembers_tab_content" href="%{associate_vlan_with_vlanMembers_tab_content_url}" indicator="associate_vlan_with_vlanMembers_tab_indicator" cssClass="tab-content" />

  <sj:tab id="ipAddress_tab" target="ipAddress_tab_content" label="%{getText('vlan.config.ipAddress.tab.label')}" />
  <s:url var="ipAddress_tab_content_url" action="ip-address-tab-content">
    <s:param name="ipAddressRelation_id" value="%{#parameters.ipAddressRelation_id}" />
  </s:url>
  <sj:div id="ipAddress_tab_content" href="%{ipAddress_tab_content_url}" indicator="ipAddress_tab_indicator" cssClass="tab-content" />
</sj:tabbedpanel>

<img id="associate_vlan_with_vlanMembers_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="tab-indicator" />
<img id="ipAddress_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="tab-indicator" />
