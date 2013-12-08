<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<sj:accordion autoHeight="false">
  <s:url var="config_workspace_url" action="config-workspace"/>
  <sj:accordionItem title="%{getText('accordion.item.label.config')}" >
    <sj:div href="%{config_workspace_url}" />
  </sj:accordionItem>
  
  <s:url var="tool_workspace_url" action="tool-workspace"/>
  <sj:accordionItem title="%{getText('accordion.item.label.tool')}" >
    <sj:div href="%{tool_workspace_url}" />
  </sj:accordionItem>
</sj:accordion>
