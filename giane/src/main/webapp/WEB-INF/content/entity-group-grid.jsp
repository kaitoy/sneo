<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="entityGroup_grid_url" action="%{#parameters.modelNameHyphen}-grid"/>

<sjg:grid
  id="%{#parameters.modelNameCamel}_grid"
  caption="%{#parameters.gridCaption}"
  dataType="json"
  href="%{entityGroup_grid_url}"
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
  onDblClickRowTopics="%{#parameters.modelNameCamel}_rowDblClicked"
  reloadTopics="%{#parameters.modelNameCamel}TableUpdated"
>
  <sjg:gridColumn
    name="id"
    index="id"
    title="%{getText('entityGroup.id.label')}"
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
    title="%{getText('entityGroup.name.label')}"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="400"
  />
  <sjg:gridColumn
    name="descr"
    index="descr"
    title="%{getText('entityGroup.descr.label')}"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="200"
    formatter="oneLine"
  />
</sjg:grid>

<s:form id="%{#parameters.modelNameCamel}_delete_form">
  <s:hidden id="%{#parameters.modelNameCamel}_deletingIdList" name="deletingIdList" />
  <s:url var="delete_confirmation_url" action="confirmation-dialog" escapeAmp="false">
    <s:param name="okTopic" value="#parameters.modelNameCamel[0] + '_delete'" />
    <s:param name="textKey" value="'confirmationDialog.' + #parameters.modelNameCamel[0] + '.delete.text'" />
  </s:url>
  <sj:submit
    listenTopics="%{#parameters.modelNameCamel}_deleteConfirmation"
    href="%{delete_confirmation_url}"
    targets="shared_dialog_box"
    replaceTarget="false"
    validate="true"
    validateFunction="validation"
    clearForm="false"
    cssStyle="display: none;"
  />
  <s:url var="entityGroup_delete_url" action="%{#parameters.modelNameHyphen}-delete" />
  <sj:submit
    listenTopics="%{#parameters.modelNameCamel}_delete"
    href="%{entityGroup_delete_url}"
    targets="trash_box"
    replaceTarget="false"
    onSuccessTopics="%{#parameters.modelNameCamel}TableUpdated"
    onErrorTopics="deleteError"
    clearForm="true"
    cssStyle="display: none;"
  />
</s:form>
