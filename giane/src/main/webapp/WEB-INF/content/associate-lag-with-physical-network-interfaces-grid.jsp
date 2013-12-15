<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="grid_url" action="%{#parameters.gridAction}" escapeAmp="false">
  <s:param name="node_id" value="%{#parameters.node_id}" />
  <s:param name="lag_id" value="%{#parameters.lag_id}" />
</s:url>

<sjg:grid
  id="%{getText(#parameters.gridId)}"
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
  width="300"
  loadonce="true"
  scroll="true"
  shrinkToFit="true"
  altRows="true"
  gridview="true"
  hidegrid="false"
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
    width="150"
  />
</sjg:grid>
