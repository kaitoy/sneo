<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="realNetworkInterfaceConfiguration_grid_url" action="real-network-interface-configuration-grid"/>
<s:url var="realNetworkInterfaceConfiguration_edit_grid_entry_url" action="real-network-interface-configuration-edit-grid-entry"/>

<sjg:grid
  id="realNetworkInterfaceConfiguration_grid"
  caption="%{getText('realNetworkInterfaceConfiguration.grid.caption')}"
  dataType="json"
  href="%{realNetworkInterfaceConfiguration_grid_url}"
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
      topic: 'realNetworkInterfaceConfiguration_configButtonClicked'
    }
  }"
  editurl="%{realNetworkInterfaceConfiguration_edit_grid_entry_url}"
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
  onDblClickRowTopics="realNetworkInterfaceConfiguration_rowDblClicked"
  reloadTopics="realNetworkInterfaceConfigurationTableUpdated"
>
  <sjg:gridColumn
    name="id"
    index="id"
    title="%{getText('realNetworkInterfaceConfiguration.id.label')}"
    formatter="integer"
    key="true"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','lt','gt']}"
    width="70"
    hidden="true"
  />
  <sjg:gridColumn
    name="name"
    index="name"
    title="%{getText('realNetworkInterfaceConfiguration.name.label')}"
    sortable="true"
    editable="true"
    edittype="text"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="140"
  />
  <sjg:gridColumn
    name="macAddress"
    index="macAddress"
    title="%{getText('realNetworkInterfaceConfiguration.macAddress.label')}"
    sortable="true"
    editable="true"
    edittype="text"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="140"
  />
  <sjg:gridColumn
    name="deviceName"
    index="deviceName"
    title="%{getText('realNetworkInterfaceConfiguration.deviceName.label')}"
    sortable="true"
    editable="false"
    edittype="text"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="300"
  />
  <sjg:gridColumn
    name="descr"
    index="descr"
    title="%{getText('realNetworkInterfaceConfiguration.descr.label')}"
    sortable="true"
    editable="true"
    edittype="text"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="200"
    formatter="oneLine"
  />
</sjg:grid>
