<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="physicalNetworkInterface_create_url" action="physical-network-interface-create">
  <s:param name="node_id" value="%{#parameters.node_id}" />
</s:url>
<s:url var="physicalNetworkInterface_update_url" action="physical-network-interface-update">
  <s:param name="node_id" value="%{#parameters.node_id}" />
</s:url>

<div class="left-column">
  <div>
    <s:form id="physicalNetworkInterface_create_form" theme="simple">
      <fieldset>
        <legend><s:text name="new.physicalNetworkInterface" /></legend>
        <div>
          <s:textfield name="model.name"  label="%{getText('physicalNetworkInterface.name.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="physicalNetworkInterface_create_form_nameError"></span>
        </div>

        <div>
          <sj:submit
            href="%{physicalNetworkInterface_create_url}"
            targets="trash_box"
            replaceTarget="false"
            button="true"
            indicator="physicalNetworkInterface_create_indicator"
            validate="true"
            validateFunction="validation"
            onBeforeTopics="removeErrors"
            onSuccessTopics="removeErrors,physicalNetworkInterfaceTableUpdated"
            onErrorTopics="createError"
            clearForm="true"
            value="Create"
          />
          <img id="physicalNetworkInterface_create_indicator" src="images/loading_small.gif" alt="Loading..." style="display:none" />
        </div>
      </fieldset>
    </s:form>
  </div>

  <div>
    <s:form id="physicalNetworkInterface_update_form" theme="simple">
      <fieldset>
        <legend><s:text name="selected.physicalNetworkInterface" /></legend>
        <div>
          <label for="physicalNetworkInterface_grid_selected_id"><s:text name="physicalNetworkInterface.id.label" />:</label>
          <span id="physicalNetworkInterface_grid_selected_id_span" ></span>
          <s:hidden id="physicalNetworkInterface_grid_selected_id" name="model.id" />
        </div>
        <div>
          <s:textfield id="physicalNetworkInterface_grid_selected_name" name="model.name" label="%{getText('physicalNetworkInterface.name.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="physicalNetworkInterface_update_form_nameError"></span>
        </div>

        <div>
          <sj:submit
            href="%{physicalNetworkInterface_update_url}"
            targets="trash_box"
            replaceTarget="false"
            button="true"
            indicator="physicalNetworkInterface_update_indicator"
            validate="true"
            validateFunction="validation"
            onBeforeTopics="removeErrors"
            onSuccessTopics="removeErrors,physicalNetworkInterfaceTableUpdated"
            onErrorTopics="updateError"
            clearForm="true"
            value="Update"
          />
          <img id="physicalNetworkInterface_update_indicator" src="images/loading_small.gif" alt="Loading..." style="display:none" />
        </div>
      </fieldset>
    </s:form>
  </div>

</div>

<div class="right-column">
  <div class="grid-box">
    <jsp:include page="physical-network-interface-grid.jsp" />
  </div>
</div>

<s:url var="physicalNetworkInterface_url" action="physical-network-interface" />
<sj:submit
  href="%{physicalNetworkInterface_url}"
  formIds="physicalNetworkInterface_update_form"
  targets="main"
  replaceTarget="false"
  indicator="main_indicator"
  listenTopics="physicalNetworkInterface_rowDblClicked"
  style="display:none"
/>
