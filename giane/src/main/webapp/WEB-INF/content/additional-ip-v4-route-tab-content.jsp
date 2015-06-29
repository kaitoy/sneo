<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="giane-tab-content-form-column">
  <div>
    <s:form id="additionalIpV4Route_form" theme="simple" cssClass="giane-form">
      <fieldset>
        <legend><s:text name="ipV4Route.additionalIpV4Route.form" /></legend>
        <div class="giane-form-field-box">
          <s:hidden id="additionalIpV4Route_id" name="model.id" />
        </div>
        <div class="giane-form-field-box">
          <s:textfield
            id="additionalIpV4Route_name"
            name="model.name"
            label="%{getText('ipV4Route.name.label')}"
            requiredLabel="true"
            requiredPosition="left"
            theme="xhtml"
          />
          <span class="giane-form-error-message" id="additionalIpV4Route_form_nameError"></span>
        </div>
        <div class="giane-form-field-box">
          <s:textfield
            id="additionalIpV4Route_networkDestination"
            name="model.networkDestination"
            label="%{getText('ipV4Route.networkDestination.label')}"
            requiredLabel="true"
            requiredPosition="left"
            theme="xhtml"
          />
          <span class="giane-form-error-message" id="additionalIpV4Route_form_networkDestinationError"></span>
        </div>
        <div class="giane-form-field-box">
          <s:textfield
            id="additionalIpV4Route_netmask"
            name="model.netmask"
            label="%{getText('ipV4Route.netmask.label')}"
            requiredLabel="true"
            requiredPosition="left"
            theme="xhtml"
          />
          <span class="giane-form-error-message" id="additionalIpV4Route_form_netmaskError"></span>
        </div>
        <div class="giane-form-field-box">
          <s:textfield
            id="additionalIpV4Route_gateway"
            name="model.gateway"
            label="%{getText('ipV4Route.gateway.label')}"
            requiredLabel="true"
            requiredPosition="left"
            theme="xhtml"
          />
          <span class="giane-form-error-message" id="additionalIpV4Route_form_gatewayError"></span>
        </div>
        <div class="giane-form-field-box">
          <s:textfield
            id="additionalIpV4Route_metric"
            name="model.metric"
            label="%{getText('ipV4Route.metric.label')}"
            requiredLabel="true"
            requiredPosition="left"
            theme="xhtml"
          />
          <span class="giane-form-error-message" id="additionalIpV4Route_form_metricError"></span>
        </div>
        <div class="giane-form-field-box">
          <s:textarea
            id="additionalIpV4Route_descr"
            name="model.descr"
            label="%{getText('ipV4Route.descr.label')}"
            requiredLabel="false"
            requiredPosition="left"
            theme="xhtml"
          />
          <span class="giane-form-error-message" id="additionalIpV4Route_form_descrError"></span>
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
                    var="additionalIpV4Route_create_url"
                    action="additional-ip-v4-route-create"
                  />
                  <sj:submit
                    listenTopics="doCreate_additionalIpV4Route"
                    href="%{additionalIpV4Route_create_url}"
                    targets="trash_box"
                    replaceTarget="false"
                    indicator="additionalIpV4Route_create_indicator"
                    validate="true"
                    validateFunction="validation"
                    onBeforeTopics="removeErrors"
                    onSuccessTopics="additionalIpV4RouteTableUpdated"
                    onErrorTopics="createError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                </td>
                <td class="two-buttons-left-indicator-cell">
                  <img
                    id="additionalIpV4Route_create_indicator"
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
                    var="additionalIpV4Route_update_url"
                    action="additional-ip-v4-route-update"
                  />
                  <sj:submit
                    listenTopics="doUpdate_additionalIpV4Route"
                    href="%{additionalIpV4Route_update_url}"
                    targets="trash_box"
                    replaceTarget="false"
                    indicator="additionalIpV4Route_update_indicator"
                    validate="true"
                    validateFunction="validation"
                    onBeforeTopics="removeErrors"
                    onSuccessTopics="additionalIpV4RouteTableUpdated"
                    onErrorTopics="updateError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                  <img
                    id="additionalIpV4Route_update_indicator"
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

<s:url var="additionalIpV4Route_grid_box_url" action="additional-ip-v4-route-grid-box" escapeAmp="false">
  <s:param name="modelNameCamel" value="'additionalIpV4Route'" />
  <s:param name="modelNameHyphen" value="'additional-ip-v4-route'" />
  <s:param name="gridCaption" value="%{getText('ipV4Route.additionalIpV4Route.grid.caption')}" />
  <s:param name="usesNameColmn" value="true" />
  <s:param name="usesDescrColmn" value="true" />
</s:url>
<div class="giane-tab-content-grid-column">
  <sj:div href="%{additionalIpV4Route_grid_box_url}" indicator="additionalIpV4Route_grid_box_indicator" cssClass="giane-grid-box" />
  <img id="additionalIpV4Route_grid_box_indicator" src="images/loading_small.gif" alt="Loading..." style="display: none;" />
</div>
