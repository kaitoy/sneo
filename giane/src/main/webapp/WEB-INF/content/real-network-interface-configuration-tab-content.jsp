<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="left-column">
  <div>
    <s:form id="realNetworkInterfaceConfiguration_create_form" action="real-network-interface-configuration-create" theme="simple">
      <fieldset>
        <legend><s:text name="new.realNetworkInterfaceConfiguration" /></legend>
        <div>
          <s:textfield name="model.name" label="%{getText('realNetworkInterfaceConfiguration.name.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="realNetworkInterfaceConfiguration_create_form_nameError"></span>
        </div>
        <div>
          <s:textfield name="model.macAddress" label="%{getText('realNetworkInterfaceConfiguration.macAddress.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="realNetworkInterfaceConfiguration_create_form_macAddressError"></span>
        </div>
        <div>
          <s:select
            name="model.deviceName"
            label="%{getText('realNetworkInterfaceConfiguration.deviceName.label')}"
            list="%{devices}"
            multiple="false"
            required="true"
            requiredposition="left"
            theme="xhtml"
            cssClass="select-field"
          />
          <span id="realNetworkInterfaceConfiguration_create_form_deviceNameError"></span>
        </div>
        <div>
          <s:textarea name="model.descr" label="%{getText('realNetworkInterfaceConfiguration.descr.label')}" cols="30" rows="2" required="false" requiredposition="left" resizable="false" theme="xhtml" />
          <span id="realNetworkInterfaceConfiguration_create_form_descrError"></span>
        </div>
        <div>
          <sj:submit
            targets="trash_box"
            replaceTarget="false"
            button="true"
            indicator="realNetworkInterfaceConfiguration_create_indicator"
            validate="true"
            validateFunction="validation"
            onBeforeTopics="removeErrors"
            onSuccessTopics="removeErrors,realNetworkInterfaceConfigurationTableUpdated"
            onErrorTopics="createError"
            clearForm="true"
            value="Create"
          />
          <img id="realNetworkInterfaceConfiguration_create_indicator" src="images/loading_small.gif" alt="Loading..." style="display:none" />
        </div>
      </fieldset>
    </s:form>
  </div>

  <div>
    <s:form id="realNetworkInterfaceConfiguration_update_form" action="real-network-interface-configuration-update" theme="simple">
      <fieldset>
        <legend><s:text name="selected.realNetworkInterfaceConfiguration" /></legend>
        <div>
          <label for="realNetworkInterfaceConfiguration_grid_selected_id"><s:text name="realNetworkInterfaceConfiguration.id.label" />:</label>
          <s:hidden id="realNetworkInterfaceConfiguration_grid_selected_id" name="model.id" />
          <span id="realNetworkInterfaceConfiguration_grid_selected_id_span" ></span>
        </div>
        <div>
          <s:textfield id="realNetworkInterfaceConfiguration_grid_selected_name" name="model.name" label="%{getText('realNetworkInterfaceConfiguration.name.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="realNetworkInterfaceConfiguration_update_form_nameError"></span>
        </div>
        <div>
          <s:textfield id="realNetworkInterfaceConfiguration_grid_selected_macAddress" name="model.macAddress" label="%{getText('realNetworkInterfaceConfiguration.macAddress.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="realNetworkInterfaceConfiguration_update_form_macAddressError"></span>
        </div>
        <div>
          <s:select
            id="realNetworkInterfaceConfiguration_grid_selected_deviceName"
            name="model.deviceName"
            label="%{getText('realNetworkInterfaceConfiguration.deviceName.label')}"
            list="%{devices}"
            emptyOption="true"
            multiple="false"
            required="true"
            requiredposition="left"
            theme="xhtml"
            cssClass="select-field"
          />
          <span id="realNetworkInterfaceConfiguration_update_form_deviceNameError"></span>
        </div>
        <div>
          <s:textarea id="realNetworkInterfaceConfiguration_grid_selected_descr" name="model.descr" label="%{getText('realNetworkInterfaceConfiguration.descr.label')}" cols="30" rows="2" required="false" requiredposition="left" resizable="false" theme="xhtml" />
          <span id="realNetworkInterfaceConfiguration_update_form_descrError"></span>
        </div>
        <div>
          <sj:submit
            targets="trash_box"
            replaceTarget="false"
            button="true"
            indicator="realNetworkInterfaceConfiguration_update_indicator"
            validate="true"
            validateFunction="validation"
            onBeforeTopics="removeErrors"
            onSuccessTopics="removeErrors,realNetworkInterfaceConfigurationTableUpdated"
            onErrorTopics="updateError"
            clearForm="true"
            value="Update"
          />
          <img id="realNetworkInterfaceConfiguration_update_indicator" src="images/loading_small.gif" alt="Loading..." style="display:none" />
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
    dialogClass="dialog"
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

<div class="right-column">
  <div class="grid-box">
    <jsp:include page="real-network-interface-configuration-grid.jsp" />
  </div>
</div>

<s:url var="realNetworkInterfaceConfiguration_url" action="real-network-interface-configuration" />
<sj:submit
  href="%{realNetworkInterfaceConfiguration_url}"
  formIds="realNetworkInterfaceConfiguration_update_form"
  targets="main"
  replaceTarget="false"
  indicator="main_indicator"
  listenTopics="realNetworkInterfaceConfiguration_rowDblClicked"
  style="display: none"
/>
