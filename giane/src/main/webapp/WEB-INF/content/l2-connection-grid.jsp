<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="l2Connection_grid_url" action="l2-connection-grid">
  <s:param name="network_id" value="%{#parameters.network_id}" />
</s:url>
<s:url var="l2Connection_edit_grid_entry_url" action="l2-connection-edit-grid-entry">
  <s:param name="network_id" value="%{#parameters.network_id}" />
</s:url>

<sjg:grid
  id="l2Connection_grid"
  caption="%{getText('l2Connection.grid.caption')}"
  dataType="json"
  href="%{l2Connection_grid_url}"
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
  editurl="%{l2Connection_edit_grid_entry_url}"
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
  onDblClickRowTopics="l2Connection_rowDblClicked"
  reloadTopics="l2ConnectionTableUpdated"
>
  <sjg:gridColumn
    name="id"
    index="id"
    title="%{getText('l2Connection.id.label')}"
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
    title="%{getText('l2Connection.name.label')}"
    sortable="true"
    editable="true"
    edittype="text"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="200"
  />
</sjg:grid>
