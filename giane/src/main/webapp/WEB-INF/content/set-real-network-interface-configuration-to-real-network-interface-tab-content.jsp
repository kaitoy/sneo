<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<div class="left-column">
  <div>
    <s:form
      id="set_realNetworkInterfaceConfiguration_to_realNetworkInterface_form"
      theme="simple"
      cssClass="giane-form"
    >
      <fieldset>
        <legend><s:text name="set_realNetworkInterfaceConfiguration_to_realNetworkInterface.form" /></legend>
        <div>
          <s:hidden id="realNetworkInterface_with_realNetworkInterfaceConfiguration_id" name="realNetworkInterface" />
        </div>
        <div>
          <s:textfield
            id="realNetworkInterface_with_realNetworkInterfaceConfiguration_name"
            name="name"
            label="%{getText('realNetworkInterface.name.label')}"
            theme="xhtml"
            disabled="true"
            cssClass="giane-disabled-field"
          />
        </div>
        <div>
          <s:select
            id="realNetworkInterface_with_realNetworkInterfaceConfiguration_realNetworkInterfaceConfiguration"
            name="realNetworkInterfaceConfiguration"
            label="%{getText('realNetworkInterface.realNetworkInterfaceConfiguration.label')}"
            list="%{realNetworkInterfaceConfigurations}"
            emptyOption="true"
            multiple="false"
            required="false"
            requiredposition="left"
            theme="xhtml"
            cssClass="select-field"
          />
          <label for="realNetworkInterface_with_realNetworkInterfaceConfiguration_realNetworkInterfaceConfiguration"><span id="set_realNetworkInterfaceConfiguration_to_realNetworkInterface_form_realNetworkInterfaceConfigurationError"></span></label>
        </div>
        <div>
          <table class="submit-button-table">
            <tbody>
              <tr>
                <td class="set-button-cell">
                  <sj:submit
                    value="%{getText('form.saveButton.label')}"
                    button="true"
                    cssClass="giane-form-button"
                    onClickTopics="saveButtonClicked"
                  />
                  <s:url var="set_realNetworkInterfaceConfiguration_to_realNetworkInterface_url" action="set-real-network-interface-configuration-to-real-network-interface">
                    <s:param name="simulation_id" value="%{#parameters.simulation_id}" />
                  </s:url>
                  <sj:submit
                    listenTopics="doSave_set_realNetworkInterfaceConfiguration_to_realNetworkInterface"
                    href="%{set_realNetworkInterfaceConfiguration_to_realNetworkInterface_url}"
                    targets="trash_box"
                    replaceTarget="false"
                    indicator="set_realNetworkInterfaceConfiguration_to_realNetworkInterface_indicator"
                    validate="true"
                    validateFunction="validation"
                    onBeforeTopics="removeErrors"
                    onSuccessTopics="removeErrors,set_realNetworkInterfaceConfiguration_to_realNetworkInterface_success"
                    onErrorTopics="setError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                </td>
                <td class="set-button-indicator-cell">
                  <img
                    id="set_realNetworkInterfaceConfiguration_to_realNetworkInterface_indicator"
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
    <jsp:include page="real-network-interface-with-real-network-interface-configuration-grid.jsp" />
  </div>
</div>

