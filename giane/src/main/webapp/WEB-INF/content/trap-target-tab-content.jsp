<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="giane-tab-content-form-column">
  <div>
    <s:form id="trapTarget_form" theme="simple" cssClass="giane-form">
      <fieldset>
        <legend><s:text name="trapTarget.form" /></legend>
        <div class="giane-form-field-box">
          <s:hidden id="trapTarget_id" name="model.id" />
        </div>
        <div class="giane-form-field-box">
          <s:textfield
            id="trapTarget_name"
            name="model.name"
            label="%{getText('trapTarget.name.label')}"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span class="giane-form-error-message" id="trapTarget_form_nameError"></span>
        </div>
        <div class="giane-form-field-box">
          <s:textfield
            id="trapTarget_address"
            name="model.address"
            label="%{getText('trapTarget.address.label')}"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span class="giane-form-error-message" id="trapTarget_form_addressError"></span>
        </div>
        <div class="giane-form-field-box">
          <s:textfield
            id="trapTarget_port"
            name="model.port"
            label="%{getText('trapTarget.port.label')}"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span class="giane-form-error-message" id="trapTarget_form_portError"></span>
        </div>
        <div class="giane-form-field-box">
          <s:textarea
            id="trapTarget_descr"
            name="model.descr"
            label="%{getText('trapTarget.descr.label')}"
            required="false"
            requiredposition="left"
            theme="xhtml"
          />
          <span class="giane-form-error-message" id="trapTarget_form_descrError"></span>
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
                  <s:url var="trapTarget_create_url" action="trap-target-create" />
                  <sj:submit
                    listenTopics="doCreate_trapTarget"
                    href="%{trapTarget_create_url}"
                    targets="trash_box"
                    replaceTarget="false"
                    indicator="trapTarget_create_indicator"
                    validate="true"
                    validateFunction="validation"
                    onBeforeTopics="removeErrors"
                    onSuccessTopics="removeErrors,trapTargetTableUpdated"
                    onErrorTopics="createError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                </td>
                <td class="two-buttons-left-indicator-cell">
                  <img
                    id="trapTarget_create_indicator"
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
                  <s:url var="trapTarget_update_url" action="trap-target-update" />
                  <sj:submit
                    listenTopics="doUpdate_trapTarget"
                    href="%{trapTarget_update_url}"
                    targets="trash_box"
                    replaceTarget="false"
                    indicator="trapTarget_update_indicator"
                    validate="true"
                    validateFunction="validation"
                    onBeforeTopics="removeErrors"
                    onSuccessTopics="removeErrors,trapTargetTableUpdated"
                    onErrorTopics="updateError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                  <img
                    id="trapTarget_update_indicator"
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
    <jsp:include page="trap-target-grid.jsp" flush="true" />
  </div>
</div>
