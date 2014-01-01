<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="node_grid_url" action="node-grid">
  <s:param name="network_id" value="%{#parameters.network_id}" />
</s:url>

<sjg:grid
  id="node_grid"
  caption="%{getText('node.grid.caption')}"
  dataType="json"
  href="%{node_grid_url}"
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
  onDblClickRowTopics="node_rowDblClicked"
  reloadTopics="nodeTableUpdated"
>
  <sjg:gridColumn
    name="id"
    index="id"
    title="%{getText('node.id.label')}"
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
    title="%{getText('node.name.label')}"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="200"
  />
  <sjg:gridColumn
    name="ttl"
    index="ttl"
    title="%{getText('node.ttl.label')}"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','lt','gt']}"
    width="50"
  />
  <sjg:gridColumn
    name="descr"
    index="descr"
    title="%{getText('node.descr.label')}"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="200"
    formatter="oneLine"
  />
</sjg:grid>

<s:form id="node_delete_form">
  <s:hidden id="node_deletingIdList" name="deletingIdList" />
  <s:url var="delete_confirmation_url" action="confirmation-dialog" escapeAmp="false">
    <s:param name="okTopic" value="'node_delete'" />
    <s:param name="textKey" value="'dialog.text.confirmation.node.delete'" />
  </s:url>
  <sj:submit
    listenTopics="node_deleteConfirmation"
    href="%{delete_confirmation_url}"
    targets="trash_box"
    replaceTarget="false"
    validate="false"
    clearForm="false"
    cssStyle="display: none;"
  />
  <s:url var="node_delete_url" action="node-delete" />
  <sj:submit
    listenTopics="node_delete"
    href="%{node_delete_url}"
    targets="trash_box"
    replaceTarget="false"
    onSuccessTopics="nodeTableUpdated"
    onErrorTopics="deleteError"
    clearForm="true"
    cssStyle="display: none;"
  />
</s:form>
