<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<s:form id="localeSelector_form" theme="simple">
  <s:select
    id="giane_locale"
    name="currentLocale"
    list="%{locales}"
    multiple="false"
    theme="xhtml"
    onchange="$.publish('localeChanged')"
  />
  <s:url var="changeLocale_url" action="change-locale" />
  <sj:submit
    listenTopics="localeChanged"
    href="%{changeLocale_url}"
    targets="trash_box"
    replaceTarget="false"
    indicator="localeSelector_indicator"
    onSuccessTopics="localeChangeCompleted"
    cssStyle="display: none;"
  />
</s:form>
