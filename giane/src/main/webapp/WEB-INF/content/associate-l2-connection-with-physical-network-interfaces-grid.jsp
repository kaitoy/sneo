<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="grid_url" action="%{#parameters.gridAction}" escapeAmp="false">
  <s:param name="l2Connection_id" value="%{#parameters.l2Connection_id}" />
  <s:param name="network_id" value="%{#parameters.network_id}" />
</s:url>

<sjg:grid
  id="%{#parameters.gridId}"
  caption="%{getText(#parameters.gridCaption)}"
  dataType="json"
  href="%{grid_url}"
  pager="false"
  toppager="false"
  navigator="false"
  navigatorAdd="false"
  navigatorEdit="false"
  navigatorView="false"
  navigatorDelete="false"
  navigatorSearch="false"
  editinline="false"
  multiselect="true"
  viewrecords="false"
  viewsortcols="[true, 'vertical', true]"
  gridModel="gridModel"
  rownumbers="false"
  height="320"
  width="%{#parameters.gridWidth}"
  loadonce="true"
  scroll="true"
  shrinkToFit="true"
  altRows="true"
  gridview="true"
  hidegrid="false"
  groupField="%{#parameters.groupField}"
  groupColumnShow="[false]"
  groupCollapse="true"
  groupText="['<b>{0}</b>']"
  groupDataSorted="true"
  groupOrder="['asc']"
  onClickGroupTopics="groupClicked"
  onSortColTopics="groupGridSorted"
  onCompleteTopics="groupGridCompleted"
>
  <sjg:gridColumn
    name="id"
    index="id"
    title="%{getText('physicalNetworkInterface.id.label')}"
    formatter="integer"
    key="true"
    sortable="true"
    hidden="true"
  />
  <sjg:gridColumn
    name="name"
    index="name"
    title="%{getText('physicalNetworkInterface.name.label')}"
    sortable="true"
    width="250"
  />
  <sjg:gridColumn
    name="nodeName"
    index="nodeName"
    title="%{getText('physicalNetworkInterface.nodeName.label')}"
    sortable="true"
    width="150"
  />
</sjg:grid>

<div id="l2Connection_unassociated_physicalNetworkInterface_grid_expanded_groups" style="display: none;"></div>

