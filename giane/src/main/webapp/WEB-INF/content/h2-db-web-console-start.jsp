<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<div class="left-column">
  <s:form id="h2DbWebConsole_form" theme="simple" cssClass="giane-form">
    <fieldset>
      <legend><s:text name="h2DbWebConsole.form" /></legend>
      <div>
        <s:textfield
          id="h2DbWebConsole_port"
          name="port"
          label="%{getText('h2DbWebConsole.port.label')}"
          required="true"
          requiredposition="left"
          theme="xhtml"
        />
        <span id="h2DbWebConsole_form_portError"></span>
      </div>
      <table class="submit-button-table">
        <tbody>
          <tr>
            <td class="single-button-cell">
              <s:url var="h2DbWebConsoleStart_url" action="h2-db-web-console-start" />
              <sj:submit
                value="%{getText('h2DbWebConsole.startButton.label')}"
                button="true"
                href="%{h2DbWebConsoleStart_url}"
                targets="h2DbWebConsole_box"
                replaceTarget="false"
                indicator="h2DbWebConsole_indicator"
                validate="true"
                validateFunction="validation"
                onBeforeTopics="removeErrors"
                onSuccessTopics="removeErrors"
                onErrorTopics="h2DbWebConsole_startError"
                clearForm="true"
              />
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
    </fieldset>
  </s:form>
</div>
