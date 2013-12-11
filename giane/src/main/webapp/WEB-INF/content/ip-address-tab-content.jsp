<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="left-column">
  <div>
    <s:form id="ipAddress_form" theme="simple" cssClass="giane-form">
      <fieldset>
        <legend><s:text name="ipAddress.form" /></legend>
        <div>
          <s:hidden id="ipAddress_id" name="model.id" />
        </div>
        <div>
          <s:textfield
            id="ipAddress_address"
            name="model.address"
            label="%{getText('ipAddress.address.label')}"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span id="ipAddress_form_addressError"></span>
        </div>
        <div>
          <s:textfield
            id="ipAddress_prefixLength"
            name="model.prefixLength"
            label="%{getText('ipAddress.prefixLength.label')}"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span id="ipAddress_form_prefixLengthError"></span>
        </div>
        <div>
          <table class="submit-button-table">
            <tbody>
              <tr>
                <td class="two-buttons-left-cell">
                  <sj:submit
                    value="%{getText('form.createButton.label')}"
                    button="true"
                    cssClass="giane-form-button"
                    onClickTopics="createButtonClicked"
                  />
                  <s:url var="ipAddress_create_url" action="ip-address-create">
                    <s:param name="ipAddressRelation_id" value="%{#parameters.ipAddressRelation_id}" />
                  </s:url>
                  <sj:submit
                    listenTopics="doCreate_ipAddress"
                    href="%{ipAddress_create_url}"
                    targets="trash_box"
                    replaceTarget="false"
                    indicator="ipAddress_create_indicator"
                    validate="true"
                    validateFunction="validation"
                    onBeforeTopics="removeErrors"
                    onSuccessTopics="removeErrors,ipAddressTableUpdated"
                    onErrorTopics="createError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                </td>
                <td class="two-buttons-left-indicator-cell">
                  <img
                    id="ipAddress_create_indicator"
                    src="images/loading_small.gif"
                    alt="Loading..."
                    style="display: none;"
                  />
                </td>
                <td class="two-buttons-right-cell">
                  <sj:submit
                    value="%{getText('form.updateButton.label')}"
                    button="true"
                    cssClass="giane-form-button"
                    onClickTopics="updateButtonClicked"
                  />
                  <s:url var="ipAddress_update_url" action="ip-address-update" />
                <sj:submit
                  listenTopics="doUpdate_ipAddress"
                  href="%{ipAddress_update_url}"
                  targets="trash_box"
                  replaceTarget="false"
                  indicator="ipAddress_update_indicator"
                  validate="true"
                  validateFunction="validation"
                  onBeforeTopics="removeErrors"
                  onSuccessTopics="removeErrors,ipAddressTableUpdated"
                  onErrorTopics="updateError"
                  clearForm="true"
                  cssStyle="display: none;"
                />
                <img
                  id="ipAddress_update_indicator"
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
    <jsp:include page="ip-address-grid.jsp" flush="true" />
  </div>
</div>
