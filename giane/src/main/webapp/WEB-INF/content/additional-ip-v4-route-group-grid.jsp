<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="additionalIpV4RouteGroup_grid_url" action="additional-ip-v4-route-group-grid"/>
<s:url var="additionalIpV4RouteGroup_edit_grid_entry_url" action="additional-ip-v4-route-group-edit-grid-entry"/>

<sjg:grid
  id="additionalIpV4RouteGroup_grid"
  caption="%{getText('additionalIpV4RouteGroup.grid.caption')}"
  dataType="json"
  href="%{additionalIpV4RouteGroup_grid_url}"
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
      topic: 'additionalIpV4RouteGroup_configButtonClicked'
    }
  }"
  editurl="%{additionalIpV4RouteGroup_edit_grid_entry_url}"
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
  onSelectRowTopics="rowSelected"
  onCompleteTopics="gridCompleted"
  onDblClickRowTopics="additionalIpV4RouteGroup_rowDblClicked"
  reloadTopics="additionalIpV4RouteGroupTableUpdated"
>
  <sjg:gridColumn
    name="id"
    index="id"
    title="%{getText('additionalIpV4RouteGroup.id.label')}"
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
    title="%{getText('additionalIpV4RouteGroup.name.label')}"
    sortable="true"
    editable="true"
    edittype="text"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="400"
  />
  <sjg:gridColumn
    name="descr"
    index="descr"
    title="%{getText('additionalIpV4RouteGroup.descr.label')}"
    sortable="true"
    editable="true"
    edittype="text"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="200"
    formatter="oneLine"
  />
</sjg:grid>
