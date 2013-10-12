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
  &nbsp;&gt;&nbsp;[<s:text name="breadcrumbs.additionalIpV4RouteGroup.configuration" />: <s:property value="#parameters.additionalIpV4RouteGroup_name" />]
</div>

<sj:tabbedpanel id="additionalIpV4RouteGroup_config_tabs" animate="true" cssClass="tabs">
  <sj:tab
    id="associate_additionalIpV4RouteGroup_with_additionalIpV4Routes_tab"
    target="associate_additionalIpV4RouteGroup_with_additionalIpV4Routes_tab_content"
    label="%{getText('additionalIpV4RouteGroup.config.associate.additionalIpV4RouteGroup.with.additionalIpV4Routes.tab.label')}"
  />
  <s:url
    var="associate_additionalIpV4RouteGroup_with_additionalIpV4Routes_tab_content_url"
    action="associate-additional-ip-v4-route-group-with-additional-ip-v4-routes-tab-content"
    escapeAmp="false"
  >
    <s:param name="additionalIpV4RouteGroup_id" value="%{#parameters.additionalIpV4RouteGroup_id}" />
    <s:param name="additionalIpV4RouteGroup_name" value="%{#parameters.additionalIpV4RouteGroup_name}" />
  </s:url>
  <sj:div
    id="associate_additionalIpV4RouteGroup_with_additionalIpV4Routes_tab_content"
    href="%{associate_additionalIpV4RouteGroup_with_additionalIpV4Routes_tab_content_url}"
    indicator="associate_additionalIpV4RouteGroup_with_additionalIpV4Routes_tab_indicator"
    cssClass="tab-content"
  />
</sj:tabbedpanel>

<img
  id="associate_additionalIpV4RouteGroup_with_additionalIpV4Routes_tab_indicator"
  src="images/loading_big.gif"
  alt="Loading..."
  style="display: none;"
  class="tab-indicator"
/>
