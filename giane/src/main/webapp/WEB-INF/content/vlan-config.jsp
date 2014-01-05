<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="breadcrumb-label" style="display: none;">
  <s:label
    title="%{getText('breadcrumbs.vlan.configuration.title')}"
    value="%{getText('breadcrumbs.vlan.configuration.label')}"
  />
</div>

<sj:tabbedpanel id="vlan_config_tabs" animate="true" cssClass="giane-tab-container" selectedTab="%{selectedTab}">
  <sj:tab id="associate_vlan_with_vlanMembers_tab" target="associate_vlan_with_vlanMembers_tab_content" label="%{getText('vlan.config.associate.vlan.with.vlanMembers.tab.label')}" />
  <s:url var="associate_vlan_with_vlanMembers_tab_content_url" action="associate-vlan-with-vlan-members-tab-content" escapeAmp="false">
    <s:param name="node_id" value="%{node_id}" />
    <s:param name="vlan_id" value="%{vlan_id}" />
    <s:param name="vlan_name" value="%{vlan_name}" />
    <s:param name="tabIndex" value="0" />
    <s:param name="breadcrumbsId" value="%{#parameters.breadcrumbsId}" />
  </s:url>
  <sj:div id="associate_vlan_with_vlanMembers_tab_content" href="%{associate_vlan_with_vlanMembers_tab_content_url}" indicator="associate_vlan_with_vlanMembers_tab_indicator" cssClass="giane-tab-content" />

  <sj:tab id="ipAddress_tab" target="ipAddress_tab_content" label="%{getText('vlan.config.ipAddress.tab.label')}" />
  <s:url var="ipAddress_tab_content_url" action="ip-address-tab-content" escapeAmp="false">
    <s:param name="ipAddressRelation_id" value="%{ipAddressRelation_id}" />
    <s:param name="tabIndex" value="1" />
    <s:param name="breadcrumbsId" value="%{#parameters.breadcrumbsId}" />
  </s:url>
  <sj:div id="ipAddress_tab_content" href="%{ipAddress_tab_content_url}" indicator="ipAddress_tab_indicator" cssClass="giane-tab-content" />
</sj:tabbedpanel>

<img id="associate_vlan_with_vlanMembers_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="giane-tab-indicator" />
<img id="ipAddress_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="giane-tab-indicator" />
