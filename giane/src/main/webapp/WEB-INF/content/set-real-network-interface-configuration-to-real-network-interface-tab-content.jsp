<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="set_realNetworkInterfaceConfiguration_to_realNetworkInterface_url" action="set-real-network-interface-configuration-to-real-network-interface">
  <s:param name="simulation_id" value="%{#parameters.simulation_id}" />
</s:url>

<div id="set_realNetworkInterfaceConfiguration_to_realNetworkInterface_left_column" class="left-column">
  <div id="set_realNetworkInterfaceConfiguration_to_realNetworkInterface_box">
    <s:form id="set_realNetworkInterfaceConfiguration_to_realNetworkInterface_form" theme="simple">
      <fieldset id="set_realNetworkInterfaceConfiguration_to_realNetworkInterface_fields">
        <legend><s:text name="selected.realNetworkInterface.with.realNetworkInterfaceConfiguration" /></legend>
        <div class="type-hidden">
          <label for="realNetworkInterface_with_realNetworkInterfaceConfiguration_grid_selected_id"><s:text name="realNetworkInterface.id.label" /></label>
          <span id="realNetworkInterface_with_realNetworkInterfaceConfiguration_grid_selected_id_span" ></span>
          <s:hidden id="realNetworkInterface_with_realNetworkInterfaceConfiguration_grid_selected_id" name="realNetworkInterface" />
        </div>
        <div class="type-text">
          <label for="realNetworkInterface_with_realNetworkInterfaceConfiguration_grid_selected_name"><s:text name="realNetworkInterface.name.label" /></label>
          <span id="realNetworkInterface_with_realNetworkInterfaceConfiguration_grid_selected_name_span" ></span>
        </div>
        <div class="type-text">
          <label for="realNetworkInterface_with_realNetworkInterfaceConfiguration_grid_selected_realNetworkInterfaceConfiguration"><s:text name="realNetworkInterface.realNetworkInterfaceConfiguration.label" /></label>
          <s:select
            id="realNetworkInterface_with_realNetworkInterfaceConfiguration_grid_selected_realNetworkInterfaceConfiguration"
            name="realNetworkInterfaceConfiguration"
            list="%{realNetworkInterfaceConfigurations}"
            emptyOption="true"
            multiple="false"
            required="true"
            theme="simple"
            cssClass="select-field"
          />
          <label for="realNetworkInterface_with_realNetworkInterfaceConfiguration_grid_selected_realNetworkInterfaceConfiguration"><span id="set_realNetworkInterfaceConfiguration_to_realNetworkInterface_form_realNetworkInterfaceConfigurationError"></span></label>
        </div>

        <div class="type-button">
          <sj:submit
            id="submit_set_realNetworkInterfaceConfiguration_to_realNetworkInterface"
            href="%{set_realNetworkInterfaceConfiguration_to_realNetworkInterface_url}"
            targets="realNetworkInterface_with_realNetworkInterfaceConfiguration_grid_box"
            replaceTarget="false"
            button="true"
            indicator="set_realNetworkInterfaceConfiguration_to_realNetworkInterface_indicator"
            validate="true"
            validateFunction="validation"
            onBeforeTopics="removeErrors"
            onSuccessTopics="removeErrors,set_realNetworkInterfaceConfiguration_to_realNetworkInterface_success"
            onErrorTopics="setError"
            clearForm="true"
            value="Set"
          />
          <img id="set_realNetworkInterfaceConfiguration_to_realNetworkInterface_indicator" src="images/loading_small.gif" alt="Loading..." style="display:none" />
        </div>
      </fieldset>
    </s:form>
  </div>

</div>

<div id="set_realNetworkInterfaceConfiguration_to_realNetworkInterface_right_column" class="right-column">
  <div id="realNetworkInterface_with_realNetworkInterfaceConfiguration_grid_box" class="grid-box">
    <jsp:include page="real-network-interface-with-real-network-interface-configuration-grid.jsp" />
  </div>
</div>

