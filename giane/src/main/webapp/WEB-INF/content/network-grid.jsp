<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="network_grid_url" action="network-grid"/>

<sjg:grid
  id="network_grid"
  caption="%{getText('network.grid.caption')}"
  dataType="json"
  href="%{network_grid_url}"
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
  onDblClickRowTopics="network_rowDblClicked"
  reloadTopics="networkTableUpdated"
>
  <sjg:gridColumn
    name="id"
    index="id"
    title="%{getText('network.id.label')}"
    formatter="integer"
    key="true"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','lt','gt']}"
    hidden="true"
    cssClass="id-colmn"
  />
  <sjg:gridColumn
    name="name"
    index="name"
    title="%{getText('network.name.label')}"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="100"
  />
  <sjg:gridColumn
    name="descr"
    index="descr"
    title="%{getText('network.descr.label')}"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="200"
    formatter="oneLine"
  />
</sjg:grid>

<s:form id="network_delete_form">
  <s:hidden id="network_deletingIdList" name="deletingIdList" />
  <s:url var="delete_confirmation_url" action="confirmation-dialog" escapeAmp="false">
    <s:param name="okTopic" value="'network_delete'" />
    <s:param name="textKey" value="'dialog.text.confirmation.network.delete'" />
  </s:url>
  <sj:submit
    listenTopics="network_deleteConfirmation"
    href="%{delete_confirmation_url}"
    targets="trash_box"
    replaceTarget="false"
    validate="false"
    clearForm="false"
    cssStyle="display: none;"
  />
  <s:url var="network_delete_url" action="network-delete" />
  <sj:submit
    listenTopics="network_delete"
    href="%{network_delete_url}"
    targets="trash_box"
    replaceTarget="false"
    onSuccessTopics="networkTableUpdated"
    onErrorTopics="deleteError"
    clearForm="true"
    cssStyle="display: none;"
  />
</s:form>

