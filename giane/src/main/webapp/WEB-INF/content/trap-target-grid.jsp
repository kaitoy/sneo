<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="trapTarget_grid_url" action="trap-target-grid"/>

<sjg:grid
  id="trapTarget_grid"
  caption="%{getText('trapTarget.grid.caption')}"
  dataType="json"
  href="%{trapTarget_grid_url}"
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
  onSelectRowTopics="rowSelected"
  onCompleteTopics="gridCompleted"
  onDblClickRowTopics="trapTarget_rowDblClicked"
  reloadTopics="trapTargetTableUpdated"
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
    hidden="true"
  />
  <sjg:gridColumn
    name="name"
    index="name"
    title="%{getText('trapTarget.name.label')}"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="200"
  />
  <sjg:gridColumn
    name="address"
    index="address"
    title="%{getText('trapTarget.address.label')}"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="200"
  />
  <sjg:gridColumn
    name="port"
    index="port"
    title="%{getText('trapTarget.port.label')}"
    formatter="integer"
    key="true"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','lt','gt']}"
    width="100"
  />
  <sjg:gridColumn
    name="descr"
    index="descr"
    title="%{getText('trapTarget.descr.label')}"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="200"
    formatter="oneLine"
  />
</sjg:grid>

<s:form id="trapTarget_delete_form">
  <s:hidden id="trapTarget_deletingIdList" name="deletingIdList" />
  <s:url var="delete_confirmation_url" action="confirmation-dialog" escapeAmp="false">
    <s:param name="okTopic" value="'trapTarget_delete'" />
    <s:param name="textKey" value="'confirmationDialog.trapTarget.delete.text'" />
  </s:url>
  <sj:submit
    listenTopics="trapTarget_deleteConfirmation"
    href="%{delete_confirmation_url}"
    targets="shared_dialog_box"
    replaceTarget="false"
    validate="true"
    validateFunction="validation"
    clearForm="false"
    cssStyle="display: none;"
  />
  <s:url var="trapTarget_delete_url" action="trap-target-delete" />
  <sj:submit
    listenTopics="trapTarget_delete"
    href="%{trapTarget_delete_url}"
    targets="trash_box"
    replaceTarget="false"
    onSuccessTopics="trapTargetTableUpdated"
    onErrorTopics="deleteError"
    clearForm="true"
    cssStyle="display: none;"
  />
</s:form>
