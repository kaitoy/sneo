<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="snmpAgent_with_trapTargetGroup_grid_url" action="snmp-agent-with-trap-target-group-grid">
  <s:param name="simulation_id" value="%{#parameters.simulation_id}" />
</s:url>

<sjg:grid
  id="snmpAgent_with_trapTargetGroup_grid"
  caption="%{getText('snmpAgent.with.trapTargetGroup.grid.caption')}"
  dataType="json"
  href="%{snmpAgent_with_trapTargetGroup_grid_url}"
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
  onDblClickRowTopics="snmpAgent_with_trapTargetGroup_rowDblClicked"
  reloadTopics="set_trapTargetGroup_to_snmpAgent_success"
>
  <sjg:gridColumn
    name="id"
    index="id"
    title="%{getText('snmpAgent.id.label')}"
    formatter="integer"
    key="true"
    sortable="true"
    editable="false"
    search="true"
    searchoptions="{sopt:['eq','ne','lt','gt']}"
    hidden="true"
  />
  <sjg:gridColumn
    name="address"
    index="address"
    title="%{getText('snmpAgent.address.label')}"
    sortable="true"
    editable="false"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="200"
  />
  <sjg:gridColumn
    name="hostNode"
    index="hostNode"
    title="%{getText('snmpAgent.hostNode.label')}"
    sortable="true"
    editable="false"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="200"
  />
  <sjg:gridColumn
    name="trapTargetGroup"
    index="trapTargetGroup"
    title="%{getText('snmpAgent.trapTargetGroup.label')}"
    sortable="true"
    editable="false"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="200"
  />
</sjg:grid>
