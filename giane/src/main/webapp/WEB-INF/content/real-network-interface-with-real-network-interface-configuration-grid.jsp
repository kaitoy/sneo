<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="realNetworkInterface_with_realNetworkInterfaceConfiguration_grid_url" action="real-network-interface-with-real-network-interface-configuration-grid">
  <s:param name="simulation_id" value="%{#parameters.simulation_id}" />
</s:url>

<sjg:grid
  id="realNetworkInterface_with_realNetworkInterfaceConfiguration_grid"
  caption="%{getText('realNetworkInterface.with.realNetworkInterfaceConfiguration.grid.caption')}"
  dataType="json"
  href="%{realNetworkInterface_with_realNetworkInterfaceConfiguration_grid_url}"
  pager="true"
  toppager="false"
  navigator="true"
  navigatorAdd="false"
  navigatorEdit="false"
  navigatorView="true"
  navigatorViewOptions="{modal:true}"
  navigatorDelete="false"
  navigatorSearch="true"
  navigatorSearchOptions="{modal:true, drag:true, closeAfterSearch:true, closeAfterReset:true}"
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
  gridview="true"
  onSelectRowTopics="rowSelected"
  onCompleteTopics="gridCompleted"
  onDblClickRowTopics="realNetworkInterface_with_realNetworkInterfaceConfiguration_rowDblClicked"
  listenTopics="set_realNetworkInterfaceConfiguration_to_realNetworkInterface_success"
>
  <sjg:gridColumn
    name="id"
    index="id"
    title="%{getText('realNetworkInterface.id.label')}"
    formatter="integer"
    key="true"
    sortable="true"
    editable="false"
    search="true"
    searchoptions="{sopt:['eq','ne','lt','gt']}"
    width="50"
  />
  <sjg:gridColumn
    name="name"
    index="name"
    title="%{getText('realNetworkInterface.name.label')}"
    sortable="true"
    editable="false"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="200"
  />
  <sjg:gridColumn
    name="realNetworkInterfaceConfiguration"
    index="realNetworkInterfaceConfiguration"
    title="%{getText('realNetworkInterface.realNetworkInterfaceConfiguration.label')}"
    sortable="true"
    editable="false"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="200"
  />
</sjg:grid>
