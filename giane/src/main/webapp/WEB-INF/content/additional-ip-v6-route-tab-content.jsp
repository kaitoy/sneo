<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="giane-tab-content-form-column">
  <div>
    <s:form id="additionalIpV6Route_form" theme="simple" cssClass="giane-form">
      <fieldset>
        <legend><s:text name="ipV6Route.additionalIpV6Route.form" /></legend>
        <div class="giane-form-field-box">
          <s:hidden id="additionalIpV6Route_id" name="model.id" />
        </div>
        <div class="giane-form-field-box">
          <s:textfield
            id="additionalIpV6Route_name"
            name="model.name"
            label="%{getText('ipV6Route.name.label')}"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span class="giane-form-error-message" id="additionalIpV6Route_form_nameError"></span>
        </div>
        <div class="giane-form-field-box">
          <s:textfield
            id="additionalIpV6Route_networkDestination"
            name="model.networkDestination"
            label="%{getText('ipV6Route.networkDestination.label')}"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span class="giane-form-error-message" id="additionalIpV6Route_form_networkDestinationError"></span>
        </div>
        <div class="giane-form-field-box">
          <s:textfield
            id="additionalIpV6Route_prefixLength"
            name="model.prefixLength"
            label="%{getText('ipV6Route.prefixLength.label')}"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span class="giane-form-error-message" id="additionalIpV6Route_form_prefixLengthError"></span>
        </div>
        <div class="giane-form-field-box">
          <s:textfield
            id="additionalIpV6Route_gateway"
            name="model.gateway"
            label="%{getText('ipV6Route.gateway.label')}"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span class="giane-form-error-message" id="additionalIpV6Route_form_gatewayError"></span>
        </div>
        <div class="giane-form-field-box">
          <s:textfield
            id="additionalIpV6Route_metric"
            name="model.metric"
            label="%{getText('ipV6Route.metric.label')}"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span class="giane-form-error-message" id="additionalIpV6Route_form_metricError"></span>
        </div>
        <div class="giane-form-field-box">
          <s:textarea
            id="additionalIpV6Route_descr"
            name="model.descr"
            label="%{getText('ipV6Route.descr.label')}"
            required="false"
            requiredposition="left"
            theme="xhtml"
          />
          <span class="giane-form-error-message" id="additionalIpV6Route_form_descrError"></span>
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
                    var="additionalIpV6Route_create_url"
                    action="additional-ip-v6-route-create"
                  />
                  <sj:submit
                    listenTopics="doCreate_additionalIpV6Route"
                    href="%{additionalIpV6Route_create_url}"
                    targets="trash_box"
                    replaceTarget="false"
                    indicator="additionalIpV6Route_create_indicator"
                    validate="true"
                    validateFunction="validation"
                    onBeforeTopics="removeErrors"
                    onSuccessTopics="removeErrors,additionalIpV6RouteTableUpdated"
                    onErrorTopics="createError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                </td>
                <td class="two-buttons-left-indicator-cell">
                  <img
                    id="additionalIpV6Route_create_indicator"
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
                    var="additionalIpV6Route_update_url"
                    action="additional-ip-v6-route-update"
                  />
                  <sj:submit
                    listenTopics="doUpdate_additionalIpV6Route"
                    href="%{additionalIpV6Route_update_url}"
                    targets="trash_box"
                    replaceTarget="false"
                    indicator="additionalIpV6Route_update_indicator"
                    validate="true"
                    validateFunction="validation"
                    onBeforeTopics="removeErrors"
                    onSuccessTopics="removeErrors,additionalIpV6RouteTableUpdated"
                    onErrorTopics="updateError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                  <img
                    id="additionalIpV6Route_update_indicator"
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

<s:url var="additionalIpV6Route_grid_box_url" action="additional-ip-v6-route-grid-box" escapeAmp="false">
  <s:param name="modelNameCamel" value="'additionalIpV6Route'" />
  <s:param name="modelNameHyphen" value="'additional-ip-v6-route'" />
  <s:param name="gridCaption" value="%{getText('ipV6Route.additionalIpV6Route.grid.caption')}" />
  <s:param name="usesNameColmn" value="true" />
  <s:param name="usesDescrColmn" value="true" />
</s:url>
<div class="giane-tab-content-grid-column">
  <sj:div href="%{additionalIpV6Route_grid_box_url}" indicator="additionalIpV6Route_grid_box_indicator" cssClass="giane-grid-box" />
  <img id="additionalIpV6Route_grid_box_indicator" src="images/loading_small.gif" alt="Loading..." style="display: none;" />
</div>

