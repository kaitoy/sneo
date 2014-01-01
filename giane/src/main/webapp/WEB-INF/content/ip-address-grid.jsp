<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="ipAddress_grid_url" action="ip-address-grid">
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
  multiselect="true"
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
    hidden="true"
  />
  <sjg:gridColumn
    name="address"
    index="address"
    title="%{getText('ipAddress.address.label')}"
    sortable="true"
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
    search="true"
    searchoptions="{sopt:['eq','ne','lt','gt']}"
    width="50"
  />
</sjg:grid>

<s:form id="ipAddress_delete_form">
  <s:hidden id="ipAddress_deletingIdList" name="deletingIdList" />
  <s:url var="delete_confirmation_url" action="confirmation-dialog" escapeAmp="false">
    <s:param name="okTopic" value="'ipAddress_delete'" />
    <s:param name="textKey" value="'dialog.text.confirmation.ipAddress.delete'" />
  </s:url>
  <sj:submit
    listenTopics="ipAddress_deleteConfirmation"
    href="%{delete_confirmation_url}"
    targets="trash_box"
    replaceTarget="false"
    validate="false"
    clearForm="false"
    cssStyle="display: none;"
  />
  <s:url var="ipAddress_delete_url" action="ip-address-delete" />
  <sj:submit
    listenTopics="ipAddress_delete"
    href="%{ipAddress_delete_url}"
    targets="trash_box"
    replaceTarget="false"
    onSuccessTopics="ipAddressTableUpdated"
    onErrorTopics="deleteError"
    clearForm="true"
    cssStyle="display: none;"
  />
</s:form>
