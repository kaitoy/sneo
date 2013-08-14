<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="snmpAgent_create_url" action="snmp-agent-create">
  <s:param name="node_id" value="%{#parameters.node_id}" />
</s:url>

<div class="left-column">
  <div>
    <s:form id="snmpAgent_create_form" theme="simple">
      <fieldset>
        <legend><s:text name="new.snmpAgent" /></legend>
        <div>
          <s:textfield name="model.address" label="%{getText('snmpAgent.address.label')}" required="true" requiredposition="left" theme="xhtml" title="Used as the trap source address" />
          <span id="snmpAgent_create_form_addressError"></span>
        </div>
        <div>
          <s:textfield name="model.port" label="%{getText('snmpAgent.port.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="snmpAgent_create_form_portError"></span>
        </div>
        <div>
          <s:textfield name="model.communityName" label="%{getText('snmpAgent.communityName.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="snmpAgent_create_form_communityNameError"></span>
        </div>
        <div>
          <s:textfield name="model.securityName" label="%{getText('snmpAgent.securityName.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="snmpAgent_create_form_securityNameError"></span>
        </div>
        <div>
          <s:textfield name="model.fileMibPath" label="%{getText('snmpAgent.fileMibPath.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="snmpAgent_create_form_fileMibPathError"></span>
        </div>
        <div>
          <s:select
            name="model.fileMibFormat"
            label="%{getText('snmpAgent.fileMibFormat.label')}"
            list="%{formats}"
            multiple="false"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span id="snmpAgent_create_form_fileMibFormatError"></span>
        </div>
        <div>
          <s:textfield name="model.communityStringIndexList"  label="%{getText('snmpAgent.communityStringIndexList.label')}" required="false" requiredposition="left" theme="xhtml" title="Comma separated values" />
          <span id="snmpAgent_create_form_communityStringIndexListError"></span>
        </div>

        <div>
          <sj:submit
            href="%{snmpAgent_create_url}"
            targets="trash_box"
            replaceTarget="false"
            button="true"
            indicator="snmpAgent_create_indicator"
            validate="true"
            validateFunction="validation"
            onBeforeTopics="removeErrors"
            onSuccessTopics="removeErrors,snmpAgentTableUpdated"
            onErrorTopics="createError"
            clearForm="true"
            value="Create"
          />
          <img id="snmpAgent_create_indicator" src="images/loading_small.gif" alt="Loading..." style="display:none" />
        </div>
      </fieldset>
    </s:form>
  </div>

  <div>
    <s:form id="snmpAgent_update_form" action="snmp-agent-update" theme="simple">
      <fieldset>
        <legend><s:text name="selected.snmpAgent" /></legend>
        <div>
          <label for="snmpAgent_grid_selected_id"><s:text name="snmpAgent.id.label" />:</label>
          <span id="snmpAgent_grid_selected_id_span" ></span>
          <s:hidden id="snmpAgent_grid_selected_id" name="model.id" />
        </div>
        <div>
          <s:textfield id="snmpAgent_grid_selected_address" name="model.address" label="%{getText('snmpAgent.address.label')}" required="true" requiredposition="left" theme="xhtml" title="Used as the trap source address" />
          <span id="snmpAgent_update_form_addressError"></span>
        </div>
        <div>
          <s:textfield id="snmpAgent_grid_selected_port" name="model.port" label="%{getText('snmpAgent.port.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="snmpAgent_update_form_portError"></span>
        </div>
        <div>
          <s:textfield id="snmpAgent_grid_selected_communityName" name="model.communityName" label="%{getText('snmpAgent.communityName.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="snmpAgent_update_form_communityNameError"></span>
        </div>
        <div>
          <s:textfield id="snmpAgent_grid_selected_securityName" name="model.securityName" label="%{getText('snmpAgent.securityName.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="snmpAgent_update_form_securityNameError"></span>
        </div>
        <div>
          <s:textfield id="snmpAgent_grid_selected_fileMibPath" name="model.fileMibPath" label="%{getText('snmpAgent.fileMibPath.label')}" required="true" requiredposition="left" theme="xhtml" />
          <span id="snmpAgent_update_form_fileMibPathError"></span>
        </div>
        <div>
          <s:select
            id="snmpAgent_grid_selected_fileMibFormat"
            name="model.fileMibFormat"
            label="%{getText('snmpAgent.fileMibFormat.label')}"
            list="%{formats}"
            emptyOption="true"
            multiple="false"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span id="snmpAgent_update_form_fileMibFormatError"></span>
        </div>
        <div>
          <s:textfield id="snmpAgent_grid_selected_communityStringIndexList" name="model.communityStringIndexList"  label="%{getText('snmpAgent.communityStringIndexList.label')}" required="false" requiredposition="left" theme="xhtml" title="Comma separated values" />
          <span id="snmpAgent_update_form_communityStringIndexListError"></span>
        </div>

        <div>
          <sj:submit
            targets="trash_box"
            replaceTarget="false"
            button="true"
            indicator="snmpAgent_update_indicator"
            validate="true"
            validateFunction="validation"
            onBeforeTopics="removeErrors"
            onSuccessTopics="removeErrors,snmpAgentTableUpdated"
            onErrorTopics="updateError"
            clearForm="true"
            value="Update"
          />
          <img id="snmpAgent_update_indicator" src="images/loading_small.gif" alt="Loading..." style="display:none" />
        </div>
      </fieldset>
    </s:form>
  </div>

</div>

<div class="right-column">
  <div class="grid-box">
    <jsp:include page="snmp-agent-grid.jsp" />
  </div>
</div>

