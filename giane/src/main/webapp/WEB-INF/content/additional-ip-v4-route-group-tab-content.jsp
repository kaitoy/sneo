<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="left-column">
  <div>
    <s:form id="additionalIpV4RouteGroup_create_form" action="additional-ip-v4-route-group-create" theme="simple">
      <fieldset>
        <legend><s:text name="new.additionalIpV4RouteGroup" /></legend>
        <div>
          <s:textfield name="model.name" label="%{getText('additionalIpV4RouteGroup.name.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="additionalIpV4RouteGroup_create_form_nameError"></span>
        </div>
        <div>
          <sj:submit
            targets="trash_box"
            replaceTarget="false"
            button="true"
            indicator="additionalIpV4RouteGroup_create_indicator"
            validate="true"
            validateFunction="validation"
            onBeforeTopics="removeErrors"
            onSuccessTopics="removeErrors,additionalIpV4RouteGroupTableUpdated"
            onErrorTopics="createError"
            clearForm="true"
            value="Create"
          />
          <img id="additionalIpV4RouteGroup_create_indicator" src="images/loading_small.gif" alt="Loading..." style="display:none" />
        </div>
      </fieldset>
    </s:form>
  </div>

  <div>
    <s:form id="additionalIpV4RouteGroup_update_form" action="additional-ip-v4-route-group-update" theme="simple">
      <fieldset>
        <legend><s:text name="selected.additionalIpV4RouteGroup" /></legend>
        <div>
          <label for="additionalIpV4RouteGroup_grid_selected_id"><s:text name="additionalIpV4RouteGroup.id.label" />:</label>
          <s:hidden id="additionalIpV4RouteGroup_grid_selected_id" name="model.id" />
          <span id="additionalIpV4RouteGroup_grid_selected_id_span" ></span>
        </div>
        <div>
          <s:textfield id="additionalIpV4RouteGroup_grid_selected_name" name="model.name" label="%{getText('additionalIpV4RouteGroup.name.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="additionalIpV4RouteGroup_update_form_nameError"></span>
        </div>
        <div>
          <sj:submit
            targets="trash_box"
            replaceTarget="false"
            button="true"
            indicator="additionalIpV4RouteGroup_update_indicator"
            validate="true"
            validateFunction="validation"
            onBeforeTopics="removeErrors"
            onSuccessTopics="removeErrors,additionalIpV4RouteGroupTableUpdated"
            onErrorTopics="updateError"
            clearForm="true"
            value="Update"
          />
          <img id="additionalIpV4RouteGroup_update_indicator" src="images/loading_small.gif" alt="Loading..." style="display:none" />
        </div>
      </fieldset>
    </s:form>
  </div>

</div>

<div class="right-column">
  <div class="grid-box">
    <jsp:include page="additional-ip-v4-route-group-grid.jsp" />
  </div>
</div>

<s:url var="additionalIpV4RouteGroup_url" action="additional-ip-v4-route-group" />
<sj:submit
  href="%{additionalIpV4RouteGroup_url}"
  formIds="additionalIpV4RouteGroup_update_form"
  targets="main"
  replaceTarget="false"
  indicator="main_indicator"
  listenTopics="additionalIpV4RouteGroup_rowDblClicked"
  style="display:none"
/>
