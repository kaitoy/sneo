<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="ipV4Route_grid_url" action="%{#parameters.modelNameHyphen}-grid">
  <s:param name="node_id" value="%{#parameters.node_id}" />
</s:url>

<sjg:grid
  id="%{#parameters.modelNameCamel}_grid"
  caption="%{#parameters.gridCaption}"
  dataType="json"
  href="%{ipV4Route_grid_url}"
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
  onDblClickRowTopics="%{#parameters.modelNameCamel}_rowDblClicked"
  reloadTopics="%{#parameters.modelNameCamel}TableUpdated"
>
  <sjg:gridColumn
    name="id"
    index="id"
    title="%{getText('ipV4Route.id.label')}"
    formatter="integer"
    key="true"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','lt','gt']}"
    hidden="true"
  />
  <s:if test="#parameters.usesNameColmn">
    <sjg:gridColumn
      name="name"
      index="name"
      title="%{getText('ipV4Route.name.label')}"
      sortable="true"
      search="true"
      searchoptions="{sopt:['eq','ne','bw','en','cn']}"
      width="200"
    />
  </s:if>
  <sjg:gridColumn
    name="networkDestination"
    index="networkDestination"
    title="%{getText('ipV4Route.networkDestination.label')}"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="100"
  />
  <sjg:gridColumn
    name="netmask"
    index="netmask"
    title="%{getText('ipV4Route.netmask.label')}"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="100"
  />
  <sjg:gridColumn
    name="gateway"
    index="gateway"
    title="%{getText('ipV4Route.gateway.label')}"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="100"
  />
  <sjg:gridColumn
    name="metric"
    index="metric"
    title="%{getText('ipV4Route.metric.label')}"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','lt','gt']}"
    width="30"
  />
  <s:if test="#parameters.usesDescrColmn">
    <sjg:gridColumn
      name="descr"
      index="descr"
      title="%{getText('ipV4Route.descr.label')}"
      sortable="true"
      search="true"
      searchoptions="{sopt:['eq','ne','bw','en','cn']}"
      width="200"
      formatter="oneLine"
    />
  </s:if>
</sjg:grid>

<s:form id="%{#parameters.modelNameCamel}_delete_form">
  <s:hidden id="%{#parameters.modelNameCamel}_deletingIdList" name="deletingIdList" />
  <s:url var="delete_confirmation_url" action="confirmation-dialog" escapeAmp="false">
    <s:param name="okTopic" value="#parameters.modelNameCamel[0] + '_delete'" />
    <s:param name="textKey" value="'dialog.text.confirmation.' + #parameters.modelNameCamel[0] + '.delete'" />
  </s:url>
  <sj:submit
    listenTopics="%{#parameters.modelNameCamel}_deleteConfirmation"
    href="%{delete_confirmation_url}"
    targets="trash_box"
    replaceTarget="false"
    validate="false"
    clearForm="false"
    cssStyle="display: none;"
  />
  <s:url var="ipV4Route_delete_url" action="%{#parameters.modelNameHyphen}-delete" />
  <sj:submit
    listenTopics="%{#parameters.modelNameCamel}_delete"
    href="%{ipV4Route_delete_url}"
    targets="trash_box"
    replaceTarget="false"
    onSuccessTopics="%{#parameters.modelNameCamel}TableUpdated"
    onErrorTopics="deleteError"
    clearForm="true"
    cssStyle="display: none;"
  />
</s:form>

