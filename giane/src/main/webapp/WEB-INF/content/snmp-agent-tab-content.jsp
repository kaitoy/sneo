<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="giane-tab-content-form-column">
  <div>
    <s:form id="snmpAgent_form" theme="simple" cssClass="giane-form">
      <fieldset>
        <legend><s:text name="snmpAgent.form" /></legend>
        <div class="giane-form-field-box">
          <s:hidden id="snmpAgent_id" name="model.id" />
        </div>
        <div class="giane-form-field-box">
          <s:textfield
            id="snmpAgent_address"
            name="model.address"
            label="%{getText('snmpAgent.address.label')}"
            requiredLabel="true"
            requiredPosition="left"
            theme="xhtml"
            title="Used as the trap source address"
          />
          <span class="giane-form-error-message" id="snmpAgent_form_addressError"></span>
        </div>
        <div class="giane-form-field-box">
          <s:textfield
          id="snmpAgent_port"
          name="model.port"
          label="%{getText('snmpAgent.port.label')}"
          requiredLabel="true"
          requiredPosition="left"
          theme="xhtml"
        />
          <span class="giane-form-error-message" id="snmpAgent_form_portError"></span>
        </div>
        <div class="giane-form-field-box">
          <s:textfield
            id="snmpAgent_communityName"
            name="model.communityName"
            label="%{getText('snmpAgent.communityName.label')}"
            requiredLabel="true"
            requiredPosition="left"
            theme="xhtml"
          />
          <span class="giane-form-error-message" id="snmpAgent_form_communityNameError"></span>
        </div>
        <div class="giane-form-field-box">
          <s:textfield
            id="snmpAgent_securityName"
            name="model.securityName"
            label="%{getText('snmpAgent.securityName.label')}"
            requiredLabel="true"
            requiredPosition="left"
            theme="xhtml"
          />
          <span class="giane-form-error-message" id="snmpAgent_form_securityNameError"></span>
        </div>
        <div class="giane-form-field-box">
          <s:textfield
            id="snmpAgent_fileMibPath"
            name="model.fileMibPath"
            label="%{getText('snmpAgent.fileMibPath.label')}"
            requiredLabel="true"
            requiredPosition="left"
            theme="xhtml"
          />
          <span class="giane-form-error-message" id="snmpAgent_form_fileMibPathError"></span>
        </div>
        <div class="giane-form-field-box">
          <s:select
            id="snmpAgent_fileMibFormat"
            name="model.fileMibFormat"
            label="%{getText('snmpAgent.fileMibFormat.label')}"
            list="%{formats}"
            multiple="false"
            requiredLabel="true"
            requiredPosition="left"
            theme="xhtml"
          />
          <span class="giane-form-error-message" id="snmpAgent_form_fileMibFormatError"></span>
        </div>
        <div class="giane-form-field-box">
          <s:textfield
            id="snmpAgent_communityStringIndexList"
            name="model.communityStringIndexList"
            label="%{getText('snmpAgent.communityStringIndexList.label')}"
            requiredLabel="false"
            requiredPosition="left"
            theme="xhtml"
            title="Comma separated values"
          />
          <span class="giane-form-error-message" id="snmpAgent_form_communityStringIndexListError"></span>
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
                  <s:url var="snmpAgent_create_url" action="snmp-agent-create">
                    <s:param name="node_id" value="%{#parameters.node_id}" />
                  </s:url>
                  <sj:submit
                    listenTopics="doCreate_snmpAgent"
                    href="%{snmpAgent_create_url}"
                    targets="trash_box"
                    replaceTarget="false"
                    indicator="snmpAgent_create_indicator"
                    validate="true"
                    validateFunction="validation"
                    onBeforeTopics="removeErrors"
                    onSuccessTopics="snmpAgentTableUpdated"
                    onErrorTopics="createError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                </td>
                <td class="two-buttons-left-indicator-cell">
                  <img
                    id="snmpAgent_create_indicator"
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
                  <s:url var="snmpAgent_update_url" action="snmp-agent-update" />
                  <sj:submit
                    listenTopics="doUpdate_snmpAgent"
                    href="%{snmpAgent_update_url}"
                    targets="trash_box"
                    replaceTarget="false"
                    indicator="snmpAgent_update_indicator"
                    validate="true"
                    validateFunction="validation"
                    onBeforeTopics="removeErrors"
                    onSuccessTopics="snmpAgentTableUpdated"
                    onErrorTopics="updateError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                  <img
                    id="snmpAgent_update_indicator"
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
    <jsp:include page="snmp-agent-grid.jsp" flush="true" />
  </div>
</div>

