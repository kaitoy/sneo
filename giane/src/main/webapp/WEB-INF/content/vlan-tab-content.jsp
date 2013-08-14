<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="vlan_create_url" action="vlan-create">
  <s:param name="node_id" value="%{#parameters.node_id}" />
</s:url>
<s:url var="vlan_update_url" action="vlan-update">
  <s:param name="node_id" value="%{#parameters.node_id}" />
</s:url>

<div class="left-column">
  <div>
    <s:form id="vlan_create_form" theme="simple">
      <fieldset>
        <legend><s:text name="new.vlan" /></legend>
        <div>
          <s:textfield name="model.name" label="%{getText('vlan.name.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="vlan_create_form_nameError"></span>
        </div>
        <div>
          <s:textfield name="model.vid" label="%{getText('vlan.vid.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="vlan_create_form_vidError"></span>
        </div>
        <div>
          <sj:submit
            href="%{vlan_create_url}"
            targets="trash_box"
            replaceTarget="false"
            button="true"
            indicator="vlan_create_indicator"
            validate="true"
            validateFunction="validation"
            onBeforeTopics="removeErrors"
            onSuccessTopics="removeErrors,vlanTableUpdated"
            onErrorTopics="createError"
            clearForm="true"
            value="Create"
          />
          <img id="vlan_create_indicator" src="images/loading_small.gif" alt="Loading..." style="display:none" />
        </div>
      </fieldset>
    </s:form>
  </div>

  <div>
    <s:form id="vlan_update_form" theme="simple">
      <fieldset>
        <legend><s:text name="selected.vlan" /></legend>
        <div>
          <label for="vlan_grid_selected_id"><s:text name="vlan.id.label" />:</label>
          <s:hidden id="vlan_grid_selected_id" name="model.id" />
          <span id="vlan_grid_selected_id_span" ></span>
        </div>
        <div>
          <s:textfield id="vlan_grid_selected_name" name="model.name" label="%{getText('vlan.name.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="vlan_update_form_nameError"></span>
        </div>
        <div>
          <s:textfield id="vlan_grid_selected_vid" name="model.vid" label="%{getText('vlan.vid.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="vlan_update_form_vidError"></span>
        </div>
        <div>
          <sj:submit
            href="%{vlan_update_url}"
            targets="trash_box"
            replaceTarget="false"
            button="true"
            indicator="vlan_update_indicator"
            validate="true"
            validateFunction="validation"
            onBeforeTopics="removeErrors"
            onSuccessTopics="removeErrors,vlanTableUpdated"
            onErrorTopics="updateError"
            clearForm="true"
            value="Update"
          />
          <img id="vlan_update_indicator" src="images/loading_small.gif" alt="Loading..." style="display:none" />
        </div>
      </fieldset>
    </s:form>
  </div>

</div>

<div class="right-column">
  <div class="grid-box">
    <jsp:include page="vlan-grid.jsp" />
  </div>
</div>

<s:url var="vlan_url" action="vlan" />
<sj:submit
  href="%{vlan_url}"
  formIds="vlan_update_form"
  targets="main"
  replaceTarget="false"
  indicator="main_indicator"
  listenTopics="vlan_rowDblClicked"
  style="display:none"
/>
