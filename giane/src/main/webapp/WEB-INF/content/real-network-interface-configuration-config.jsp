<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="breadcrumb-label" style="display: none;">
  <s:label
    title="%{getText('breadcrumbs.realNetworkInterfaceConfiguration.configuration.title')}"
    value="%{getText('breadcrumbs.realNetworkInterfaceConfiguration.configuration.label')}"
  />
</div>

<sj:tabbedpanel id="realNetworkInterfaceConfiguration_config_tabs" animate="true" cssClass="tabs">
  <sj:tab id="ipAddress_tab" target="ipAddress_tab_content" label="%{getText('realNetworkInterfaceConfiguration.config.ipAddress.tab.label')}" />
  <s:url var="ipAddress_tab_content_url" action="ip-address-tab-content">
    <s:param name="ipAddressRelation_id" value="%{#parameters.ipAddressRelation_id}" />
  </s:url>
  <sj:div id="ipAddress_tab_content" href="%{ipAddress_tab_content_url}" indicator="ipAddress_tab_indicator" cssClass="tab-content" />
</sj:tabbedpanel>

<img id="ipAddress_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="tab-indicator" />
