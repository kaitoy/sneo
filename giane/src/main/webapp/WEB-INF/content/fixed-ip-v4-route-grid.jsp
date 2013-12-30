<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="fixedIpV4Route_grid_url" action="fixed-ip-v4-route-grid">
  <s:param name="node_id" value="%{#parameters.node_id}" />
</s:url>

<sjg:grid
  id="fixedIpV4Route_grid"
  caption="%{getText('fixedIpV4Route.grid.caption')}"
  dataType="json"
  href="%{fixedIpV4Route_grid_url}"
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
    delete: { 
      title: 'Delete selected item',
      icon: 'ui-icon-trash',
      topic: 'gridDeleteButtonClicked'
    }
  }"
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
  onDblClickRowTopics="fixedIpV4Route_rowDblClicked"
  reloadTopics="fixedIpV4RouteTableUpdated"
>
  <sjg:gridColumn
    name="id"
    index="id"
    title="%{getText('fixedIpV4Route.id.label')}"
    formatter="integer"
    key="true"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','lt','gt']}"
    hidden="true"
  />
  <sjg:gridColumn
    name="networkDestination"
    index="networkDestination"
    title="%{getText('fixedIpV4Route.networkDestination.label')}"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="100"
  />
  <sjg:gridColumn
    name="netmask"
    index="netmask"
    title="%{getText('fixedIpV4Route.netmask.label')}"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="50"
  />
  <sjg:gridColumn
    name="gateway"
    index="gateway"
    title="%{getText('fixedIpV4Route.gateway.label')}"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="50"
  />
  <sjg:gridColumn
    name="metric"
    index="metric"
    title="%{getText('fixedIpV4Route.metric.label')}"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','lt','gt']}"
    width="50"
  />
</sjg:grid>

<s:form id="fixedIpV4Route_delete_form">
  <s:hidden id="fixedIpV4Route_deletingIdList" name="deletingIdList" />
  <s:url var="delete_confirmation_url" action="confirmation-dialog" escapeAmp="false">
    <s:param name="okTopic" value="'fixedIpV4Route_delete'" />
    <s:param name="textKey" value="'confirmationDialog.fixedIpV4Route.delete.text'" />
  </s:url>
  <sj:submit
    listenTopics="fixedIpV4Route_deleteConfirmation"
    href="%{delete_confirmation_url}"
    targets="shared_dialog_box"
    replaceTarget="false"
    validate="true"
    validateFunction="validation"
    clearForm="false"
    cssStyle="display: none;"
  />
  <s:url var="fixedIpV4Route_delete_url" action="fixed-ip-v4-route-delete" />
  <sj:submit
    listenTopics="fixedIpV4Route_delete"
    href="%{fixedIpV4Route_delete_url}"
    targets="trash_box"
    replaceTarget="false"
    onSuccessTopics="fixedIpV4RouteTableUpdated"
    onErrorTopics="deleteError"
    clearForm="true"
    cssStyle="display: none;"
  />
</s:form>

