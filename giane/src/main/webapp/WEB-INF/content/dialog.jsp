<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<sj:dialog
  showEffect="scale"
  hideEffect="puff"
  autoOpen="true"
  modal="true"
  title="%{getText(dialogTitleKey)}"
  dialogClass="dialog"
>
  <s:text name="%{dialogTextKey}" />
</sj:dialog>
