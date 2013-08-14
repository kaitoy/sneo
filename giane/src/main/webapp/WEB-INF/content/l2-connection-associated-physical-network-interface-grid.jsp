<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="l2Connection_associated_physicalNetworkInterface_grid_url" action="l2-connection-associated-physical-network-interface-grid" escapeAmp="false">
  <s:param name="network_id" value="%{#parameters.network_id}" />
  <s:param name="l2Connection_id" value="%{#parameters.l2Connection_id}" />
</s:url>
<s:url var="l2Connection_associated_physicalNetworkInterface_edit_grid_entry_url" action="l2-connection-associated-physical-network-interface-edit-grid-entry" escapeAmp="false">
  <s:param name="network_id" value="%{#parameters.network_id}" />
  <s:param name="l2Connection_id" value="%{#parameters.l2Connection_id}" />
</s:url>

<sjg:grid
  id="l2Connection_associated_physicalNetworkInterface_grid"
  caption="%{getText('l2Connection.associated.physicalNetworkInterface.grid.caption')}"
  dataType="json"
  href="%{l2Connection_associated_physicalNetworkInterface_grid_url}"
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
  editurl="%{l2Connection_associated_physicalNetworkInterface_edit_grid_entry_url}"
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
    title="%{getText('physicalNetworkInterface.id.label')}"
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
    title="%{getText('physicalNetworkInterface.name.label')}"
    sortable="true"
    editable="true"
    edittype="text"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="150"
  />
  <sjg:gridColumn
    name="nodeName"
    index="nodeName"
    title="%{getText('physicalNetworkInterface.nodeName.label')}"
    sortable="true"
    editable="true"
    edittype="text"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="150"
  />
</sjg:grid>
