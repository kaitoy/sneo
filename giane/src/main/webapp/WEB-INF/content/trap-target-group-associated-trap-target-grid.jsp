<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="trapTargetGroup_associated_trapTarget_grid_url" action="trap-target-group-associated-trap-target-grid" escapeAmp="false">
  <s:param name="trapTargetGroup_id" value="%{#parameters.trapTargetGroup_id}" />
</s:url>
<s:url var="trapTargetGroup_associated_trapTarget_edit_grid_entry_url" action="trap-target-group-associated-trap-target-edit-grid-entry" escapeAmp="false">
  <s:param name="trapTargetGroup_id" value="%{#parameters.trapTargetGroup_id}" />
</s:url>

<sjg:grid
  id="trapTargetGroup_associated_trapTarget_grid"
  caption="%{getText('trapTargetGroup.associated.trapTarget.grid.caption')}"
  dataType="json"
  href="%{trapTargetGroup_associated_trapTarget_grid_url}"
  pager="false"
  toppager="false"
  navigator="false"
  navigatorAdd="false"
  navigatorEdit="false"
  navigatorView="false"
  navigatorViewOptions="{modal:true}"
  navigatorDelete="false"
  navigatorDeleteOptions="{modal:true, drag:true, reloadAfterSubmit:true, width:300, left:0}"
  navigatorSearch="false"
  navigatorSearchOptions="{modal:true, drag:true, closeAfterSearch:true, closeAfterReset:true}"
  editurl="%{trapTargetGroup_associated_trapTarget_edit_grid_entry_url}"
  editinline="false"
  multiselect="true"
  viewrecords="false"
  viewsortcols="[true, 'vertical', true]"
  gridModel="gridModel"
  rownumbers="false"
  height="400"
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
    title="%{getText('trapTarget.id.label')}"
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
    title="%{getText('trapTarget.name.label')}"
    sortable="true"
    editable="true"
    edittype="text"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="150"
  />
</sjg:grid>
