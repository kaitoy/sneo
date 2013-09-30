<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="left-column">
  <div>
    <s:form id="trapTarget_create_form" action="trap-target-create" theme="simple">
      <fieldset>
        <legend><s:text name="new.trapTarget" /></legend>
        <div>
          <s:textfield name="model.name" label="%{getText('trapTarget.name.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="trapTarget_create_form_nameError"></span>
        </div>
        <div>
          <s:textfield name="model.address" label="%{getText('trapTarget.address.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="trapTarget_create_form_addressError"></span>
        </div>
        <div>
          <s:textfield name="model.port" label="%{getText('trapTarget.port.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="trapTarget_create_form_portError"></span>
        </div>
        <div>
          <s:textarea name="model.descr" label="%{getText('trapTarget.descr.label')}" cols="30" rows="2" required="false" requiredposition="left" resizable="false" theme="xhtml" />
          <span id="trapTarget_create_form_descrError"></span>
        </div>
        <div>
          <sj:submit
            targets="trash_box"
            replaceTarget="false"
            button="true"
            indicator="trapTarget_create_indicator"
            validate="true"
            validateFunction="validation"
            onBeforeTopics="removeErrors"
            onSuccessTopics="removeErrors,trapTargetTableUpdated"
            onErrorTopics="createError"
            clearForm="true"
            value="Create"
          />
          <img id="trapTarget_create_indicator" src="images/loading_small.gif" alt="Loading..." style="display:none" />
        </div>
      </fieldset>
    </s:form>
  </div>

  <div>
    <s:form id="trapTarget_update_form" action="trap-target-update" theme="simple">
      <fieldset>
        <legend><s:text name="selected.trapTarget" /></legend>
        <div>
          <label for="trapTarget_grid_selected_id"><s:text name="trapTarget.id.label" />:</label>
          <s:hidden id="trapTarget_grid_selected_id" name="model.id" />
          <span id="trapTarget_grid_selected_id_span" ></span>
        </div>
        <div>
          <s:textfield id="trapTarget_grid_selected_name" name="model.name" label="%{getText('trapTarget.name.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="trapTarget_update_form_nameError"></span>
        </div>
        <div>
          <s:textfield id="trapTarget_grid_selected_address" name="model.address" label="%{getText('trapTarget.address.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="trapTarget_update_form_addressError"></span>
        </div>
        <div>
          <s:textfield id="trapTarget_grid_selected_port" name="model.port" label="%{getText('trapTarget.port.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="trapTarget_update_form_portError"></span>
        </div>
        <div>
          <s:textarea id="trapTarget_grid_selected_descr" name="model.descr" label="%{getText('trapTarget.descr.label')}" cols="30" rows="2" required="false" requiredposition="left" resizable="false" theme="xhtml" />
          <span id="trapTarget_update_form_descrError"></span>
        </div>
        <div>
          <sj:submit
            targets="trash_box"
            replaceTarget="false"
            button="true"
            indicator="trapTarget_update_indicator"
            validate="true"
            validateFunction="validation"
            onBeforeTopics="removeErrors"
            onSuccessTopics="removeErrors,trapTargetTableUpdated"
            onErrorTopics="updateError"
            clearForm="true"
            value="Update"
          />
          <img id="trapTarget_update_indicator" src="images/loading_small.gif" alt="Loading..." style="display:none" />
        </div>
      </fieldset>
    </s:form>
  </div>

</div>

<div class="right-column">
  <div class="grid-box">
    <jsp:include page="trap-target-grid.jsp" />
  </div>
</div>
