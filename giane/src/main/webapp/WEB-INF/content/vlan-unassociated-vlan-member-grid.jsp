<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="vlan_unassociated_vlanMember_grid_url" action="vlan-unassociated-vlan-member-grid" escapeAmp="false">
  <s:param name="node_id" value="%{#parameters.node_id}" />
  <s:param name="vlan_id" value="%{#parameters.vlan_id}" />
</s:url>
<s:url var="vlan_unassociated_vlanMember_edit_grie_entry_url" action="vlan-unassociated-vlan-member-edit-grid-entry" escapeAmp="false">
  <s:param name="node_id" value="%{#parameters.node_id}" />
  <s:param name="vlan_id" value="%{#parameters.vlan_id}" />
</s:url>

<sjg:grid
  id="vlan_unassociated_vlanMember_grid"
  caption="%{getText('vlan.unassociated.vlanMember.grid.caption')}"
  dataType="json"
  href="%{vlan_unassociated_vlanMember_grid_url}"
  pager="false"
  toppager="false"
  navigator="false"
  navigatorAdd="false"
  navigatorEdit="false"
  navigatorView="false"
  navigatorViewOptions="{modal:true}"
  navigatorDelete="false"
  navigatorDeleteOptions="{modal:true, drag:true, reloadAfterSubmit:true, width:300, left:0}"
  navigatorSearch="false"
  navigatorSearchOptions="{modal:true, drag:true, closeAfterSearch:true, closeAfterReset:true}"
  editurl="%{vlan_unassociated_vlanMember_edit_grie_entry_url}"
  editinline="false"
  multiselect="true"
  viewrecords="false"
  viewsortcols="[true, 'vertical', true]"
  gridModel="gridModel"
  rownumbers="false"
  height="400"
  width="300"
  loadonce="true"
  scroll="true"
  shrinkToFit="true"
  altRows="true"
  gridview="true"
  hidegrid="false"
>
  <sjg:gridColumn
    name="id"
    index="id"
    title="%{getText('vlanMember.id.label')}"
    formatter="integer"
    key="true"
    sortable="true"
    search="true"
    searchoptions="{sopt:['eq','ne','lt','gt']}"
    width="50"
  />
  <sjg:gridColumn
    name="name"
    index="name"
    title="%{getText('vlanMember.name.label')}"
    sortable="true"
    editable="true"
    edittype="text"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="150"
  />
</sjg:grid>
