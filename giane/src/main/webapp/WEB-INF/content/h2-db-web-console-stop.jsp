<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<div class="left-column">
  <div>
    <a href="<s:property value="url" />" target="_blank">
      <s:text name="h2DbWebConsole.openButton.label"/>
    </a>
  </div>
  
  <div class="giane-form">
    <table class="submit-button-table">
      <tbody>
        <tr>
          <td class="single-button-cell">
            <s:url var="h2DbWebConsoleStop_url" action="h2-db-web-console-stop" />
            <sj:a
              button="true"
              href="%{h2DbWebConsoleStop_url}"
              targets="h2DbWebConsole_box"
              replaceTarget="false"
              indicator="h2DbWebConsole_indicator"
            >
              <s:text name="h2DbWebConsole.stopButton.label" />
            </sj:a>
          </td>
          <td class="single-button-indicator-cell">
            <img
              id="h2DbWebConsole_indicator"
              src="images/loading_small.gif"
              alt="Loading..."
              style="display: none;"
            />
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</div>