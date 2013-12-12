<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="giane-tab-content-form-column">
  <div>
    <s:form id="realNetworkInterface_form" theme="simple" cssClass="giane-form">
      <fieldset>
        <legend><s:text name="realNetworkInterface.form" /></legend>
        <div>
          <s:hidden id="realNetworkInterface_id" name="model.id" />
        </div>
        <div>
          <s:textfield
            id="realNetworkInterface_name"
            name="model.name"
            label="%{getText('realNetworkInterface.name.label')}"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span id="realNetworkInterface_form_nameError"></span>
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
                  <s:url var="realNetworkInterface_create_url" action="real-network-interface-create">
                    <s:param name="node_id" value="%{#parameters.node_id}" />
                  </s:url>
                  <sj:submit
                    listenTopics="doCreate_realNetworkInterface"
                    href="%{realNetworkInterface_create_url}"
                    targets="trash_box"
                    replaceTarget="false"
                    indicator="realNetworkInterface_create_indicator"
                    validate="true"
                    validateFunction="validation"
                    onBeforeTopics="removeErrors"
                    onSuccessTopics="removeErrors,realNetworkInterfaceTableUpdated"
                    onErrorTopics="createError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                </td>
                <td class="two-buttons-left-indicator-cell">
                  <img id="realNetworkInterface_create_indicator" src="images/loading_small.gif" alt="Loading..." style="display: none;" />
                </td>
                <td class="two-buttons-second-cell">
                  <sj:submit
                    value="%{getText('form.updateButton.label')}"
                    button="true"
                    cssClass="giane-form-button"
                    onClickTopics="updateButtonClicked"
                  />
                  <s:url var="realNetworkInterface_update_url" action="real-network-interface-update">
                    <s:param name="node_id" value="%{#parameters.node_id}" />
                  </s:url>
                    <sj:submit
                      listenTopics="doUpdate_realNetworkInterface"
                      href="%{realNetworkInterface_update_url}"
                      targets="trash_box"
                      replaceTarget="false"
                      indicator="realNetworkInterface_update_indicator"
                      validate="true"
                      validateFunction="validation"
                      onBeforeTopics="removeErrors"
                      onSuccessTopics="removeErrors,realNetworkInterfaceTableUpdated"
                      onErrorTopics="updateError"
                      clearForm="true"
                      cssStyle="display: none;"
                    />
                    <img id="realNetworkInterface_update_indicator" src="images/loading_small.gif" alt="Loading..." style="display: none;" />
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
    <jsp:include page="real-network-interface-grid.jsp" flush="true" />
  </div>
</div>

