<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="set_trapTargetGroup_to_snmpAgent_url" action="set-trap-target-group-to-snmp-agent">
  <s:param name="simulation_id" value="%{#parameters.simulation_id}" />
</s:url>

<div id="set_trapTargetGroup_to_snmpAgent_left_column" class="left-column">
  <div id="set_trapTargetGroup_to_snmpAgent_box">
    <s:form id="set_trapTargetGroup_to_snmpAgent_form" theme="simple">
      <fieldset id="set_trapTargetGroup_to_snmpAgent_fields">
        <legend><s:text name="selected.snmpAgent.with.trapTargetGroup" /></legend>
        <div class="type-hidden">
          <label for="snmpAgent_with_trapTargetGroup_grid_selected_id"><s:text name="snmpAgent.id.label" /></label>
          <span id="snmpAgent_with_trapTargetGroup_grid_selected_id_span" ></span>
          <s:hidden id="snmpAgent_with_trapTargetGroup_grid_selected_id" name="snmpAgent" />
        </div>
        <div class="type-text">
          <label for="snmpAgent_with_trapTargetGroup_grid_selected_address"><s:text name="snmpAgent.address.label" /></label>
          <span id="snmpAgent_with_trapTargetGroup_grid_selected_address_span" ></span>
        </div>
        <div class="type-text">
          <label for="snmpAgent_with_trapTargetGroup_grid_selected_hostNode"><s:text name="snmpAgent.hostNode.label" /></label>
          <span id="snmpAgent_with_trapTargetGroup_grid_selected_hostNode_span" ></span>
        </div>
        <div class="type-text">
          <label for="snmpAgent_with_trapTargetGroup_grid_selected_trapTargetGroup"><s:text name="snmpAgent.trapTargetGroup.label" /></label>
          <s:select
            id="snmpAgent_with_trapTargetGroup_grid_selected_trapTargetGroup"
            name="trapTargetGroup"
            list="%{trapTargetGroups}"
            emptyOption="true"
            multiple="false"
            required="true"
            theme="simple"
            cssClass="select-field"
          />
          <label for="snmpAgent_with_trapTargetGroup_grid_selected_trapTargetGroup"><span id="set_trapTargetGroup_to_snmpAgent_form_trapTargetGroupError"></span></label>
        </div>

        <div class="type-button">
          <sj:submit
            id="submit_set_trapTargetGroup_to_snmpAgent"
            href="%{set_trapTargetGroup_to_snmpAgent_url}"
            targets="snmpAgent_with_trapTargetGroup_grid_box"
            replaceTarget="false"
            button="true"
            indicator="set_trapTargetGroup_to_snmpAgent_indicator"
            validate="true"
            validateFunction="validation"
            onBeforeTopics="removeErrors"
            onSuccessTopics="removeErrors,set_trapTargetGroup_to_snmpAgent_success"
            onErrorTopics="setError"
            clearForm="true"
            value="Set"
          />
          <img id="set_trapTargetGroup_to_snmpAgent_indicator" src="images/loading_small.gif" alt="Loading..." style="display: none;" />
        </div>
      </fieldset>
    </s:form>
  </div>

</div>

<div id="set_trapTargetGroup_to_snmpAgent_right_column" class="right-column">
  <div id="snmpAgent_with_trapTargetGroup_grid_box" class="grid-box">
    <jsp:include page="snmp-agent-with-trap-target-group-grid.jsp" />
  </div>
</div>

