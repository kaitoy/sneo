<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="node_create_url" action="node-create">
  <s:param name="network_id" value="%{#parameters.network_id}" />
</s:url>
<s:url var="node_update_url" action="node-update">
  <s:param name="network_id" value="%{#parameters.network_id}" />
</s:url>

<div class="left-column">
  <div>
    <s:form id="node_create_form" theme="simple">
      <fieldset>
        <legend><s:text name="new.node" /></legend>
        <div>
          <s:textfield name="model.name" label="%{getText('node.name.label')}" required="true" requiredposition="left" theme="xhtml"/>
          <span id="node_create_form_nameError"></span>
        </div>
        <div>
          <s:textfield name="model.ttl" label="%{getText('node.ttl.label')}" required="true" requiredposition="left" theme="xhtml"/>
          <span id="node_create_form_ttlError"></span>
        </div>
        <div>
          <sj:submit
            href="%{node_create_url}"
            targets="trash_box"
            replaceTarget="false"
            button="true"
            indicator="node_create_indicator"
            validate="true"
            validateFunction="validation"
            onBeforeTopics="removeErrors"
            onSuccessTopics="removeErrors,nodeTableUpdated"
            onErrorTopics="createError"
            clearForm="true"
            value="Create"
          />
          <img id="node_create_indicator" src="images/loading_small.gif" alt="Loading..." style="display: none" />
        </div>
      </fieldset>
    </s:form>
  </div>

  <div>
    <s:form id="node_update_form" theme="simple">
      <fieldset>
        <legend><s:text name="selected.node" /></legend>
        <div>
          <label for="node_grid_selected_id"><s:text name="node.id.label" />:</label>
          <s:hidden id="node_grid_selected_id" name="model.id" />
          <span id="node_grid_selected_id_span" ></span>
        </div>
        <div>
          <s:textfield id="node_grid_selected_name" name="model.name" label="%{getText('node.name.label')}" required="true" requiredposition="left" theme="xhtml"/>
          <span id="node_update_form_nameError"></span>
        </div>
        <div>
          <s:textfield id="node_grid_selected_ttl" name="model.ttl" label="%{getText('node.ttl.label')}" required="true" requiredposition="left" theme="xhtml"/>
          <span id="node_update_form_ttlError"></span>
        </div>
        <div>
          <sj:submit
            href="%{node_update_url}"
            targets="trash_box"
            replaceTarget="false"
            button="true"
            indicator="node_update_indicator"
            validate="true"
            validateFunction="validation"
            onBeforeTopics="removeErrors"
            onSuccessTopics="removeErrors,nodeTableUpdated"
            onErrorTopics="updateError"
            clearForm="true"
            value="Update"
          />
          <img id="node_update_indicator" src="images/loading_small.gif" alt="Loading..." style="display: none" />
        </div>
      </fieldset>
    </s:form>
  </div>

</div>

<div class="right-column">
  <div class="grid-box">
    <jsp:include page="node-grid.jsp" />
  </div>
</div>

<s:url var="node_url" action="node" />
<sj:submit
  href="%{node_url}"
  formIds="node_update_form"
  targets="main"
  replaceTarget="false"
  indicator="main_indicator"
  listenTopics="node_rowDblClicked"
  style="display: none"
/>

