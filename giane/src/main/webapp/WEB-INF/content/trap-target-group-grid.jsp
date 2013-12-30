<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="trapTargetGroup_grid_url" action="trap-target-group-grid"/>

<sjg:grid
  id="trapTargetGroup_grid"
  caption="%{getText('trapTargetGroup.grid.caption')}"
  dataType="json"
  href="%{trapTargetGroup_grid_url}"
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
  onDblClickRowTopics="trapTargetGroup_rowDblClicked"
  reloadTopics="trapTargetGroupTableUpdated"
>
  <sjg:gridColumn
    name="id"
    index="id"
    title="%{getText('trapTargetGroup.id.label')}"
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
    title="%{getText('trapTargetGroup.name.label')}"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="400"
  />
  <sjg:gridColumn
    name="descr"
    index="descr"
    title="%{getText('trapTargetGroup.descr.label')}"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="200"
    formatter="oneLine"
  />
</sjg:grid>

<s:form id="trapTargetGroup_delete_form">
  <s:hidden id="trapTargetGroup_deletingIdList" name="deletingIdList" />
  <s:url var="delete_confirmation_url" action="confirmation-dialog" escapeAmp="false">
    <s:param name="okTopic" value="'trapTargetGroup_delete'" />
    <s:param name="textKey" value="'confirmationDialog.trapTargetGroup.delete.text'" />
  </s:url>
  <sj:submit
    listenTopics="trapTargetGroup_deleteConfirmation"
    href="%{delete_confirmation_url}"
    targets="shared_dialog_box"
    replaceTarget="false"
    validate="true"
    validateFunction="validation"
    clearForm="false"
    cssStyle="display: none;"
  />
  <s:url var="trapTargetGroup_delete_url" action="trap-target-group-delete" />
  <sj:submit
    listenTopics="trapTargetGroup_delete"
    href="%{trapTargetGroup_delete_url}"
    targets="trash_box"
    replaceTarget="false"
    onSuccessTopics="trapTargetGroupTableUpdated"
    onErrorTopics="deleteError"
    clearForm="true"
    cssStyle="display: none;"
  />
</s:form>
