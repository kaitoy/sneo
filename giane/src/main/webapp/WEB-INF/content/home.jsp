<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="breadcrumb-label" style="display: none;">
  <s:label
    title="%{getText('breadcrumbs.home.title')}"
    value="%{getText('breadcrumbs.home.label')}"
  />
</div>

<div class="breadcrumb-link" style="display: none;">
  <s:url var="back_to_home_url" action="back-to-home" />
  <sj:a
    id="back_to_home_button"
    href="%{back_to_home_url}"
    title="%{getText('breadcrumbs.home.title')}"
    targets="main"
    replaceTarget="false"
    button="true"
    indicator="main_indicator"
    onBeforeTopics="mainPaneGoingBack"
    onCompleteTopics="mainPaneCompleted"
  >
    <s:text name="breadcrumbs.home.label" />
  </sj:a>
</div>

<sj:tabbedpanel id="config_top_tabs" animate="true" cssClass="tabs" selectedTab="%{selectedTab}">
  <sj:tab id="network_tab" target="network_tab_content" label="%{getText('home.network.tab.label')}" />
  <s:url var="network_tab_content_url" action="network-tab-content" escapeAmp="false">
    <s:param name="tabIndex" value="0" />
  </s:url>
  <sj:div id="network_tab_content" href="%{network_tab_content_url}" indicator="network_tab_indicator" cssClass="tab-content" />
  
  <sj:tab id="realNetworkInterfaceConfiguration_tab" target="realNetworkInterfaceConfiguration_tab_content" label="%{getText('home.realNetworkInterfaceConfiguration.tab.label')}" />
  <s:url var="realNetworkInterfaceConfiguration_tab_content_url" action="real-network-interface-configuration-tab-content" escapeAmp="false">
    <s:param name="tabIndex" value="1" />
  </s:url>
  <sj:div id="realNetworkInterfaceConfiguration_tab_content" href="%{realNetworkInterfaceConfiguration_tab_content_url}" indicator="realNetworkInterfaceConfiguration_tab_indicator" cssClass="tab-content" />
  
  <sj:tab id="additionalIpV4Route_tab" target="additionalIpV4Route_tab_content" label="%{getText('home.additionalIpV4Route.tab.label')}" />
  <s:url var="additionalIpV4Route_tab_content_url" action="additional-ip-v4-route-tab-content" escapeAmp="false">
    <s:param name="tabIndex" value="2" />
  </s:url>
  <sj:div id="additionalIpV4Route_tab_content" href="%{additionalIpV4Route_tab_content_url}" indicator="additionalIpV4Route_tab_indicator" cssClass="tab-content" />

  <sj:tab id="additionalIpV4RouteGroup_tab" target="additionalIpV4RouteGroup_tab_content" label="%{getText('home.additionalIpV4RouteGroup.tab.label')}" />
  <s:url var="additionalIpV4RouteGroup_tab_content_url" action="additional-ip-v4-route-group-tab-content" escapeAmp="false">
    <s:param name="tabIndex" value="3" />
  </s:url>
  <sj:div id="additionalIpV4RouteGroup_tab_content" href="%{additionalIpV4RouteGroup_tab_content_url}" indicator="additionalIpV4RouteGroup_tab_indicator" cssClass="tab-content" />

  <sj:tab id="trapTarget_tab" target="trapTarget_tab_content" label="%{getText('home.trapTarget.tab.label')}" />
  <s:url var="trapTarget_tab_content_url" action="trap-target-tab-content" escapeAmp="false">
    <s:param name="tabIndex" value="4" />
  </s:url>
  <sj:div id="trapTarget_tab_content" href="%{trapTarget_tab_content_url}" indicator="trapTarget_tab_indicator" cssClass="tab-content" />

  <sj:tab id="trapTargetGroup_tab" target="trapTargetGroup_tab_content" label="%{getText('home.trapTargetGroup.tab.label')}" />
  <s:url var="trapTargetGroup_tab_content_url" action="trap-target-group-tab-content" escapeAmp="false">
    <s:param name="tabIndex" value="5" />
  </s:url>
  <sj:div id="trapTargetGroup_tab_content" href="%{trapTargetGroup_tab_content_url}" indicator="trapTargetGroup_tab_indicator" cssClass="tab-content" />

  <sj:tab id="simulation_tab" target="simulation_tab_content" label="%{getText('home.simulation.tab.label')}" />
  <s:url var="simulation_tab_content_url" action="simulation-tab-content" escapeAmp="false">
    <s:param name="tabIndex" value="6" />
  </s:url>
  <sj:div id="simulation_tab_content" href="%{simulation_tab_content_url}" indicator="simulation_tab_indicator" cssClass="tab-content" />
</sj:tabbedpanel>

<img id="network_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="tab-indicator" />
<img id="realNetworkInterfaceConfiguration_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="tab-indicator" />
<img id="additionalIpV4Route_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="tab-indicator" />
<img id="trapTarget_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="tab-indicator" />
<img id="trapTargetGroup_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="tab-indicator" />
<img id="additionalIpV4RouteGroup_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="tab-indicator" />
<img id="simulation_tab_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="tab-indicator" />


