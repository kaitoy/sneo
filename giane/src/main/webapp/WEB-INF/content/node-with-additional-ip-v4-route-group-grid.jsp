<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="node_with_additionalIpV4RouteGroup_grid_url" action="node-with-additional-ip-v4-route-group-grid">
  <s:param name="simulation_id" value="%{#parameters.simulation_id}" />
</s:url>

<sjg:grid
  id="node_with_additionalIpV4RouteGroup_grid"
  caption="%{getText('node.with.additionalIpV4RouteGroup.grid.caption')}"
  dataType="json"
  href="%{node_with_additionalIpV4RouteGroup_grid_url}"
  pager="true"
  toppager="false"
  navigator="true"
  navigatorAdd="false"
  navigatorEdit="false"
  navigatorView="true"
  navigatorViewOptions="{modal:true}"
  navigatorDelete="false"
  navigatorSearch="true"
  navigatorSearchOptions="{modal:true, drag:true, closeAfterSearch:true, closeAfterReset:true}"
  editinline="false"
  multiselect="false"
  viewrecords="true"
  viewsortcols="[true, 'vertical', true]"
  gridModel="gridModel"
  rowNum="12"
  rownumbers="true"
  width="550"
  shrinkToFit="true"
  altRows="true"
  gridview="true"
  onSelectRowTopics="rowSelected"
  onCompleteTopics="gridCompleted"
  onDblClickRowTopics="node_with_additionalIpV4RouteGroup_rowDblClicked"
  reloadTopics="set_additionalIpV4RouteGroup_to_node_success"
>
  <sjg:gridColumn
    name="id"
    index="id"
    title="%{getText('node.id.label')}"
    formatter="integer"
    key="true"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','lt','gt']}"
    hidden="true"
  />
  <sjg:gridColumn
    name="name"
    index="name"
    title="%{getText('node.name.label')}"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="200"
  />
  <sjg:gridColumn
    name="additionalIpV4RouteGroup"
    index="additionalIpV4RouteGroup"
    title="%{getText('node.additionalIpV4RouteGroup.label')}"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="200"
  />
</sjg:grid>
