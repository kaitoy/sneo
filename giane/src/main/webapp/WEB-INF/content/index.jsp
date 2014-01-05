<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <sj:head jqueryui="true" jquerytheme="black-tie"/>
    <!--link rel="stylesheet" type="text/css" href="css/cssreset-min.css" /-->
    <link rel="stylesheet" type="text/css" href="css/index.css" />
    <link rel="stylesheet" type="text/css" href="css/contents_container.css" />
    <link rel="stylesheet" type="text/css" href="css/main.css" />
    <link rel="stylesheet" type="text/css" href="css/form.css" />
    <link rel="stylesheet" type="text/css" href="css/association.css" />
    <link rel="stylesheet" type="text/css" href="css/odd_even_table.css" />
    <link rel="stylesheet" type="text/css" href="css/breadcrumbs.css" />
    <link rel="shortcut icon" type="image/png" href="images/favicon.png" />
    <script type="text/javascript" src="js/form.js"></script>
    <script type="text/javascript" src="js/validation.js"></script>
    <script type="text/javascript" src="js/grid.js"></script>
    <script type="text/javascript" src="js/error.js"></script>
    <script type="text/javascript" src="js/association.js"></script>
    <script type="text/javascript" src="js/association_group.js"></script>
    <script type="text/javascript" src="js/breadcrumbs.js"></script>
    <title>Giane</title>
  </head>
  <body>
    
    <div id="header">
      <span id="header_title">Giane</span>
      <s:url var="localeSelector_url" action="locale-selector" />
      <sj:div
        id="localeSelector"
        href="%{localeSelector_url}"
        indicator="localeSelector_indicator"
        onCompleteTopics="localeSelectorCompleted"
      />
      <span id="localeSelector_indicator_wapper">
        <img
          id="localeSelector_indicator"
          src="images/loading_small_white.gif"
          alt="Loading..."
          style="display: none;"
        />
      </span>
    </div>

    <s:url var="signIn_url" action="sign-in" />
    <sj:div
      id="contents_container"
      href="%{signIn_url}"
      indicator="contents_container_indicator"
      listenTopics="localeSelectorCompleted,localeChangeCompleted"
      deferredLoading="true"
    />

    <img id="contents_container_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" />
    <div id="trash_box" style="display: none;"></div>
    
    <s:form id="commonDialog_form" action="common-dialog">
      <s:hidden id="commonDialog_form_titleKey" name="dialogTitleKey" />
      <s:hidden id="commonDialog_form_textKey" name="dialogTextKey" />
      <sj:submit
        listenTopics="showCommonDialog"
        targets="trash_box"
        replaceTarget="false"
        clearForm="true"
        cssStyle="display: none;"
      />
    </s:form>

    <div id="footer">
      <a id="footer_url" href="https://github.com/kaitoy/sneo" target="_blank">https://github.com/kaitoy/sneo</a>
    </div>
  </body>
</html>

