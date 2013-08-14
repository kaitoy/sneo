<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="ipAddress_grid_url" action="ip-address-grid">
  <s:param name="ipAddressRelation_id" value="%{#parameters.ipAddressRelation_id}" />
</s:url>
<s:url var="ipAddress_edit_grid_entry_url" action="ip-address-edit-grid-entry">
  <s:param name="ipAddressRelation_id" value="%{#parameters.ipAddressRelation_id}" />
</s:url>

<sjg:grid
  id="ipAddress_grid"
  caption="%{getText('ipAddress.grid.caption')}"
  dataType="json"
  href="%{ipAddress_grid_url}"
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
  editurl="%{ipAddress_edit_grid_entry_url}"
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
  onDblClickRowTopics="ipAddress_rowDblClicked"
  reloadTopics="ipAddressTableUpdated"
>
  <sjg:gridColumn
    name="id"
    index="id"
    title="%{getText('ipAddress.id.label')}"
    formatter="integer"
    key="true"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','lt','gt']}"
    width="50"
  />
  <sjg:gridColumn
    name="address"
    index="address"
    title="%{getText('ipAddress.address.label')}"
    sortable="true"
    editable="true"
    edittype="text"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="100"
  />
  <sjg:gridColumn
    name="prefixLength"
    index="prefixLength"
    title="%{getText('ipAddress.prefixLength.label')}"
    formatter="integer"
    sortable="true"
    editable="true"
    edittype="text"
    search="true"
    searchoptions="{sopt:['eq','ne','lt','gt']}"
    width="50"
  />
</sjg:grid>
