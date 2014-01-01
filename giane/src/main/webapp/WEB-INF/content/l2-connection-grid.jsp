<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="l2Connection_grid_url" action="l2-connection-grid">
  <s:param name="network_id" value="%{#parameters.network_id}" />
</s:url>

<sjg:grid
  id="l2Connection_grid"
  caption="%{getText('l2Connection.grid.caption')}"
  dataType="json"
  href="%{l2Connection_grid_url}"
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
  gridview="true"
  onSelectRowTopics="rowSelected"
  onCompleteTopics="gridCompleted"
  onDblClickRowTopics="l2Connection_rowDblClicked"
  reloadTopics="l2ConnectionTableUpdated"
>
  <sjg:gridColumn
    name="id"
    index="id"
    title="%{getText('l2Connection.id.label')}"
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
    title="%{getText('l2Connection.name.label')}"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="200"
  />
</sjg:grid>

<s:form id="l2Connection_delete_form">
  <s:hidden id="l2Connection_deletingIdList" name="deletingIdList" />
  <s:url var="delete_confirmation_url" action="confirmation-dialog" escapeAmp="false">
    <s:param name="okTopic" value="'l2Connection_delete'" />
    <s:param name="textKey" value="'dialog.text.confirmation.l2Connection.delete'" />
  </s:url>
  <sj:submit
    listenTopics="l2Connection_deleteConfirmation"
    href="%{delete_confirmation_url}"
    targets="trash_box"
    replaceTarget="false"
    validate="false"
    clearForm="false"
    cssStyle="display: none;"
  />
  <s:url var="l2Connection_delete_url" action="l2-connection-delete" />
  <sj:submit
    listenTopics="l2Connection_delete"
    href="%{l2Connection_delete_url}"
    targets="trash_box"
    replaceTarget="false"
    onSuccessTopics="l2ConnectionTableUpdated"
    onErrorTopics="deleteError"
    clearForm="true"
    cssStyle="display: none;"
  />
</s:form>
