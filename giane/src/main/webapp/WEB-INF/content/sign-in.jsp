<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<s:url var="config_top_url" action="config-top" />
<sj:a
  id="signIn_button"
  href="%{config_top_url}"
  targets="main"
  replaceTarget="false"
  button="true"
  indicator="main_indicator"
>
  <s:text name="signIn.button.label" />
</sj:a>


