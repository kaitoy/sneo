<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<div class="giane-tab-content-form-column">
  <div>
    <s:form
      id="set_additionalIpV6RouteGroup_to_node_form"
      theme="simple"
      cssClass="giane-form"
    >
      <fieldset>
        <legend><s:text name="set_additionalIpV6RouteGroup_to_node.form" /></legend>
        <div class="giane-form-field-box">
          <s:hidden id="node_with_additionalIpV6RouteGroup_id" name="node" />
        </div>
        <div class="giane-form-field-box">
          <s:textfield
            id="node_with_additionalIpV6RouteGroup_name"
            name="name"
            label="%{getText('node.name.label')}"
            theme="xhtml"
            disabled="true"
            cssClass="giane-disabled-field"
          />
        </div>
        <div class="giane-form-field-box">
          <s:select
            id="node_with_additionalIpV6RouteGroup_additionalIpV6RouteGroup"
            name="additionalIpV6RouteGroup"
            label="%{getText('node.additionalIpV6RouteGroup.label')}"
            list="%{additionalIpV6RouteGroups}"
            emptyOption="true"
            multiple="false"
            required="false"
            requiredposition="left"
            theme="xhtml"
            cssClass="select-field"
          />
          <span class="giane-form-error-message" id="set_additionalIpV6RouteGroup_to_node_form_additionalIpV6RouteGroupError"></span>
        </div>
        <div>
          <table class="submit-button-table">
            <tbody>
              <tr>
                <td class="single-button-cell">
                  <sj:submit
                    value="%{getText('form.saveButton.label')}"
                    button="true"
                    cssClass="giane-form-button"
                    onClickTopics="saveButtonClicked"
                  />
                  <s:url var="set_additionalIpV6RouteGroup_to_node_url" action="set-additional-ip-v6-route-group-to-node">
                    <s:param name="simulation_id" value="%{#parameters.simulation_id}" />
                  </s:url>
                  <sj:submit
                    listenTopics="doSave_set_additionalIpV6RouteGroup_to_node"
                    href="%{set_additionalIpV6RouteGroup_to_node_url}"
                    targets="trash_box"
                    replaceTarget="false"
                    indicator="set_additionalIpV6RouteGroup_to_node_indicator"
                    validate="true"
                    validateFunction="validation"
                    onBeforeTopics="removeErrors"
                    onSuccessTopics="set_additionalIpV6RouteGroup_to_node_success"
                    onErrorTopics="setError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                </td>
                <td class="single-button-indicator-cell">
                  <img
                    id="set_additionalIpV6RouteGroup_to_node_indicator"
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

<div class="giane-tab-content-grid-column">
  <div class="giane-grid-box">
    <jsp:include page="node-with-additional-ip-v6-route-group-grid.jsp" flush="true" />
  </div>
</div>

