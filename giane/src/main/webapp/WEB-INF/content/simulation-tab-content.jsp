<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="left-column">
  <div>
    <s:form id="simulation_create_form" action="simulation-create" theme="simple">
      <fieldset>
        <legend><s:text name="new.simulation" /></legend>
        <div>
          <s:textfield name="model.name" label="%{getText('simulation.name.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="simulation_create_form_nameError"></span>
        </div>
        <div>
          <s:select
            name="model.network"
            label="%{getText('simulation.network.label')}"
            list="%{networks}"
            multiple="false"
            required="true"
            requiredposition="left"
            theme="xhtml"
            cssClass="select-field"
          />
          <span id="simulation_create_form_networkError"></span>
        </div>
        <div>
          <s:textarea name="model.descr" label="%{getText('simulation.descr.label')}" cols="30" rows="2" required="false" requiredposition="left" resizable="false" theme="xhtml" />
          <span id="simulation_create_form_descrError"></span>
        </div>
        <div>
          <sj:submit
            targets="trash_box"
            replaceTarget="false"
            button="true"
            indicator="simulation_create_indicator"
            validate="true"
            validateFunction="validation"
            onBeforeTopics="removeErrors"
            onSuccessTopics="removeErrors,simulationTableUpdated"
            onErrorTopics="createError"
            clearForm="true"
            value="Create"
          />
          <img id="simulation_create_indicator" src="images/loading_small.gif" alt="Loading..." style="display:none" />
        </div>
      </fieldset>
    </s:form>
  </div>

  <div>
    <s:form id="simulation_update_form" action="simulation-update" theme="simple">
      <fieldset>
        <legend><s:text name="selected.simulation" /></legend>
        <div>
          <label for="simulation_grid_selected_id"><s:text name="simulation.id.label" />:</label>
          <s:hidden id="simulation_grid_selected_id" name="model.id" />
          <span id="simulation_grid_selected_id_span" ></span>
        </div>
        <div>
          <s:textfield id="simulation_grid_selected_name" name="model.name" label="%{getText('simulation.name.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="simulation_update_form_nameError"></span>
        </div>
        <div>
          <s:select
            id="simulation_grid_selected_network"
            name="model.network"
            label="%{getText('simulation.network.label')}"
            list="%{networks}"
            emptyOption="true"
            multiple="false"
            required="true"
            requiredposition="left"
            theme="xhtml"
            cssClass="select-field"
          />
          <span id="simulation_update_form_networkError"></span>
        </div>
        <div>
          <s:textarea id="simulation_grid_selected_descr" name="model.descr" label="%{getText('simulation.descr.label')}" cols="30" rows="2" required="false" requiredposition="left" resizable="false" theme="xhtml" />
          <span id="simulation_update_form_descrError"></span>
        </div>
        <div>
          <sj:submit
            targets="trash_box"
            replaceTarget="false"
            button="true"
            indicator="simulation_update_indicator"
            validate="true"
            validateFunction="validation"
            onBeforeTopics="removeErrors"
            onSuccessTopics="removeErrors,simulationTableUpdated"
            onErrorTopics="updateError"
            clearForm="true"
            value="Update"
          />
          <img id="simulation_update_indicator" src="images/loading_small.gif" alt="Loading..." style="display:none" />
        </div>
      </fieldset>
    </s:form>
  </div>

</div>

<div class="right-column">
  <div class="grid-box">
    <jsp:include page="simulation-grid.jsp" />
  </div>
</div>

<s:url var="simulation_url" action="simulation" />
<sj:submit
  href="%{simulation_url}"
  formIds="simulation_update_form"
  targets="main"
  replaceTarget="false"
  indicator="main_indicator"
  listenTopics="simulation_rowDblClicked"
  style="display:none"
/>
