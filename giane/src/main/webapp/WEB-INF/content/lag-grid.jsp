<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="lag_grid_url" action="lag-grid">
  <s:param name="node_id" value="%{#parameters.node_id}" />
</s:url>
<s:url var="lag_edit_grid_entry_url" action="lag-edit-grid-entry">
  <s:param name="node_id" value="%{#parameters.node_id}" />
</s:url>

<sjg:grid
  id="lag_grid"
  caption="%{getText('lag.grid.caption')}"
  dataType="json"
  href="%{lag_grid_url}"
  pager="true"
  toppager="false"
  navigator="true"
  navigatorAdd="false"
  navigatorEdit="false"
  navigatorView="true"
  navigatorViewOptions="{modal:true}"
  navigatorDelete="true"
  navigatorDeleteOptions="{modal:true, drag:true, reloadAfterSubmit:true, width:300, left:0}"
  navigatorSearch="true"
  navigatorSearchOptions="{modal:true, drag:true, closeAfterSearch:true, closeAfterReset:true}"
  navigatorExtraButtons="{
    config: { 
      title: 'Configure selected item',
      icon: 'ui-icon-gear',
      topic: 'lag_configButtonClicked'
    }
  }"
  editurl="%{lag_edit_grid_entry_url}"
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
  onDblClickRowTopics="lag_rowDblClicked"
  reloadTopics="lagTableUpdated"
>
  <sjg:gridColumn
    name="id"
    index="id"
    title="%{getText('lag.id.label')}"
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
    title="%{getText('lag.name.label')}"
    sortable="true"
    editable="true"
    edittype="text"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="200"
  />
  <sjg:gridColumn
    name="channelGroupNumber"
    index="channelGroupNumber"
    title="%{getText('lag.channelGroupNumber.label')}"
    sortable="true"
    editable="true"
    edittype="text"
    search="true"
    searchoptions="{sopt:['eq','ne','lt','gt']}"
    width="100"
  />
</sjg:grid>
