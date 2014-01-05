<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="breadcrumb-label" style="display: none;">
  <s:label
    title="%{getText('breadcrumbs.trapTargetGroup.configuration.title')}"
    value="%{getText('breadcrumbs.trapTargetGroup.configuration.label')}"
  />
</div>

<sj:tabbedpanel id="trapTargetGroup_config_tabs" animate="true" cssClass="giane-tab-container" selectedTab="%{selectedTab}">
  <sj:tab
    id="associate_trapTargetGroup_with_trapTargets_tab"
    target="associate_trapTargetGroup_with_trapTargets_tab_content"
    label="%{getText('trapTargetGroup.config.associate.trapTargetGroup.with.trapTargets.tab.label')}"
  />
  <s:url
    var="associate_trapTargetGroup_with_trapTargets_tab_content_url"
    action="associate-trap-target-group-with-trap-targets-tab-content"
    escapeAmp="false"
  >
    <s:param name="trapTargetGroup_id" value="%{trapTargetGroup_id}" />
    <s:param name="trapTargetGroup_name" value="%{trapTargetGroup_name}" />
    <s:param name="tabIndex" value="0" />
    <s:param name="breadcrumbsId" value="%{#parameters.breadcrumbsId}" />
  </s:url>
  <sj:div
    id="associate_trapTargetGroup_with_trapTargets_tab_content"
    href="%{associate_trapTargetGroup_with_trapTargets_tab_content_url}"
    indicator="associate_trapTargetGroup_with_trapTargets_tab_indicator"
    cssClass="giane-tab-content"
  />
</sj:tabbedpanel>

<img
  id="associate_trapTargetGroup_with_trapTargets_tab_indicator"
  src="images/loading_big.gif"
  alt="Loading..."
  style="display: none;"
  class="giane-tab-indicator"
/>
