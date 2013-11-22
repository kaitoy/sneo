<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url
  var="lag_associated_physicalNetworkInterface_grid_url"
  action="lag-associated-physical-network-interface-grid"
  escapeAmp="false"
>
  <s:param name="node_id" value="%{#parameters.node_id}" />
  <s:param name="lag_id" value="%{#parameters.lag_id}" />
</s:url>
<s:url
  var="lag_associated_physicalNetworkInterface_edit_grid_entry_url"
  action="lag-associated-physical-network-interface-edit-grid-entry"
  escapeAmp="false"
>
  <s:param name="node_id" value="%{#parameters.node_id}" />
  <s:param name="lag_id" value="%{#parameters.lag_id}" />
</s:url>

<sjg:grid
  id="lag_associated_physicalNetworkInterface_grid"
  caption="%{getText('lag.associated.physicalNetworkInterface.grid.caption')}"
  dataType="json"
  href="%{lag_associated_physicalNetworkInterface_grid_url}"
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
  editurl="%{lag_associated_physicalNetworkInterface_edit_grid_entry_url}"
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
    search="true"
    searchoptions="{sopt:['eq','ne','lt','gt']}"
    hidden="true"
  />
  <sjg:gridColumn
    name="name"
    index="name"
    title="%{getText('physicalNetworkInterface.name.label')}"
    sortable="true"
    editable="true"
    edittype="text"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="150"
  />
</sjg:grid>
