<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="giane-tab-content-form-column">
  <div>
    <s:form id="node_form" theme="simple" cssClass="giane-form">
      <fieldset>
        <legend><s:text name="node.form" /></legend>
        <div class="giane-form-field-box">
          <s:hidden id="node_id" name="model.id" />
        </div>
        <div class="giane-form-field-box">
          <s:textfield
            id="node_name"
            name="model.name"
            label="%{getText('node.name.label')}"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span class="giane-form-error-message" id="node_form_nameError"></span>
        </div>
        <div class="giane-form-field-box">
          <s:textfield
            id="node_ttl"
            name="model.ttl"
            label="%{getText('node.ttl.label')}"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span class="giane-form-error-message" id="node_form_ttlError"></span>
        </div>
        <div class="giane-form-field-box">
          <s:textarea
            id="node_descr"
            name="model.descr"
            label="%{getText('node.descr.label')}"
            required="false"
            requiredposition="left"
            theme="xhtml"
          />
          <span class="giane-form-error-message" id="node_form_descrError"></span>
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
                  <s:url var="node_create_url" action="node-create">
                    <s:param name="network_id" value="%{#parameters.network_id}" />
                  </s:url>
                  <sj:submit
                    listenTopics="doCreate_node"
                    href="%{node_create_url}"
                    targets="trash_box"
                    replaceTarget="false"
                    indicator="node_create_indicator"
                    validate="true"
                    validateFunction="validation"
                    onBeforeTopics="removeErrors"
                    onSuccessTopics="nodeTableUpdated"
                    onErrorTopics="createError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                </td>
                <td class="two-buttons-left-indicator-cell">
                  <img id="node_create_indicator" src="images/loading_small.gif" alt="Loading..." style="display: none;" />
                </td>
                <td class="two-buttons-second-cell">
                  <sj:submit
                    value="%{getText('form.updateButton.label')}"
                    button="true"
                    cssClass="giane-form-button"
                    onClickTopics="updateButtonClicked"
                  />
                  <s:url var="node_update_url" action="node-update">
                    <s:param name="network_id" value="%{#parameters.network_id}" />
                  </s:url>
                  <sj:submit
                    listenTopics="doUpdate_node"
                    href="%{node_update_url}"
                    targets="trash_box"
                    replaceTarget="false"
                    indicator="node_update_indicator"
                    validate="true"
                    validateFunction="validation"
                    onBeforeTopics="removeErrors"
                    onSuccessTopics="nodeTableUpdated"
                    onErrorTopics="updateError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                  <img id="node_update_indicator" src="images/loading_small.gif" alt="Loading..." style="display: none;" />
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
    <jsp:include page="node-grid.jsp" flush="true" />
  </div>
</div>

<s:url var="node_url" action="node" escapeAmp="false">
  <s:param name="tabIndex" value="%{#parameters.tabIndex}" />
</s:url>
<sj:submit
  href="%{node_url}"
  formIds="node_form"
  targets="config_main"
  replaceTarget="false"
  indicator="config_main_indicator"
  validate="true"
  validateFunction="checkRowSelection"
  listenTopics="node_rowDblClicked"
  onBeforeTopics="configMainPaneGoingForward_before"
  onAfterValidationTopics="configMainPaneGoingForward_after"
  onCompleteTopics="configMainPaneCompleted"
  style="display: none;"
/>

