<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="left-column">
  <div>
    <s:form id="simulation_form" theme="simple" cssClass="giane-form">
      <fieldset>
        <legend><s:text name="simulation.form" /></legend>
        <div>
          <s:hidden id="simulation_id" name="model.id" />
        </div>
        <div>
          <s:textfield
            id="simulation_name"
            name="model.name"
            label="%{getText('simulation.name.label')}"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span id="simulation_form_nameError"></span>
        </div>
        <div>
          <s:select
            id="simulation_network"
            name="model.network"
            label="%{getText('simulation.network.label')}"
            list="%{networks}"
            multiple="false"
            required="true"
            requiredposition="left"
            theme="xhtml"
            cssClass="select-field"
          />
          <span id="simulation_form_networkError"></span>
        </div>
        <div>
          <s:textarea
            id="simulation_descr"
            name="model.descr"
            label="%{getText('simulation.descr.label')}"
            cols="30"
            required="false" requiredposition="left"
            resizable="false"
            theme="xhtml"
          />
          <span id="simulation_form_descrError"></span>
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
                  <s:url var="simulation_create_url" action="simulation-create" />
                  <sj:submit
                    listenTopics="doCreate_simulation"
                    href="%{simulation_create_url}"
                    targets="trash_box"
                    replaceTarget="false"
                    indicator="simulation_create_indicator"
                    validate="true"
                    validateFunction="validation"
                    onBeforeTopics="removeErrors"
                    onSuccessTopics="removeErrors,simulationTableUpdated"
                    onErrorTopics="createError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                </td>
                <td class="create-button-indicator-cell">
                  <img
                    id="simulation_create_indicator"
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
                  <s:url var="simulation_update_url" action="simulation-update" />
                  <sj:submit
                    listenTopics="doUpdate_simulation"
                    href="%{simulation_update_url}"
                    targets="trash_box"
                    replaceTarget="false"
                    indicator="simulation_update_indicator"
                    validate="true"
                    validateFunction="validation"
                    onBeforeTopics="removeErrors"
                    onSuccessTopics="removeErrors,simulationTableUpdated"
                    onErrorTopics="updateError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                  <img
                    id="simulation_update_indicator"
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
    <jsp:include page="simulation-grid.jsp" />
  </div>
</div>

<s:url var="simulation_url" action="simulation" />
<sj:submit
  href="%{simulation_url}"
  formIds="simulation_form"
  targets="main"
  replaceTarget="false"
  indicator="main_indicator"
  validate="true"
  validateFunction="validation"
  listenTopics="simulation_rowDblClicked,simulation_configButtonClicked"
  onBeforeTopics="mainPaneGoingForward"
  onCompleteTopics="mainPaneCompleted"
  style="display: none;"
/>
