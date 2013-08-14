<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="realNetworkInterface_grid_url" action="real-network-interface-grid">
  <s:param name="node_id" value="%{#parameters.node_id}" />
</s:url>
<s:url var="realNetworkInterface_edit_grid_entry_url" action="real-network-interface-edit-grid-entry">
  <s:param name="node_id" value="%{#parameters.node_id}" />
</s:url>

<sjg:grid
  id="realNetworkInterface_grid"
  caption="%{getText('realNetworkInterface.grid.caption')}"
  dataType="json"
  href="%{realNetworkInterface_grid_url}"
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
  editurl="%{realNetworkInterface_edit_grid_entry_url}"
  editinline="false"
  multiselect="false"
  viewrecords="true"
  viewsortcols="[true, 'vertical', true]"
  gridModel="gridModel"
  rowNum="15"
  rownumbers="true"
  width="650"
  shrinkToFit="true"
  altRows="true"
  gridview="true"
  onSelectRowTopics="rowSelected"
  onCompleteTopics="gridCompleted"
  onDblClickRowTopics="realNetworkInterface_rowDblClicked"
  reloadTopics="realNetworkInterfaceTableUpdated"
>
  <sjg:gridColumn
    name="id"
    index="id"
    title="%{getText('realNetworkInterface.id.label')}"
    formatter="integer"
    key="true"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','lt','gt']}"
    width="50"
  />
  <sjg:gridColumn
    name="name"
    index="name"
    title="%{getText('realNetworkInterface.name.label')}"
    sortable="true"
    editable="true"
    edittype="text"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="150"
  />
</sjg:grid>
