<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<s:url var="simulatorHome_url" action="simulator-home" />
<sj:div
  id="simulator_main"
  href="%{simulatorHome_url}"
  indicator="simulator_main_indicator"
  cssClass="giane-main"
 />

<img id="simulator_main_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" class="main-indicator"/>
