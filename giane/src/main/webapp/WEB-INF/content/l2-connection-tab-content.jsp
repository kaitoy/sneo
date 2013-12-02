<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="left-column">
  <div>
    <s:form id="l2Connection_form" theme="simple" cssClass="giane-form">
      <fieldset>
        <legend><s:text name="l2Connection.form" /></legend>
        <div>
          <s:hidden id="l2Connection_id" name="model.id" />
        </div>
        <div>
          <s:textfield
            id="l2Connection_name"
            name="model.name"
            label="%{getText('l2Connection.name.label')}"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span id="l2Connection_form_nameError"></span>
        </div>
        <div>
          <table class="submit-button-table">
            <tbody>
              <tr>
                <td class="create-button-cell">
                  <sj:submit
                    value="%{getText('form.createButton.label')}"
                    button="true"
                    cssClass="giane-form-button"
                    onClickTopics="createButtonClicked"
                  />
                  <s:url var="l2Connection_create_url" action="l2-connection-create">
                    <s:param name="network_id" value="%{#parameters.network_id}" />
                  </s:url>
                  <sj:submit
                    listenTopics="doCreate_l2Connection"
                    href="%{l2Connection_create_url}"
                    targets="trash_box"
                    replaceTarget="false"
                    indicator="l2Connection_create_indicator"
                    validate="true"
                    validateFunction="validation"
                    onBeforeTopics="removeErrors"
                    onSuccessTopics="removeErrors,l2ConnectionTableUpdated"
                    onErrorTopics="createError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                </td>
                <td class="create-button-indicator-cell">
                  <img
                    id="l2Connection_create_indicator"
                    src="images/loading_small.gif"
                    alt="Loading..."
                    style="display: none;"
                  />
                </td>
                <td class="update-button-cell">
                  <sj:submit
                    value="%{getText('form.updateButton.label')}"
                    button="true"
                    cssClass="giane-form-button"
                    onClickTopics="updateButtonClicked"
                  />
                  <s:url var="l2Connection_update_url" action="l2-connection-update">
                    <s:param name="network_id" value="%{#parameters.network_id}" />
                  </s:url>
                  <sj:submit
                    listenTopics="doUpdate_l2Connection"
                    href="%{l2Connection_update_url}"
                    targets="trash_box"
                    replaceTarget="false"
                    indicator="l2Connection_update_indicator"
                    validate="true"
                    validateFunction="validation"
                    onBeforeTopics="removeErrors"
                    onSuccessTopics="removeErrors,l2ConnectionTableUpdated"
                    onErrorTopics="updateError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                  <img
                    id="l2Connection_update_indicator"
                    src="images/loading_small.gif"
                    alt="Loading..."
                    style="display: none;"
                  />
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </fieldset>
    </s:form>
  </div>
</div>

<div class="right-column">
  <div class="grid-box">
    <jsp:include page="l2-connection-grid.jsp" />
  </div>
</div>

<s:url var="l2Connection_url" action="l2-connection" />
<sj:submit
  href="%{l2Connection_url}"
  formIds="l2Connection_form"
  targets="main"
  replaceTarget="false"
  indicator="main_indicator"
  validate="true"
  validateFunction="validation"
  listenTopics="l2Connection_rowDblClicked,l2Connection_configButtonClicked"
  onBeforeTopics="mainPaneGoingForward"
  onCompleteTopics="mainPaneCompleted"
  style="display: none;"
/>

