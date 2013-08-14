<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="realNetworkInterface_create_url" action="real-network-interface-create">
  <s:param name="node_id" value="%{#parameters.node_id}" />
</s:url>
<s:url var="realNetworkInterface_update_url" action="real-network-interface-update">
  <s:param name="node_id" value="%{#parameters.node_id}" />
</s:url>

<div class="left-column">
  <div>
    <s:form id="realNetworkInterface_create_form" theme="simple">
      <fieldset>
        <legend><s:text name="new.realNetworkInterface" /></legend>
        <div>
          <s:textfield name="model.name"  label="%{getText('realNetworkInterface.name.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="realNetworkInterface_create_form_nameError"></span>
        </div>
        <div>
          <sj:submit
            href="%{realNetworkInterface_create_url}"
            targets="trash_box"
            replaceTarget="false"
            button="true"
            indicator="realNetworkInterface_create_indicator"
            validate="true"
            validateFunction="validation"
            onBeforeTopics="removeErrors"
            onSuccessTopics="removeErrors,realNetworkInterfaceTableUpdated"
            onErrorTopics="createError"
            clearForm="true"
            value="Create"
          />
          <img id="realNetworkInterface_create_indicator" src="images/loading_small.gif" alt="Loading..." style="display: none" />
        </div>
      </fieldset>
    </s:form>
  </div>

  <div>
    <s:form id="realNetworkInterface_update_form" theme="simple">
      <fieldset>
        <legend><s:text name="selected.realNetworkInterface" /></legend>
        <div>
          <label for="realNetworkInterface_grid_selected_id"><s:text name="realNetworkInterface.id.label" />:</label>
          <span id="realNetworkInterface_grid_selected_id_span" ></span>
          <s:hidden id="realNetworkInterface_grid_selected_id" name="model.id" />
        </div>
        <div>
          <s:textfield id="realNetworkInterface_grid_selected_name" name="model.name" label="%{getText('realNetworkInterface.name.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="realNetworkInterface_update_form_nameError"></span>
        </div>
        <div>
          <sj:submit
            href="%{realNetworkInterface_update_url}"
            targets="trash_box"
            replaceTarget="false"
            button="true"
            indicator="realNetworkInterface_update_indicator"
            validate="true"
            validateFunction="validation"
            onBeforeTopics="removeErrors"
            onSuccessTopics="removeErrors,realNetworkInterfaceTableUpdated"
            onErrorTopics="updateError"
            clearForm="true"
            value="Update"
          />
          <img id="realNetworkInterface_update_indicator" src="images/loading_small.gif" alt="Loading..." style="display: none" />
        </div>
      </fieldset>
    </s:form>
  </div>

</div>

<div class="right-column">
  <div class="grid-box">
    <jsp:include page="real-network-interface-grid.jsp" />
  </div>
</div>

