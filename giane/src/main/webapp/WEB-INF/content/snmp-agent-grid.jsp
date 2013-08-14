<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="snmpAgent_grid_url" action="snmp-agent-grid">
  <s:param name="node_id" value="%{#parameters.node_id}" />
</s:url>
<s:url var="snmpAgent_edit_grid_entry_url" action="snmp-agent-edit-grid-entry">
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
  navigatorDelete="true"
  navigatorDeleteOptions="{modal:true, drag:true, reloadAfterSubmit:true, width:300, left:0}"
  navigatorSearch="true"
  navigatorSearchOptions="{modal:true, drag:true, closeAfterSearch:true, closeAfterReset:true}"
  editurl="%{snmpAgent_edit_grid_entry_url}"
  editinline="false"
  multiselect="false"
  viewrecords="true"
  viewsortcols="[true, 'vertical', true]"
  gridModel="gridModel"
  rowNum="15"
  rownumbers="true"
  width="650"
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
    width="50"
  />
  <sjg:gridColumn
    name="address"
    index="address"
    title="%{getText('snmpAgent.address.label')}"
    sortable="true"
    editable="true"
    edittype="text"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="100"
  />
  <sjg:gridColumn
    name="port"
    index="port"
    title="%{getText('snmpAgent.port.label')}"
    sortable="true"
    editable="true"
    edittype="text"
    search="true"
    searchoptions="{sopt:['eq','ne','lt','gt']}"
    width="50"
  />
  <sjg:gridColumn
    name="communityName"
    index="communityName"
    title="%{getText('snmpAgent.communityName.label')}"
    sortable="true"
    editable="true"
    edittype="text"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="100"
  />
  <sjg:gridColumn
    name="securityName"
    index="securityName"
    title="%{getText('snmpAgent.securityName.label')}"
    sortable="true"
    editable="true"
    edittype="text"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="100"
  />
  <sjg:gridColumn
    name="fileMibPath"
    index="fileMibPath"
    title="%{getText('snmpAgent.fileMibPath.label')}"
    sortable="true"
    editable="true"
    edittype="text"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="100"
  />
  <sjg:gridColumn
    name="fileMibFormat"
    index="fileMibFormat"
    title="%{getText('snmpAgent.fileMibFormat.label')}"
    sortable="true"
    editable="true"
    edittype="select"
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
    editable="true"
    edittype="text"
    search="false"
    width="50"
  />
</sjg:grid>
