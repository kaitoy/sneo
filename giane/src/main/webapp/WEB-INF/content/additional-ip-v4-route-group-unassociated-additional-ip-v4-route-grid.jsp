<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="additionalIpV4RouteGroup_unassociated_additionalIpV4Route_grid_url" action="additional-ip-v4-route-group-unassociated-additional-ip-v4-route-grid" escapeAmp="false">
  <s:param name="additionalIpV4RouteGroup_id" value="%{#parameters.additionalIpV4RouteGroup_id}" />
</s:url>
<s:url var="additionalIpV4RouteGroup_unassociated_additionalIpV4Route_edit_grie_entry_url" action="additional-ip-v4-route-group-unassociated-additional-ip-v4-route-edit-grid-entry" escapeAmp="false">
  <s:param name="additionalIpV4RouteGroup_id" value="%{#parameters.additionalIpV4RouteGroup_id}" />
</s:url>

<sjg:grid
  id="additionalIpV4RouteGroup_unassociated_additionalIpV4Route_grid"
  caption="%{getText('additionalIpV4RouteGroup.unassociated.additionalIpV4Route.grid.caption')}"
  dataType="json"
  href="%{additionalIpV4RouteGroup_unassociated_additionalIpV4Route_grid_url}"
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
  editurl="%{additionalIpV4RouteGroup_unassociated_additionalIpV4Route_edit_grie_entry_url}"
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
    title="%{getText('additionalIpV4Route.id.label')}"
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
    title="%{getText('additionalIpV4Route.name.label')}"
    sortable="true"
    editable="true"
    edittype="text"
    search="true"
    searchoptions="{sopt:['eq','ne','bw','en','cn']}"
    width="150"
  />
</sjg:grid>
