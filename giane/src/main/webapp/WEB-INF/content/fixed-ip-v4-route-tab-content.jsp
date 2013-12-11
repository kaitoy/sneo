<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="left-column">
  <div>
    <s:form id="fixedIpV4Route_form" theme="simple" cssClass="giane-form">
      <fieldset>
        <legend><s:text name="fixedIpV4Route.form" /></legend>
        <div>
          <s:hidden id="fixedIpV4Route_id" name="model.id" />
        </div>
        <div>
          <s:textfield
            id="fixedIpV4Route_networkDestination"
            name="model.networkDestination"
            label="%{getText('fixedIpV4Route.networkDestination.label')}"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span id="fixedIpV4Route_form_networkDestinationError"></span>
        </div>
        <div>
          <s:textfield
            id="fixedIpV4Route_netmask"
            name="model.netmask"
            label="%{getText('fixedIpV4Route.netmask.label')}"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span id="fixedIpV4Route_form_netmaskError"></span>
        </div>
        <div>
          <s:textfield
            id="fixedIpV4Route_gateway"
            name="model.gateway"
            label="%{getText('fixedIpV4Route.gateway.label')}"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span id="fixedIpV4Route_form_gatewayError"></span>
        </div>
        <div>
          <s:textfield
            id="fixedIpV4Route_metric"
            name="model.metric"
            label="%{getText('fixedIpV4Route.metric.label')}"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span id="fixedIpV4Route_form_metricError"></span>
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
                  <s:url var="fixedIpV4Route_create_url" action="fixed-ip-v4-route-create">
                    <s:param name="node_id" value="%{#parameters.node_id}" />
                  </s:url>
                  <sj:submit
                    listenTopics="doCreate_fixedIpV4Route"
                    href="%{fixedIpV4Route_create_url}"
                    targets="trash_box"
                    replaceTarget="false"
                    indicator="fixedIpV4Route_create_indicator"
                    validate="true"
                    validateFunction="validation"
                    onBeforeTopics="removeErrors"
                    onSuccessTopics="removeErrors,fixedIpV4RouteTableUpdated"
                    onErrorTopics="createError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                  
                </td>
                <td class="two-buttons-left-indicator-cell">
                  <img
                    id="fixedIpV4Route_create_indicator"
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
                  <s:url var="fixedIpV4Route_update_url" action="fixed-ip-v4-route-update">
                    <s:param name="node_id" value="%{#parameters.node_id}" />
                  </s:url>
                  <sj:submit
                    listenTopics="doUpdate_fixedIpV4Route"
                    href="%{fixedIpV4Route_update_url}"
                    targets="trash_box"
                    replaceTarget="false"
                    indicator="fixedIpV4Route_update_indicator"
                    validate="true"
                    validateFunction="validation"
                    onBeforeTopics="removeErrors"
                    onSuccessTopics="removeErrors,fixedIpV4RouteTableUpdated"
                    onErrorTopics="updateError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                  <img
                    id="fixedIpV4Route_update_indicator"
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
    <jsp:include page="fixed-ip-v4-route-grid.jsp" flush="true" />
  </div>
</div>
