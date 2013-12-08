<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<div id="h2DbWebConsole_box">
  <s:if test="#h2DbWebServerRunning == true ">
    <jsp:include page="h2-db-web-console-stop.jsp"></jsp:include>
  </s:if>
  <s:else>
    <jsp:include page="h2-db-web-console-start.jsp"></jsp:include>
  </s:else>
</div>

<img
  id="h2DbWebConsole_indicator"
  src="images/loading_small.gif"
  alt="Loading..."
  style="display: none;"
/>
