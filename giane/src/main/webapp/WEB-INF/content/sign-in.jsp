<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<s:url var="accordion_url" action="accordion" />
<sj:a
  id="signIn_button"
  href="%{accordion_url}"
  targets="contents_container"
  replaceTarget="false"
  button="true"
  indicator="contents_container_indicator"
>
  <s:text name="signIn.button.label" />
</sj:a>


