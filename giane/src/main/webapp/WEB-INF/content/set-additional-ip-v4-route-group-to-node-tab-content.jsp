<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="set_additionalIpV4RouteGroup_to_node_url" action="set-additional-ip-v4-route-group-to-node">
  <s:param name="simulation_id" value="%{#parameters.simulation_id}" />
</s:url>

<div id="set_additionalIpV4RouteGroup_to_node_left_column" class="left-column">
  <div id="set_additionalIpV4RouteGroup_to_node_box">
    <s:form id="set_additionalIpV4RouteGroup_to_node_form" theme="simple">
      <fieldset id="set_additionalIpV4RouteGroup_to_node_fields">
        <legend><s:text name="selected.node.with.additionalIpV4RouteGroup" /></legend>
        <div class="type-hidden">
          <label for="node_with_additionalIpV4RouteGroup_grid_selected_id"><s:text name="node.id.label" /></label>
          <span id="node_with_additionalIpV4RouteGroup_grid_selected_id_span" ></span>
          <s:hidden id="node_with_additionalIpV4RouteGroup_grid_selected_id" name="node" />
        </div>
        <div class="type-text">
          <label for="node_with_additionalIpV4RouteGroup_grid_selected_name"><s:text name="node.name.label" /></label>
          <span id="node_with_additionalIpV4RouteGroup_grid_selected_name_span" ></span>
        </div>
        <div class="type-text">
          <label for="node_with_additionalIpV4RouteGroup_grid_selected_additionalIpV4RouteGroup"><s:text name="node.additionalIpV4RouteGroup.label" /></label>
          <s:select
            id="node_with_additionalIpV4RouteGroup_grid_selected_additionalIpV4RouteGroup"
            name="additionalIpV4RouteGroup"
            list="%{additionalIpV4RouteGroups}"
            emptyOption="true"
            multiple="false"
            required="true"
            theme="simple"
            cssClass="select-field"
          />
          <label for="node_with_additionalIpV4RouteGroup_grid_selected_additionalIpV4RouteGroup"><span id="set_additionalIpV4RouteGroup_to_node_form_additionalIpV4RouteGroupError"></span></label>
        </div>

        <div class="type-button">
          <sj:submit
            id="submit_set_additionalIpV4RouteGroup_to_node"
            href="%{set_additionalIpV4RouteGroup_to_node_url}"
            targets="node_with_additionalIpV4RouteGroup_grid_box"
            replaceTarget="false"
            button="true"
            indicator="set_additionalIpV4RouteGroup_to_node_indicator"
            validate="true"
            validateFunction="validation"
            onBeforeTopics="removeErrors"
            onSuccessTopics="removeErrors,set_additionalIpV4RouteGroup_to_node_success"
            onErrorTopics="setError"
            clearForm="true"
            value="Set"
          />
          <img id="set_additionalIpV4RouteGroup_to_node_indicator" src="images/loading_small.gif" alt="Loading..." style="display: none;" />
        </div>
      </fieldset>
    </s:form>
  </div>

</div>

<div id="set_additionalIpV4RouteGroup_to_node_right_column" class="right-column">
  <div id="node_with_additionalIpV4RouteGroup_grid_box" class="grid-box">
    <jsp:include page="node-with-additional-ip-v4-route-group-grid.jsp" />
  </div>
</div>

