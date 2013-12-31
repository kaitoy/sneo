<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="giane-tab-content-form-column">
  <div>
    <s:form id="l2Connection_form" theme="simple" cssClass="giane-form">
      <fieldset>
        <legend><s:text name="l2Connection.form" /></legend>
        <div class="giane-form-field-box">
          <s:hidden id="l2Connection_id" name="model.id" />
        </div>
        <div class="giane-form-field-box">
          <s:textfield
            id="l2Connection_name"
            name="model.name"
            label="%{getText('l2Connection.name.label')}"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span class="giane-form-error-message" id="l2Connection_form_nameError"></span>
        </div>
        <div>
          <table class="submit-button-table">
            <tbody>
              <tr>
                <td class="two-buttons-first-cell">
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
                    onSuccessTopics="l2ConnectionTableUpdated"
                    onErrorTopics="createError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                </td>
                <td class="two-buttons-left-indicator-cell">
                  <img
                    id="l2Connection_create_indicator"
                    src="images/loading_small.gif"
                    alt="Loading..."
                    style="display: none;"
                  />
                </td>
                <td class="two-buttons-second-cell">
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
                    onSuccessTopics="l2ConnectionTableUpdated"
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

<div class="giane-tab-content-grid-column">
  <div class="giane-grid-box">
    <jsp:include page="l2-connection-grid.jsp" flush="true" />
  </div>
</div>

<s:url var="l2Connection_url" action="l2-connection" escapeAmp="false">
  <s:param name="tabIndex" value="%{#parameters.tabIndex}" />
</s:url>
<sj:submit
  href="%{l2Connection_url}"
  formIds="l2Connection_form"
  targets="config_main"
  replaceTarget="false"
  indicator="config_main_indicator"
  validate="true"
  validateFunction="checkRowSelection"
  listenTopics="l2Connection_rowDblClicked"
  onBeforeTopics="mainPaneGoingForward_before"
  onAfterValidationTopics="mainPaneGoingForward_after"
  onCompleteTopics="mainPaneCompleted"
  style="display: none;"
/>

