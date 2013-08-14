<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="ipAddress_create_url" action="ip-address-create">
  <s:param name="ipAddressRelation_id" value="%{#parameters.ipAddressRelation_id}" />
</s:url>

<div class="left-column">
  <div>
    <s:form id="ipAddress_create_form" theme="simple">
      <fieldset>
        <legend><s:text name="new.ipAddress" /></legend>
        <div>
          <s:textfield name="model.address" label="%{getText('ipAddress.address.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="ipAddress_create_form_addressError"></span>
        </div>
        <div>
          <s:textfield name="model.prefixLength" label="%{getText('ipAddress.prefixLength.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="ipAddress_create_form_prefixLengthError"></span>
        </div>
        <div>
          <sj:submit
            href="%{ipAddress_create_url}"
            targets="trash_box"
            replaceTarget="false"
            button="true"
            indicator="ipAddress_create_indicator"
            validate="true"
            validateFunction="validation"
            onBeforeTopics="removeErrors"
            onSuccessTopics="removeErrors,ipAddressTableUpdated"
            onErrorTopics="createError"
            clearForm="true"
            value="Create"
          />
          <img id="ipAddress_create_indicator" src="images/loading_small.gif" alt="Loading..." style="display:none" />
        </div>
      </fieldset>
    </s:form>
  </div>

  <div>
    <s:form id="ipAddress_update_form" action="ip-address-update" theme="simple">
      <fieldset>
        <legend><s:text name="selected.ipAddress" /></legend>
        <div>
          <label for="ipAddress_grid_selected_id"><s:text name="ipAddress.id.label" />:</label>
          <s:hidden id="ipAddress_grid_selected_id" name="model.id" />
          <span id="ipAddress_grid_selected_id_span" ></span>
        </div>
        <div>
          <s:textfield id="ipAddress_grid_selected_address" name="model.address" label="%{getText('ipAddress.address.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="ipAddress_update_form_addressError"></span>
        </div>
        <div>
          <s:textfield id="ipAddress_grid_selected_prefixLength" name="model.prefixLength" label="%{getText('ipAddress.prefixLength.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="ipAddress_update_form_prefixLengthError"></span>
        </div>
        <div>
          <sj:submit
            targets="trash_box"
            replaceTarget="false"
            button="true"
            indicator="ipAddress_update_indicator"
            validate="true"
            validateFunction="validation"
            onBeforeTopics="removeErrors"
            onSuccessTopics="removeErrors,ipAddressTableUpdated"
            onErrorTopics="updateError"
            clearForm="true"
            value="Update"
          />
          <img id="ipAddress_update_indicator" src="images/loading_small.gif" alt="Loading..." style="display:none" />
        </div>
      </fieldset>
    </s:form>
  </div>

</div>

<div class="right-column">
  <div class="grid-box">
    <jsp:include page="ip-address-grid.jsp" />
  </div>
</div>
