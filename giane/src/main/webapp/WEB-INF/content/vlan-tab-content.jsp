<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="giane-tab-content-form-column">
  <div>
    <s:form id="vlan_form" theme="simple" cssClass="giane-form">
      <fieldset>
        <legend><s:text name="vlan.form" /></legend>
        <div class="giane-form-field-box">
          <s:hidden id="vlan_id" name="model.id" />
        </div>
        <div class="giane-form-field-box">
          <s:textfield
            id="vlan_name"
            name="model.name"
            label="%{getText('vlan.name.label')}"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span class="giane-form-error-message" id="vlan_form_nameError"></span>
        </div>
        <div class="giane-form-field-box">
          <s:textfield
            id="vlan_vid"
            name="model.vid"
            label="%{getText('vlan.vid.label')}"
            required="true" requiredposition="left"
            theme="xhtml"
          />
          <span class="giane-form-error-message" id="vlan_form_vidError"></span>
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
                  <s:url var="vlan_create_url" action="vlan-create">
                    <s:param name="node_id" value="%{#parameters.node_id}" />
                  </s:url>
                  <sj:submit
                    listenTopics="doCreate_vlan"
                    href="%{vlan_create_url}"
                    targets="trash_box"
                    replaceTarget="false"
                    indicator="vlan_create_indicator"
                    validate="true"
                    validateFunction="validation"
                    onBeforeTopics="removeErrors"
                    onSuccessTopics="vlanTableUpdated"
                    onErrorTopics="createError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                </td>
                <td class="two-buttons-left-indicator-cell">
                  <img
                    id="vlan_create_indicator"
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
                  <s:url var="vlan_update_url" action="vlan-update">
                    <s:param name="node_id" value="%{#parameters.node_id}" />
                  </s:url>
                <sj:submit
                  listenTopics="doUpdate_vlan"
                  href="%{vlan_update_url}"
                  targets="trash_box"
                  replaceTarget="false"
                  indicator="vlan_update_indicator"
                  validate="true"
                  validateFunction="validation"
                  onBeforeTopics="removeErrors"
                  onSuccessTopics="vlanTableUpdated"
                  onErrorTopics="updateError"
                  clearForm="true"
                  cssStyle="display: none;"
                />
                <img id="vlan_update_indicator" src="images/loading_small.gif" alt="Loading..." style="display: none;" />
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
    <jsp:include page="vlan-grid.jsp" flush="true" />
  </div>
</div>

<s:url var="vlan_url" action="vlan" escapeAmp="false">
  <s:param name="tabIndex" value="%{#parameters.tabIndex}" />
  <s:param name="breadcrumbsId" value="%{#parameters.breadcrumbsId}" />
</s:url>
<sj:submit
  href="%{vlan_url}"
  formIds="vlan_form"
  targets="config_main"
  replaceTarget="false"
  indicator="config_main_indicator"
  validate="true"
  validateFunction="checkRowSelection"
  listenTopics="vlan_rowDblClicked"
  onBeforeTopics="mainPaneGoingForward_before"
  onAfterValidationTopics="mainPaneGoingForward_after"
  onCompleteTopics="mainPaneCompleted"
  style="display: none;"
/>
