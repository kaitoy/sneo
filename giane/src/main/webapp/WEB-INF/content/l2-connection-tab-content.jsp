<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="l2Connection_create_url" action="l2-connection-create">
  <s:param name="network_id" value="%{#parameters.network_id}" />
</s:url>
<s:url var="l2Connection_update_url" action="l2-connection-update">
  <s:param name="network_id" value="%{#parameters.network_id}" />
</s:url>

<div class="left-column">
  <div>
    <s:form id="l2Connection_create_form" theme="simple">
      <fieldset>
        <legend><s:text name="new.l2Connection" /></legend>
        <div>
          <s:textfield name="model.name" label="%{getText('l2Connection.name.label')}" required="true" requiredposition="left" theme="xhtml"/>
          <span id="l2Connection_create_form_nameError"></span>
        </div>

        <div>
          <sj:submit
            href="%{l2Connection_create_url}"
            targets="trash_box"
            replaceTarget="false"
            button="true"
            indicator="l2Connection_create_indicator"
            validate="true"
            validateFunction="validation"
            onBeforeTopics="removeErrors"
            onSuccessTopics="removeErrors,l2ConnectionTableUpdated"
            onErrorTopics="createError"
            clearForm="true"
            value="Create"
          />
          <img id="l2Connection_create_indicator" src="images/loading_small.gif" alt="Loading..." style="display:none" />
        </div>
      </fieldset>
    </s:form>
  </div>

  <div>
    <s:form id="l2Connection_update_form" theme="simple">
      <fieldset>
        <legend><s:text name="selected.l2Connection" /></legend>
        <div>
          <label for="l2Connection_grid_selected_id"><s:text name="l2Connection.id.label" />:</label>
          <s:hidden id="l2Connection_grid_selected_id" name="model.id" />
          <span id="l2Connection_grid_selected_id_span" ></span>
        </div>
        <div>
          <s:textfield id="l2Connection_grid_selected_name" name="model.name" label="%{getText('l2Connection.name.label')}" required="true" requiredposition="left" theme="xhtml"/>
          <span id="l2Connection_update_form_nameError"></span>
        </div>

        <div>
          <sj:submit
            targets="trash_box"
            href="%{l2Connection_update_url}"
            replaceTarget="false"
            button="true"
            indicator="l2Connection_update_indicator"
            validate="true"
            validateFunction="validation"
            onBeforeTopics="removeErrors"
            onSuccessTopics="removeErrors,l2ConnectionTableUpdated"
            onErrorTopics="updateError"
            clearForm="true"
            value="Update"
          />
          <img id="l2Connection_update_indicator" src="images/loading_small.gif" alt="Loading..." style="display:none" />
        </div>
      </fieldset>
    </s:form>
  </div>

</div>

<div class="right-column">
  <div class="grid-box">
    <jsp:include page="l2-connection-grid.jsp" />
  </div>
</div>

<s:url var="l2Connection_url" action="l2-connection" />
<sj:submit
  href="%{l2Connection_url}"
  formIds="l2Connection_update_form"
  targets="main"
  replaceTarget="false"
  indicator="main_indicator"
  listenTopics="l2Connection_rowDblClicked"
  style="display:none"
/>

