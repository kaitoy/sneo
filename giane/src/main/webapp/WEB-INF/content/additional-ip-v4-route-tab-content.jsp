<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="additionalIpV4Route_create_url" action="additional-ip-v4-route-create" />
<s:url var="additionalIpV4Route_update_url" action="additional-ip-v4-route-update" />

<div class="left-column">
  <div>
    <s:form id="additionalIpV4Route_create_form" theme="simple">
      <fieldset>
        <legend><s:text name="new.additionalIpV4Route" /></legend>
        <div>
          <s:textfield name="model.name" label="%{getText('additionalIpV4Route.name.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="additionalIpV4Route_create_form_nameError"></span>
        </div>
        <div>
          <s:textfield name="model.networkDestination" label="%{getText('additionalIpV4Route.networkDestination.label')}" required="true" requiredposition="left" theme="xhtml"/>
          <span id="additionalIpV4Route_create_form_networkDestinationError"></span>
        </div>
        <div>
          <s:textfield name="model.netmask" label="%{getText('additionalIpV4Route.netmask.label')}" required="true" requiredposition="left" theme="xhtml"/>
          <span id="additionalIpV4Route_create_form_netmaskError"></span>
        </div>
        <div>
          <s:textfield name="model.gateway" label="%{getText('additionalIpV4Route.gateway.label')}" required="true" requiredposition="left" theme="xhtml"/>
          <span id="additionalIpV4Route_create_form_gatewayError"></span>
        </div>
        <div>
          <s:textfield name="model.metric" label="%{getText('additionalIpV4Route.metric.label')}" required="true" requiredposition="left" theme="xhtml"/>
          <span id="additionalIpV4Route_create_form_metricError"></span>
        </div>
        <div>
          <s:textarea name="model.descr" label="%{getText('additionalIpV4Route.descr.label')}" cols="30" rows="2" required="false" requiredposition="left" resizable="false" theme="xhtml" />
          <span id="additionalIpV4Route_create_form_descrError"></span>
        </div>
        
        <div>
          <sj:submit
            href="%{additionalIpV4Route_create_url}"
            targets="trash_box"
            replaceTarget="false"
            button="true"
            indicator="additionalIpV4Route_create_indicator"
            validate="true"
            validateFunction="validation"
            onBeforeTopics="removeErrors"
            onSuccessTopics="removeErrors,additionalIpV4RouteTableUpdated"
            onErrorTopics="createError"
            clearForm="true"
            value="Create"
          />
          <img id="additionalIpV4Route_create_indicator" src="images/loading_small.gif" alt="Loading..." style="display: none" />
        </div>
      </fieldset>
    </s:form>
  </div>

  <div>
    <s:form id="additionalIpV4Route_update_form" theme="simple">
      <fieldset>
        <legend><s:text name="selected.additionalIpV4Route" /></legend>
        <div>
          <label for="additionalIpV4Route_grid_selected_id"><s:text name="additionalIpV4Route.id.label" />:</label>
          <span id="additionalIpV4Route_grid_selected_id_span" ></span>
          <s:hidden id="additionalIpV4Route_grid_selected_id" name="model.id" />
        </div>
        <div>
          <s:textfield id="additionalIpV4Route_grid_selected_name" name="model.name" label="%{getText('additionalIpV4Route.name.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="additionalIpV4Route_update_form_nameError"></span>
        </div>
        <div>
          <s:textfield id="additionalIpV4Route_grid_selected_networkDestination" name="model.networkDestination" label="%{getText('additionalIpV4Route.networkDestination.label')}" required="true" requiredposition="left" theme="xhtml"/>
          <span id="additionalIpV4Route_update_form_networkDestinationError"></span>
        </div>
        <div>
          <s:textfield id="additionalIpV4Route_grid_selected_netmask" name="model.netmask" label="%{getText('additionalIpV4Route.netmask.label')}" required="true" requiredposition="left" theme="xhtml"/>
          <span id="additionalIpV4Route_update_form_netmaskError"></span>
        </div>
        <div>
          <s:textfield id="additionalIpV4Route_grid_selected_gateway" name="model.gateway" label="%{getText('additionalIpV4Route.gateway.label')}" required="true" requiredposition="left" theme="xhtml"/>
          <span id="additionalIpV4Route_update_form_gatewayError"></span>
        </div>
        <div>
          <s:textfield id="additionalIpV4Route_grid_selected_metric" name="model.metric" label="%{getText('additionalIpV4Route.metric.label')}" required="true" requiredposition="left" theme="xhtml"/>
          <span id="additionalIpV4Route_update_form_metricError"></span>
        </div>
        <div>
          <s:textarea id="additionalIpV4Route_grid_selected_descr" name="model.descr" label="%{getText('additionalIpV4Route.descr.label')}" cols="30" rows="2" required="false" requiredposition="left" resizable="false" theme="xhtml" />
          <span id="additionalIpV4Route_update_form_descrError"></span>
        </div>
        
        <div>
          <sj:submit
            href="%{additionalIpV4Route_update_url}"
            targets="trash_box"
            replaceTarget="false"
            button="true"
            indicator="additionalIpV4Route_update_indicator"
            validate="true"
            validateFunction="validation"
            onBeforeTopics="removeErrors"
            onSuccessTopics="removeErrors,additionalIpV4RouteTableUpdated"
            onErrorTopics="updateError"
            clearForm="true"
            value="Update"
          />
          <img id="additionalIpV4Route_update_indicator" src="images/loading_small.gif" alt="Loading..." style="display: none" />
        </div>
      </fieldset>
    </s:form>
  </div>

</div>

<div class="right-column">
  <div class="grid-box">
    <jsp:include page="additional-ip-v4-route-grid.jsp" />
  </div>
</div>
