<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="breadcrumb-label" style="display: none;">
  <s:label
    title="%{getText('breadcrumbs.additionalIpV6RouteGroup.configuration.title')}"
    value="%{getText('breadcrumbs.additionalIpV6RouteGroup.configuration.label')}"
  />
</div>

<sj:tabbedpanel id="additionalIpV6RouteGroup_config_tabs" animate="true" cssClass="giane-tab-container" selectedTab="%{selectedTab}">
  <sj:tab
    id="associate_additionalIpV6RouteGroup_with_additionalIpV6Routes_tab"
    target="associate_additionalIpV6RouteGroup_with_additionalIpV6Routes_tab_content"
    label="%{getText('additionalIpV6RouteGroup.config.associate.additionalIpV6RouteGroup.with.additionalIpV6Routes.tab.label')}"
  />
  <s:url
    var="associate_additionalIpV6RouteGroup_with_additionalIpV6Routes_tab_content_url"
    action="associate-additional-ip-v6-route-group-with-additional-ip-v6-routes-tab-content"
    escapeAmp="false"
  >
    <s:param name="additionalIpV6RouteGroup_id" value="%{additionalIpV6RouteGroup_id}" />
    <s:param name="additionalIpV6RouteGroup_name" value="%{additionalIpV6RouteGroup_name}" />
    <s:param name="tabIndex" value="0" />
  </s:url>
  <sj:div
    id="associate_additionalIpV6RouteGroup_with_additionalIpV6Routes_tab_content"
    href="%{associate_additionalIpV6RouteGroup_with_additionalIpV6Routes_tab_content_url}"
    indicator="associate_additionalIpV6RouteGroup_with_additionalIpV6Routes_tab_indicator"
    cssClass="giane-tab-content"
  />
</sj:tabbedpanel>

<img
  id="associate_additionalIpV6RouteGroup_with_additionalIpV6Routes_tab_indicator"
  src="images/loading_big.gif"
  alt="Loading..."
  style="display: none;"
  class="giane-tab-indicator"
/>
