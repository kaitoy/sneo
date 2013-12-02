<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<s:url var="home_url" action="home" />
<sj:a
  id="signIn_button"
  href="%{home_url}"
  targets="main"
  replaceTarget="false"
  button="true"
  indicator="main_indicator"
  onCompleteTopics="mainPaneCompleted"
>
  <s:text name="signIn.button.label" />
</sj:a>


