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
    <script type="text/javascript" src="js/start_simulator.js"></script>
    <script type="text/javascript" src="js/breadcrumbs.js"></script>
    <title>Giane</title>
  </head>
  <body>
    <div id="header">Giane</div>

      <ol id="breadcrumbs"></ol>

      <div id="main">
        <s:url var="signIn_url" action="sign-in" />
        <sj:div
          id="signIn"
          href="%{signIn_url}"
          onBeforeTopics="startingMainPane"
          indicator="main_indicator"
         />
      </div>

      <img id="main_indicator" src="images/loading_big.gif" alt="Loading..." style="display: none;" />

      <div id="trash_box" style="display: none;"></div>
      <div id="shared_dialog_box" style="display: none;"></div>

    <div id="footer">Footer</div>
  </body>
</html>

