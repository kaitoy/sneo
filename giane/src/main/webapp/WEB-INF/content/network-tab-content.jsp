<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="left-column">
  <div>
    <s:form id="network_form" theme="simple" cssClass="giane-form">
      <fieldset>
        <legend><s:text name="network.form" /></legend>
        <div>
          <s:hidden id="network_id" name="model.id" />
        </div>
        <div>
          <s:textfield
            id="network_name"
            name="model.name"
            label="%{getText('network.name.label')}"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span id="network_form_nameError"></span>
        </div>
        <div>
          <s:textarea
            id="network_descr"
            name="model.descr"
            label="%{getText('network.descr.label')}"
            cols="30"
            required="false"
            requiredposition="left"
            resizable="false"
            theme="xhtml"
          />
          <span id="network_form_descrError"></span>
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
                  <s:url var="network_create_url" action="network-create" />
                  <sj:submit
                    listenTopics="doCreate_network"
                    href="%{network_create_url}"
                    targets="trash_box"
                    replaceTarget="false"
                    indicator="network_create_indicator"
                    validate="true"
                    validateFunction="validation"
                    onBeforeTopics="removeErrors"
                    onSuccessTopics="removeErrors,networkTableUpdated"
                    onErrorTopics="createError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                </td>
                <td class="create-button-indicator-cell">
                  <img id="network_create_indicator" src="images/loading_small.gif" alt="Loading..." style="display: none;" />
                </td>
                <td class="update-button-cell">
                  <sj:submit
                    value="%{getText('form.updateButton.label')}"
                    button="true"
                    cssClass="giane-form-button"
                    onClickTopics="updateButtonClicked"
                  />
                  <s:url var="network_update_url" action="network-update" />
                  <sj:submit
                    listenTopics="doUpdate_network"
                    href="%{network_update_url}"
                    targets="trash_box"
                    replaceTarget="false"
                    indicator="network_update_indicator"
                    validate="true"
                    validateFunction="validation"
                    onBeforeTopics="removeErrors"
                    onSuccessTopics="removeErrors,networkTableUpdated"
                    onErrorTopics="updateError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                  <img id="network_update_indicator" src="images/loading_small.gif" alt="Loading..." style="display: none;" />
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
    <jsp:include page="network-grid.jsp" />
  </div>
</div>

<s:url var="network_url" action="network" escapeAmp="false">
  <s:param name="tabIndex" value="%{#parameters.tabIndex}" />
</s:url>
<sj:submit
  href="%{network_url}"
  formIds="network_form"
  targets="config_main"
  replaceTarget="false"
  indicator="config_main_indicator"
  validate="true"
  validateFunction="validation"
  listenTopics="network_rowDblClicked,network_configButtonClicked"
  onBeforeTopics="configMainPaneGoingForward"
  onCompleteTopics="configMainPaneCompleted"
  style="display: none;"
/>
