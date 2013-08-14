<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="simulation_grid_url" action="simulation-grid">
  <s:param name="network_id" value="%{#parameters.network_id}" />
</s:url>
<s:url var="simulation_edit_grid_entry_url" action="simulation-edit-grid-entry">
  <s:param name="network_id" value="%{#parameters.network_id}" />
</s:url>

<sjg:grid
  id="simulation_grid"
  caption="%{getText('simulation.grid.caption')}"
  dataType="json"
  href="%{simulation_grid_url}"
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
  editurl="%{simulation_edit_grid_entry_url}"
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
  onDblClickRowTopics="simulation_rowDblClicked"
  reloadTopics="simulationTableUpdated"
>
  <sjg:gridColumn
    name="id"
    index="id"
    title="%{getText('simulation.id.label')}"
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
    title="%{getText('simulation.name.label')}"
    sortable="true"
    editable="true"
    edittype="text"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="200"
  />
  <sjg:gridColumn
    name="network"
    index="network"
    title="%{getText('simulation.network.label')}"
    sortable="true"
    editable="true"
    edittype="text"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="200"
  />
</sjg:grid>
