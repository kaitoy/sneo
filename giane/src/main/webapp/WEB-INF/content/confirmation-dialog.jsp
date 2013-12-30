<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<sj:dialog
  id="confirmation_dialog"
  autoOpen="true"
  modal="true"
  title="%{getText('confirmationDialog.title')}"
  buttons="{
    'OK': function() {
            $.publish('%{#parameters.okTopic}');
            $('#confirmation_dialog').dialog('close');
          },
    'Cancel': function() {$('#confirmation_dialog').dialog('close');}
  }"
  dialogClass="giane-dialog"
  draggable="true"
  height="150"
  width="200"
>
  <s:text name="%{#parameters.textKey}" />
</sj:dialog>
