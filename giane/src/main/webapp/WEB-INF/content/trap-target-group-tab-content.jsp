<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="left-column">
  <div>
    <s:form id="trapTargetGroup_create_form" action="trap-target-group-create" theme="simple">
      <fieldset>
        <legend><s:text name="new.trapTargetGroup" /></legend>
        <div>
          <s:textfield name="model.name" label="%{getText('trapTargetGroup.name.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="trapTargetGroup_create_form_nameError"></span>
        </div>
        <div>
          <s:textarea name="model.descr" label="%{getText('trapTargetGroup.descr.label')}" cols="30" rows="2" required="false" requiredposition="left" resizable="false" theme="xhtml" />
          <span id="trapTargetGroup_create_form_descrError"></span>
        </div>
        <div>
          <sj:submit
            targets="trash_box"
            replaceTarget="false"
            button="true"
            indicator="trapTargetGroup_create_indicator"
            validate="true"
            validateFunction="validation"
            onBeforeTopics="removeErrors"
            onSuccessTopics="removeErrors,trapTargetGroupTableUpdated"
            onErrorTopics="createError"
            clearForm="true"
            value="Create"
          />
          <img id="trapTargetGroup_create_indicator" src="images/loading_small.gif" alt="Loading..." style="display:none" />
        </div>
      </fieldset>
    </s:form>
  </div>

  <div>
    <s:form id="trapTargetGroup_update_form" action="trap-target-group-update" theme="simple">
      <fieldset>
        <legend><s:text name="selected.trapTargetGroup" /></legend>
        <div>
          <label for="trapTargetGroup_grid_selected_id"><s:text name="trapTargetGroup.id.label" />:</label>
          <s:hidden id="trapTargetGroup_grid_selected_id" name="model.id" />
          <span id="trapTargetGroup_grid_selected_id_span" ></span>
        </div>
        <div>
          <s:textfield id="trapTargetGroup_grid_selected_name" name="model.name" label="%{getText('trapTargetGroup.name.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="trapTargetGroup_update_form_nameError"></span>
        </div>
        <div>
          <s:textarea id="trapTargetGroup_grid_selected_descr" name="model.descr" label="%{getText('trapTargetGroup.descr.label')}" cols="30" rows="2" required="false" requiredposition="left" resizable="false" theme="xhtml" />
          <span id="trapTargetGroup_update_form_descrError"></span>
        </div>
        <div>
          <sj:submit
            targets="trash_box"
            replaceTarget="false"
            button="true"
            indicator="trapTargetGroup_update_indicator"
            validate="true"
            validateFunction="validation"
            onBeforeTopics="removeErrors"
            onSuccessTopics="removeErrors,trapTargetGroupTableUpdated"
            onErrorTopics="updateError"
            clearForm="true"
            value="Update"
          />
          <img id="trapTargetGroup_update_indicator" src="images/loading_small.gif" alt="Loading..." style="display:none" />
        </div>
      </fieldset>
    </s:form>
  </div>

</div>

<div class="right-column">
  <div class="grid-box">
    <jsp:include page="trap-target-group-grid.jsp" />
  </div>
</div>

<s:url var="trapTargetGroup_url" action="trap-target-group" />
<sj:submit
  href="%{trapTargetGroup_url}"
  formIds="trapTargetGroup_update_form"
  targets="main"
  replaceTarget="false"
  indicator="main_indicator"
  listenTopics="trapTargetGroup_rowDblClicked"
  style="display:none"
/>
