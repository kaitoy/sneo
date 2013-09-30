<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="left-column">
  <div>
    <s:form id="network_create_form" action="network-create" theme="simple">
      <fieldset>
        <legend><s:text name="new.network" /></legend>
        <div>
          <s:textfield name="model.name" label="%{getText('network.name.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="network_create_form_nameError"></span>
        </div>
        <div>
          <s:textarea name="model.descr" label="%{getText('network.descr.label')}" cols="30" rows="2" required="false" requiredposition="left" resizable="false" theme="xhtml" />
          <span id="network_create_form_descrError"></span>
        </div>
        <div>
          <sj:submit
            targets="trash_box"
            replaceTarget="false"
            button="true"
            indicator="network_create_indicator"
            validate="true"
            validateFunction="validation"
            onBeforeTopics="removeErrors"
            onSuccessTopics="removeErrors,networkTableUpdated"
            onErrorTopics="createError"
            clearForm="true"
            value="Create"
          />
          <img id="network_create_indicator" src="images/loading_small.gif" alt="Loading..." style="display:none" />
        </div>
      </fieldset>
    </s:form>
  </div>

  <div>
    <s:form id="network_update_form" action="network-update" theme="simple">
      <fieldset>
        <legend><s:text name="selected.network" /></legend>
        <div>
          <label for="network_grid_selected_id"><s:text name="network.id.label" />:</label>
          <s:hidden id="network_grid_selected_id" name="model.id" />
          <span id="network_grid_selected_id_span" ></span>
        </div>
        <div>
          <s:textfield id="network_grid_selected_name" name="model.name" label="%{getText('network.name.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="network_update_form_nameError"></span>
        </div>
        <div>
          <s:textarea id="network_grid_selected_descr" name="model.descr" label="%{getText('network.descr.label')}" cols="30" rows="2" required="false" requiredposition="left" resizable="false" theme="xhtml" />
          <span id="network_update_form_descrError"></span>
        </div>
        <div>
          <sj:submit
            targets="trash_box"
            replaceTarget="false"
            button="true"
            indicator="network_update_indicator"
            validate="true"
            validateFunction="validation"
            onBeforeTopics="removeErrors"
            onSuccessTopics="removeErrors,networkTableUpdated"
            onErrorTopics="updateError"
            clearForm="true"
            value="Update"
          />
          <img id="network_update_indicator" src="images/loading_small.gif" alt="Loading..." style="display:none" />
        </div>
      </fieldset>
    </s:form>
  </div>

</div>

<div class="right-column">
  <div class="grid-box">
    <jsp:include page="network-grid.jsp" />
  </div>
</div>

<s:url var="network_url" action="network" />
<sj:submit
  href="%{network_url}"
  formIds="network_update_form"
  targets="main"
  replaceTarget="false"
  indicator="main_indicator"
  listenTopics="network_rowDblClicked"
  style="display: none"
/>

<div id="network_grid_box"></div>
