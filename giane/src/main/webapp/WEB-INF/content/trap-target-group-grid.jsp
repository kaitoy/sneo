<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="trapTargetGroup_grid_url" action="trap-target-group-grid"/>
<s:url var="trapTargetGroup_edit_grid_entry_url" action="trap-target-group-edit-grid-entry"/>

<sjg:grid
  id="trapTargetGroup_grid"
  caption="%{getText('trapTargetGroup.grid.caption')}"
  dataType="json"
  href="%{trapTargetGroup_grid_url}"
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
  editurl="%{trapTargetGroup_edit_grid_entry_url}"
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
  onSelectRowTopics="rowSelected"
  onCompleteTopics="gridCompleted"
  onDblClickRowTopics="trapTargetGroup_rowDblClicked"
  reloadTopics="trapTargetGroupTableUpdated"
>
  <sjg:gridColumn
    name="id"
    index="id"
    title="%{getText('trapTargetGroup.id.label')}"
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
    title="%{getText('trapTargetGroup.name.label')}"
    sortable="true"
    editable="true"
    edittype="text"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="400"
  />
</sjg:grid>
