<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="left-column">
  <div>
    <s:form id="additionalIpV4Route_form" theme="simple" cssClass="giane-form">
      <fieldset>
        <legend><s:text name="additionalIpV4Route.form" /></legend>
        <div>
          <s:hidden id="additionalIpV4Route_id" name="model.id" />
        </div>
        <div>
          <s:textfield
            id="additionalIpV4Route_name"
            name="model.name"
            label="%{getText('additionalIpV4Route.name.label')}"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span id="additionalIpV4Route_form_nameError"></span>
        </div>
        <div>
          <s:textfield
            id="additionalIpV4Route_networkDestination"
            name="model.networkDestination"
            label="%{getText('additionalIpV4Route.networkDestination.label')}"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span id="additionalIpV4Route_form_networkDestinationError"></span>
        </div>
        <div>
          <s:textfield
            id="additionalIpV4Route_netmask"
            name="model.netmask"
            label="%{getText('additionalIpV4Route.netmask.label')}"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span id="additionalIpV4Route_form_netmaskError"></span>
        </div>
        <div>
          <s:textfield
            id="additionalIpV4Route_gateway"
            name="model.gateway"
            label="%{getText('additionalIpV4Route.gateway.label')}"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span id="additionalIpV4Route_form_gatewayError"></span>
        </div>
        <div>
          <s:textfield
            id="additionalIpV4Route_metric"
            name="model.metric"
            label="%{getText('additionalIpV4Route.metric.label')}"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span id="additionalIpV4Route_form_metricError"></span>
        </div>
        <div>
          <s:textarea
            id="additionalIpV4Route_descr"
            name="model.descr"
            label="%{getText('additionalIpV4Route.descr.label')}"
            cols="30"
            required="false"
            requiredposition="left"
            resizable="false"
            theme="xhtml"
          />
          <span id="additionalIpV4Route_form_descrError"></span>
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
                    onSuccessTopics="removeErrors,additionalIpV4RouteTableUpdated"
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
                <td class="two-buttons-right-cell">
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
                    onSuccessTopics="removeErrors,additionalIpV4RouteTableUpdated"
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

<div class="right-column">
  <div class="grid-box">
    <jsp:include page="additional-ip-v4-route-grid.jsp" flush="true" />
  </div>
</div>
