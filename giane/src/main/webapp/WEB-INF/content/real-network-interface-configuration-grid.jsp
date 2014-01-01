<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="realNetworkInterfaceConfiguration_grid_url" action="real-network-interface-configuration-grid"/>

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
  navigatorDelete="false"
  navigatorSearch="true"
  navigatorSearchOptions="{modal:true, drag:true, closeAfterSearch:true, closeAfterReset:true}"
  navigatorExtraButtons="{
    config: { 
      title: 'Configure selected item',
      icon: 'ui-icon-gear',
      topic: 'gridConfigButtonClicked'
    },
    delete: { 
      title: 'Delete selected item',
      icon: 'ui-icon-trash',
      topic: 'gridDeleteButtonClicked'
    }
  }"
  editinline="false"
  multiselect="true"
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
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="140"
  />
  <sjg:gridColumn
    name="macAddress"
    index="macAddress"
    title="%{getText('realNetworkInterfaceConfiguration.macAddress.label')}"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="140"
  />
  <sjg:gridColumn
    name="deviceName"
    index="deviceName"
    title="%{getText('realNetworkInterfaceConfiguration.deviceName.label')}"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="300"
  />
  <sjg:gridColumn
    name="descr"
    index="descr"
    title="%{getText('realNetworkInterfaceConfiguration.descr.label')}"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="200"
    formatter="oneLine"
  />
</sjg:grid>

<s:form id="realNetworkInterfaceConfiguration_delete_form">
  <s:hidden id="realNetworkInterfaceConfiguration_deletingIdList" name="deletingIdList" />
  <s:url var="delete_confirmation_url" action="confirmation-dialog" escapeAmp="false">
    <s:param name="okTopic" value="'realNetworkInterfaceConfiguration_delete'" />
    <s:param name="textKey" value="'dialog.text.confirmation.realNetworkInterfaceConfiguration.delete'" />
  </s:url>
  <sj:submit
    listenTopics="realNetworkInterfaceConfiguration_deleteConfirmation"
    href="%{delete_confirmation_url}"
    targets="trash_box"
    replaceTarget="false"
    validate="false"
    clearForm="false"
    cssStyle="display: none;"
  />
  <s:url var="realNetworkInterfaceConfiguration_delete_url" action="real-network-interface-configuration-delete" />
  <sj:submit
    listenTopics="realNetworkInterfaceConfiguration_delete"
    href="%{realNetworkInterfaceConfiguration_delete_url}"
    targets="trash_box"
    replaceTarget="false"
    onSuccessTopics="realNetworkInterfaceConfigurationTableUpdated"
    onErrorTopics="deleteError"
    clearForm="true"
    cssStyle="display: none;"
  />
</s:form>
