<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="left-column">
  <div>
    <s:form id="physicalNetworkInterface_form" theme="simple" cssClass="giane-form">
      <fieldset>
        <legend><s:text name="physicalNetworkInterface.form" /></legend>
        <div>
          <s:hidden id="physicalNetworkInterface_id" name="model.id" />
        </div>
        <div>
          <s:textfield
            id="physicalNetworkInterface_name"
            name="model.name"
            label="%{getText('physicalNetworkInterface.name.label')}"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span id="physicalNetworkInterface_form_nameError"></span>
        </div>
        <div>
          <s:checkbox
            id="physicalNetworkInterface_trunk"
            name="model.trunk"
            label="%{getText('physicalNetworkInterface.trunk.label')}"
            value="physicalNetworkInterface_trunk"
            required="false"
            requiredposition="left"
            theme="xhtml"
          />
          <span id="physicalNetworkInterface_form_trunkError"></span>
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
                <td class="create-button-indicator-cell">
                  <img
                    id="physicalNetworkInterface_create_indicator"
                    src="images/loading_small.gif"
                    alt="Loading..."
                    style="display: none;"
                  />
                </td>
                <td class="update-button-cell">
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

<div class="right-column">
  <div class="grid-box">
    <jsp:include page="physical-network-interface-grid.jsp" />
  </div>
</div>

<s:url var="physicalNetworkInterface_url" action="physical-network-interface" />
<sj:submit
  href="%{physicalNetworkInterface_url}"
  formIds="physicalNetworkInterface_form"
  targets="main"
  replaceTarget="false"
  indicator="main_indicator"
  validate="true"
  validateFunction="validation"
  listenTopics="physicalNetworkInterface_rowDblClicked,physicalNetworkInterface_configButtonClicked"
  style="display: none;"
/>
