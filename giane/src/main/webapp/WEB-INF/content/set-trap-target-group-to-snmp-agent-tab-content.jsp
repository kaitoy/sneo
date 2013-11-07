<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<div class="left-column">
  <div>
    <s:form
      id="set_trapTargetGroup_to_snmpAgent_form"
      theme="simple"
      cssClass="giane-form"
    >
      <fieldset>
        <legend><s:text name="set_trapTargetGroup_to_snmpAgent.form" /></legend>
        <div>
          <s:hidden id="snmpAgent_with_trapTargetGroup_id" name="snmpAgent" />
        </div>
        <div>
          <s:textfield
            id="snmpAgent_with_trapTargetGroup_address"
            name="address"
            label="%{getText('snmpAgent.address.label')}"
            theme="xhtml"
            disabled="true"
            cssClass="giane-disabled-field"
          />
        </div>
        <div>
          <s:textfield
            id="snmpAgent_with_trapTargetGroup_hostNode"
            name="hostNode"
            label="%{getText('snmpAgent.hostNode.label')}"
            theme="xhtml"
            disabled="true"
            cssClass="giane-disabled-field"
          />
        </div>
        <div>
          <s:select
            id="snmpAgent_with_trapTargetGroup_trapTargetGroup"
            name="trapTargetGroup"
            label="%{getText('snmpAgent.trapTargetGroup.label')}"
            list="%{trapTargetGroups}"
            emptyOption="true"
            multiple="false"
            required="false"
            requiredposition="left"
            theme="xhtml"
            cssClass="select-field"
          />
          <label for="snmpAgent_with_trapTargetGroup_trapTargetGroup"><span id="set_trapTargetGroup_to_snmpAgent_form_trapTargetGroupError"></span></label>
        </div>
        <div>
          <table class="submit-button-table">
            <tbody>
              <tr>
                <td class="set-button-cell">
                  <sj:submit
                    value="%{getText('form.saveButton.label')}"
                    button="true"
                    cssClass="giane-form-button"
                    onClickTopics="saveButtonClicked"
                  />
                  <s:url var="set_trapTargetGroup_to_snmpAgent_url" action="set-trap-target-group-to-snmp-agent">
                    <s:param name="simulation_id" value="%{#parameters.simulation_id}" />
                  </s:url>
                  <sj:submit
                    listenTopics="doSave_set_trapTargetGroup_to_snmpAgent"
                    href="%{set_trapTargetGroup_to_snmpAgent_url}"
                    targets="trash_box"
                    replaceTarget="false"
                    indicator="set_trapTargetGroup_to_snmpAgent_indicator"
                    validate="true"
                    validateFunction="validation"
                    onBeforeTopics="removeErrors"
                    onSuccessTopics="removeErrors,set_trapTargetGroup_to_snmpAgent_success"
                    onErrorTopics="setError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                </td>
                <td class="set-button-indicator-cell">
                  <img
                    id="set_trapTargetGroup_to_snmpAgent_indicator"
                    src="images/loading_small.gif"
                    alt="Loading..."
                    style="display: none;"
                  />
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </fieldset>
    </s:form>
  </div>

</div>

<div class="right-column">
  <div class="grid-box">
    <jsp:include page="snmp-agent-with-trap-target-group-grid.jsp" />
  </div>
</div>

