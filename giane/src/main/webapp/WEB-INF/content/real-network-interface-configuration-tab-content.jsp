<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="giane-tab-content-form-column">
  <div>
    <s:form
      id="realNetworkInterfaceConfiguration_form"
      theme="simple"
      cssClass="giane-form"
    >
      <fieldset>
        <legend><s:text name="realNetworkInterfaceConfiguration.form" /></legend>
        <div class="giane-form-field-box">
          <s:hidden id="realNetworkInterfaceConfiguration_id" name="model.id" />
        </div>
        <div class="giane-form-field-box">
          <s:textfield
            id="realNetworkInterfaceConfiguration_name"
            name="model.name"
            label="%{getText('realNetworkInterfaceConfiguration.name.label')}"
            requiredLabel="true"
            requiredPosition="left"
            theme="xhtml"
          />
          <span class="giane-form-error-message" id="realNetworkInterfaceConfiguration_form_nameError"></span>
        </div>
        <div class="giane-form-field-box">
          <s:textfield
            id="realNetworkInterfaceConfiguration_macAddress"
            name="model.macAddress"
            label="%{getText('realNetworkInterfaceConfiguration.macAddress.label')}"
            requiredLabel="true"
            requiredPosition="left"
            theme="xhtml"
          />
          <span class="giane-form-error-message" id="realNetworkInterfaceConfiguration_form_macAddressError"></span>
        </div>
        <div class="giane-form-field-box">
          <s:select
            id="realNetworkInterfaceConfiguration_deviceName"
            name="model.deviceName"
            label="%{getText('realNetworkInterfaceConfiguration.deviceName.label')}"
            list="%{devices}"
            multiple="false"
            requiredLabel="true"
            requiredPosition="left"
            theme="xhtml"
            cssClass="select-field"
          />
          <span class="giane-form-error-message" id="realNetworkInterfaceConfiguration_form_deviceNameError"></span>
        </div>
        <div class="giane-form-field-box">
          <s:textarea
            id="realNetworkInterfaceConfiguration_descr"
            name="model.descr"
            label="%{getText('realNetworkInterfaceConfiguration.descr.label')}"
            requiredLabel="false"
            requiredPosition="left"
            theme="xhtml"
          />
          <span class="giane-form-error-message" id="realNetworkInterfaceConfiguration_form_descrError"></span>
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
                  <s:url
                    var="realNetworkInterfaceConfiguration_create_url"
                    action="real-network-interface-configuration-create"
                  />
                  <sj:submit
                    listenTopics="doCreate_realNetworkInterfaceConfiguration"
                    href="%{realNetworkInterfaceConfiguration_create_url}"
                    targets="trash_box"
                    replaceTarget="false"
                    indicator="realNetworkInterfaceConfiguration_create_indicator"
                    validate="true"
                    validateFunction="validation"
                    onBeforeTopics="removeErrors"
                    onSuccessTopics="realNetworkInterfaceConfigurationTableUpdated"
                    onErrorTopics="createError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                </td>
                <td class="two-buttons-left-indicator-cell">
                  <img
                    id="realNetworkInterfaceConfiguration_create_indicator"
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
                  <s:url
                    var="realNetworkInterfaceConfiguration_update_url"
                    action="real-network-interface-configuration-update"
                  />
                  <sj:submit
                    listenTopics="doUpdate_realNetworkInterfaceConfiguration"
                    href="%{realNetworkInterfaceConfiguration_update_url}"
                    targets="trash_box"
                    replaceTarget="false"
                    indicator="realNetworkInterfaceConfiguration_update_indicator"
                    validate="true"
                    validateFunction="validation"
                    onBeforeTopics="removeErrors"
                    onSuccessTopics="realNetworkInterfaceConfigurationTableUpdated"
                    onErrorTopics="updateError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                  <img
                    id="realNetworkInterfaceConfiguration_update_indicator"
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

  <s:url id="showDeviceList_url" action="show-device-list"/>
  <sj:dialog
    id="deviceList_dialog"
    showEffect="scale"
    hideEffect="scale"
    autoOpen="false"
    modal="false"
    title="%{getText('showDeviceList.dialog.title')}"
    dialogClass="giane-dialog"
    draggable="true"
    height="300"
    width="600"
  />
  <sj:a
    openDialog="deviceList_dialog"
    href="%{showDeviceList_url}"
    button="true"
    buttonIcon="ui-icon-gear"
  >
    <s:text name="showDeviceList.button.text" />
  </sj:a>
</div>

<div class="giane-tab-content-grid-column">
  <div class="giane-grid-box">
    <jsp:include page="real-network-interface-configuration-grid.jsp" flush="true" />
  </div>
</div>

<s:url var="realNetworkInterfaceConfiguration_url" action="real-network-interface-configuration" escapeAmp="false">
  <s:param name="tabIndex" value="%{#parameters.tabIndex}" />
  <s:param name="breadcrumbsId" value="%{#parameters.breadcrumbsId}" />
</s:url>
<sj:submit
  href="%{realNetworkInterfaceConfiguration_url}"
  formIds="realNetworkInterfaceConfiguration_form"
  targets="config_main"
  replaceTarget="false"
  indicator="config_main_indicator"
  validate="true"
  validateFunction="checkRowSelection"
  listenTopics="realNetworkInterfaceConfiguration_rowDblClicked"
  onBeforeTopics="mainPaneGoingForward_before"
  onAfterValidationTopics="mainPaneGoingForward_after"
  onCompleteTopics="mainPaneCompleted"
  cssStyle="display: none;"
/>
