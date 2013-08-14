<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="fixedIpV4Route_create_url" action="fixed-ip-v4-route-create">
  <s:param name="node_id" value="%{#parameters.node_id}" />
</s:url>
<s:url var="fixedIpV4Route_update_url" action="fixed-ip-v4-route-update">
  <s:param name="node_id" value="%{#parameters.node_id}" />
</s:url>

<div class="left-column">
  <div>
    <s:form id="fixedIpV4Route_create_form" theme="simple">
      <fieldset>
        <legend><s:text name="new.fixedIpV4Route" /></legend>
        <div>
          <s:textfield name="model.networkDestination" label="%{getText('fixedIpV4Route.networkDestination.label')}" required="true" requiredposition="left" theme="xhtml"/>
          <span id="fixedIpV4Route_create_form_networkDestinationError"></span>
        </div>
        <div>
          <s:textfield name="model.netmask" label="%{getText('fixedIpV4Route.netmask.label')}" required="true" requiredposition="left" theme="xhtml"/>
          <span id="fixedIpV4Route_create_form_netmaskError"></span>
        </div>
        <div>
          <s:textfield name="model.gateway" label="%{getText('fixedIpV4Route.gateway.label')}" required="true" requiredposition="left" theme="xhtml"/>
          <span id="fixedIpV4Route_create_form_gatewayError"></span>
        </div>
        <div>
          <s:textfield name="model.metric" label="%{getText('fixedIpV4Route.metric.label')}" required="true" requiredposition="left" theme="xhtml"/>
          <span id="fixedIpV4Route_create_form_metricError"></span>
        </div>

        <div>
          <sj:submit
            href="%{fixedIpV4Route_create_url}"
            targets="trash_box"
            replaceTarget="false"
            button="true"
            indicator="fixedIpV4Route_create_indicator"
            validate="true"
            validateFunction="validation"
            onBeforeTopics="removeErrors"
            onSuccessTopics="removeErrors,fixedIpV4RouteTableUpdated"
            onErrorTopics="createError"
            clearForm="true"
            value="Create"
          />
          <img id="fixedIpV4Route_create_indicator" src="images/loading_small.gif" alt="Loading..." style="display:none" />
        </div>
      </fieldset>
    </s:form>
  </div>

  <div>
    <s:form id="fixedIpV4Route_update_form" theme="simple">
      <fieldset>
        <legend><s:text name="selected.fixedIpV4Route" /></legend>
        <div>
          <label for="fixedIpV4Route_grid_selected_id"><s:text name="fixedIpV4Route.id.label" />:</label>
          <span id="fixedIpV4Route_grid_selected_id_span" ></span>
          <s:hidden id="fixedIpV4Route_grid_selected_id" name="model.id" />
        </div>
        <div>
          <s:textfield id="fixedIpV4Route_grid_selected_networkDestination" name="model.networkDestination" label="%{getText('fixedIpV4Route.networkDestination.label')}" required="true" requiredposition="left" theme="xhtml"/>
          <span id="fixedIpV4Route_update_form_networkDestinationError"></span>
        </div>
        <div>
          <s:textfield id="fixedIpV4Route_grid_selected_netmask" name="model.netmask" label="%{getText('fixedIpV4Route.netmask.label')}" required="true" requiredposition="left" theme="xhtml"/>
          <span id="fixedIpV4Route_update_form_netmaskError"></span>
        </div>
        <div>
          <s:textfield id="fixedIpV4Route_grid_selected_gateway" name="model.gateway" label="%{getText('fixedIpV4Route.gateway.label')}" required="true" requiredposition="left" theme="xhtml"/>
          <span id="fixedIpV4Route_update_form_gatewayError"></span>
        </div>
        <div>
          <s:textfield id="fixedIpV4Route_grid_selected_metric" name="model.metric" label="%{getText('fixedIpV4Route.metric.label')}" required="true" requiredposition="left" theme="xhtml"/>
          <span id="fixedIpV4Route_update_form_metricError"></span>
        </div>

        <div>
          <sj:submit
            href="%{fixedIpV4Route_update_url}"
            targets="trash_box"
            replaceTarget="false"
            button="true"
            indicator="fixedIpV4Route_update_indicator"
            validate="true"
            validateFunction="validation"
            onBeforeTopics="removeErrors"
            onSuccessTopics="removeErrors,fixedIpV4RouteTableUpdated"
            onErrorTopics="updateError"
            clearForm="true"
            value="Update"
          />
          <img id="fixedIpV4Route_update_indicator" src="images/loading_small.gif" alt="Loading..." style="display:none" />
        </div>
      </fieldset>
    </s:form>
  </div>

</div>

<div class="right-column">
  <div class="grid-box">
    <jsp:include page="fixed-ip-v4-route-grid.jsp" />
  </div>
</div>
