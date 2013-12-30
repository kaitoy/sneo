<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="giane-tab-content-form-column">
  <div>
    <s:form id="physicalNetworkInterface_form" theme="simple" cssClass="giane-form">
      <fieldset>
        <legend><s:text name="physicalNetworkInterface.form" /></legend>
        <div class="giane-form-field-box">
          <s:hidden id="physicalNetworkInterface_id" name="model.id" />
        </div>
        <div class="giane-form-field-box">
          <s:textfield
            id="physicalNetworkInterface_name"
            name="model.name"
            label="%{getText('physicalNetworkInterface.name.label')}"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span class="giane-form-error-message" id="physicalNetworkInterface_form_nameError"></span>
        </div>
        <div class="giane-form-field-box">
          <s:checkbox
            id="physicalNetworkInterface_trunk"
            name="model.trunk"
            label="%{getText('physicalNetworkInterface.trunk.label')}"
            value="physicalNetworkInterface_trunk"
            required="false"
            requiredposition="left"
            theme="xhtml"
          />
          <span class="giane-form-error-message" id="physicalNetworkInterface_form_trunkError"></span>
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
                  <s:url var="physicalNetworkInterface_create_url" action="physical-network-interface-create">
                    <s:param name="node_id" value="%{#parameters.node_id}" />
                  </s:url>
                  <sj:submit
                    listenTopics="doCreate_physicalNetworkInterface"
                    href="%{physicalNetworkInterface_create_url}"
                    targets="trash_box"
                    replaceTarget="false"
                    indicator="physicalNetworkInterface_create_indicator"
                    validate="true"
                    validateFunction="validation"
                    onBeforeTopics="removeErrors"
                    onSuccessTopics="removeErrors,physicalNetworkInterfaceTableUpdated"
                    onErrorTopics="createError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                </td>
                <td class="two-buttons-left-indicator-cell">
                  <img
                    id="physicalNetworkInterface_create_indicator"
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
                  <s:url var="physicalNetworkInterface_update_url" action="physical-network-interface-update">
                    <s:param name="node_id" value="%{#parameters.node_id}" />
                  </s:url>
                  <sj:submit
                    listenTopics="doUpdate_physicalNetworkInterface"
                    href="%{physicalNetworkInterface_update_url}"
                    targets="trash_box"
                    replaceTarget="false"
                    indicator="physicalNetworkInterface_update_indicator"
                    validate="true"
                    validateFunction="validation"
                    onBeforeTopics="removeErrors"
                    onSuccessTopics="removeErrors,physicalNetworkInterfaceTableUpdated"
                    onErrorTopics="updateError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                  <img
                    id="physicalNetworkInterface_update_indicator"
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
    <jsp:include page="physical-network-interface-grid.jsp" flush="true" />
  </div>
</div>

<s:url var="physicalNetworkInterface_url" action="physical-network-interface">
  <s:param name="tabIndex" value="%{#parameters.tabIndex}" />
</s:url>
<sj:submit
  href="%{physicalNetworkInterface_url}"
  formIds="physicalNetworkInterface_form"
  targets="config_main"
  replaceTarget="false"
  indicator="config_main_indicator"
  validate="true"
  validateFunction="checkRowSelection"
  listenTopics="physicalNetworkInterface_rowDblClicked"
  onBeforeTopics="configMainPaneGoingForward_before"
  onAfterValidationTopics="configMainPaneGoingForward_after"
  onCompleteTopics="configMainPaneCompleted"
  style="display: none;"
/>
