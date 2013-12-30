<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="snmpAgent_grid_url" action="snmp-agent-grid">
  <s:param name="node_id" value="%{#parameters.node_id}" />
</s:url>
<s:url var="fileMibFormat_surl" action="file-mib-format" />

<sjg:grid
  id="snmpAgent_grid"
  caption="%{getText('snmpAgent.grid.caption')}"
  dataType="json"
  href="%{snmpAgent_grid_url}"
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
  onDblClickRowTopics="snmpAgent_rowDblClicked"
  reloadTopics="snmpAgentTableUpdated"
>
  <sjg:gridColumn
    name="id"
    index="id"
    title="%{getText('snmpAgent.id.label')}"
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
    title="%{getText('snmpAgent.address.label')}"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="100"
  />
  <sjg:gridColumn
    name="port"
    index="port"
    title="%{getText('snmpAgent.port.label')}"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','lt','gt']}"
    width="30"
  />
  <sjg:gridColumn
    name="communityName"
    index="communityName"
    title="%{getText('snmpAgent.communityName.label')}"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="60"
  />
  <sjg:gridColumn
    name="securityName"
    index="securityName"
    title="%{getText('snmpAgent.securityName.label')}"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="60"
  />
  <sjg:gridColumn
    name="fileMibPath"
    index="fileMibPath"
    title="%{getText('snmpAgent.fileMibPath.label')}"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="100"
  />
  <sjg:gridColumn
    name="fileMibFormat"
    index="fileMibFormat"
    title="%{getText('snmpAgent.fileMibFormat.label')}"
    sortable="true"
    search="true"
    searchtype="select"
    surl="%{fileMibFormat_surl}"
    searchoptions="{sopt:['eq','ne']}"
    width="50"
  />
  <sjg:gridColumn
    name="communityStringIndexList"
    index="communityStringIndexList"
    title="%{getText('snmpAgent.communityStringIndexList.label')}"
    sortable="true"
    search="false"
    width="70"
  />
</sjg:grid>

<s:form id="snmpAgent_delete_form">
  <s:hidden id="snmpAgent_deletingIdList" name="deletingIdList" />
  <s:url var="delete_confirmation_url" action="confirmation-dialog" escapeAmp="false">
    <s:param name="okTopic" value="'snmpAgent_delete'" />
    <s:param name="textKey" value="'confirmationDialog.snmpAgent.delete.text'" />
  </s:url>
  <sj:submit
    listenTopics="snmpAgent_deleteConfirmation"
    href="%{delete_confirmation_url}"
    targets="shared_dialog_box"
    replaceTarget="false"
    validate="true"
    validateFunction="validation"
    clearForm="false"
    cssStyle="display: none;"
  />
  <s:url var="snmpAgent_delete_url" action="snmp-agent-delete" />
  <sj:submit
    listenTopics="snmpAgent_delete"
    href="%{snmpAgent_delete_url}"
    targets="trash_box"
    replaceTarget="false"
    onSuccessTopics="snmpAgentTableUpdated"
    onErrorTopics="deleteError"
    clearForm="true"
    cssStyle="display: none;"
  />
</s:form>
