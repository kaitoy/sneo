<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="simulation_grid_url" action="simulation-grid" />

<sjg:grid
  id="%{#parameters.grid_id}"
  caption="%{getText('simulation.grid.caption')}"
  dataType="json"
  href="%{simulation_grid_url}"
  pager="true"
  toppager="false"
  navigator="true"
  navigatorAdd="false"
  navigatorEdit="false"
  navigatorView="true"
  navigatorViewOptions="{modal:true}"
  navigatorDelete="%{#parameters.navigatorDelete}"
  navigatorSearch="true"
  navigatorSearchOptions="{modal:true, drag:true, closeAfterSearch:true, closeAfterReset:true}"
  navigatorExtraButtons="%{#parameters.navigatorExtraButtons}"
  editinline="false"
  multiselect="%{#parameters.multiselect}"
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
  onDblClickRowTopics="%{#parameters.onDblClickRowTopics}"
  reloadTopics="%{#parameters.reloadTopics}"
>
  <sjg:gridColumn
    name="id"
    index="id"
    title="%{getText('simulation.id.label')}"
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
    title="%{getText('simulation.name.label')}"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="200"
  />
  <sjg:gridColumn
    name="network"
    index="network"
    title="%{getText('simulation.network.label')}"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="200"
  />
  <sjg:gridColumn
    name="descr"
    index="descr"
    title="%{getText('simulation.descr.label')}"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="200"
    formatter="oneLine"
  />
  <sjg:gridColumn
    name="running"
    index="running"
    title="%{getText('simulation.running.label')}"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="80"
    hidden="%{#parameters.hide_running}"
  />
</sjg:grid>

<s:if test="#parameters.navigatorExtraButtons != null">
  <s:form id="simulation_delete_form">
    <s:hidden id="simulation_deletingIdList" name="deletingIdList" />
    <s:url var="delete_confirmation_url" action="confirmation-dialog" escapeAmp="false">
      <s:param name="okTopic" value="'simulation_delete'" />
      <s:param name="textKey" value="'dialog.text.confirmation.simulation.delete'" />
    </s:url>
    <sj:submit
      listenTopics="simulation_deleteConfirmation"
      href="%{delete_confirmation_url}"
      targets="trash_box"
      replaceTarget="false"
      validate="true"
      validateFunction="validation"
      clearForm="false"
      cssStyle="display: none;"
    />
    <s:url var="simulation_delete_url" action="simulation-delete" />
    <sj:submit
      listenTopics="simulation_delete"
      href="%{simulation_delete_url}"
      targets="trash_box"
      replaceTarget="false"
      onSuccessTopics="simulationTableUpdated"
      onErrorTopics="deleteError"
      clearForm="true"
      cssStyle="display: none;"
    />
  </s:form>
</s:if>
