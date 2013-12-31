<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="giane-tab-content-form-column">
  <div>
    <s:form
      id="additionalIpV4RouteGroup_form"
      theme="simple"
      cssClass="giane-form"
    >
      <fieldset>
        <legend><s:text name="additionalIpV4RouteGroup.form" /></legend>
        <div class="giane-form-field-box">
          <s:hidden id="additionalIpV4RouteGroup_id" name="model.id" />
        </div>
        <div class="giane-form-field-box">
          <s:textfield
            id="additionalIpV4RouteGroup_name"
            name="model.name"
            label="%{getText('entityGroup.name.label')}"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span class="giane-form-error-message" id="additionalIpV4RouteGroup_form_nameError"></span>
        </div>
        <div class="giane-form-field-box">
          <s:textarea
            id="additionalIpV4RouteGroup_descr"
            name="model.descr"
            label="%{getText('entityGroup.descr.label')}"
            required="false"
            requiredposition="left"
            theme="xhtml"
          />
          <span class="giane-form-error-message" id="additionalIpV4RouteGroup_form_descrError"></span>
        </div>
        <div>
          <table class="submit-button-table">
            <tbody>
              <tr>
                <td class="two-buttons-first-cell">
                  <sj:submit
                    value="%{getText('form.createButton.label')}"
                    button="true"
                    cssClass="giane-form-button"
                    onClickTopics="createButtonClicked"
                  />
                  <s:url
                    var="additionalIpV4RouteGroup_create_url"
                    action="additional-ip-v4-route-group-create"
                  />
                  <sj:submit
                    listenTopics="doCreate_additionalIpV4RouteGroup"
                    href="%{additionalIpV4RouteGroup_create_url}"
                    targets="trash_box"
                    replaceTarget="false"
                    indicator="additionalIpV4RouteGroup_create_indicator"
                    validate="true"
                    validateFunction="validation"
                    onBeforeTopics="removeErrors"
                    onSuccessTopics="additionalIpV4RouteGroupTableUpdated"
                    onErrorTopics="createError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                </td>
                <td class="two-buttons-left-indicator-cell">
                  <img
                    id="additionalIpV4RouteGroup_create_indicator"
                    src="images/loading_small.gif"
                    alt="Loading..."
                    style="display: none;"
                  />
                </td>
                <td class="two-buttons-second-cell">
                  <sj:submit
                    value="%{getText('form.updateButton.label')}"
                    button="true"
                    cssClass="giane-form-button"
                    onClickTopics="updateButtonClicked"
                  />
                  <s:url
                    var="additionalIpV4RouteGroup_update_url"
                    action="additional-ip-v4-route-group-update"
                  />
                  <sj:submit
                    listenTopics="doUpdate_additionalIpV4RouteGroup"
                    href="%{additionalIpV4RouteGroup_update_url}"
                    targets="trash_box"
                    replaceTarget="false"
                    indicator="additionalIpV4RouteGroup_update_indicator"
                    validate="true"
                    validateFunction="validation"
                    onBeforeTopics="removeErrors"
                    onSuccessTopics="additionalIpV4RouteGroupTableUpdated"
                    onErrorTopics="updateError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                  <img
                    id="additionalIpV4RouteGroup_update_indicator"
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

<s:url var="additionalIpV4RouteGroup_grid_box_url" action="additional-ip-v4-route-group-grid-box" escapeAmp="false">
  <s:param name="modelNameCamel" value="'additionalIpV4RouteGroup'" />
  <s:param name="modelNameHyphen" value="'additional-ip-v4-route-group'" />
  <s:param name="gridCaption" value="%{getText('additionalIpV4RouteGroup.grid.caption')}" />
</s:url>
<div class="giane-tab-content-grid-column">
  <sj:div href="%{additionalIpV4RouteGroup_grid_box_url}" indicator="additionalIpV4RouteGroup_grid_box_indicator" cssClass="giane-grid-box" />
  <img id="additionalIpV4RouteGroup_grid_box_indicator" src="images/loading_small.gif" alt="Loading..." style="display: none;" />
</div>

<s:url var="additionalIpV4RouteGroup_url" action="additional-ip-v4-route-group">
  <s:param name="tabIndex" value="%{#parameters.tabIndex}" />
</s:url>
<sj:submit
  href="%{additionalIpV4RouteGroup_url}"
  formIds="additionalIpV4RouteGroup_form"
  targets="config_main"
  replaceTarget="false"
  indicator="config_main_indicator"
  validate="true"
  validateFunction="checkRowSelection"
  listenTopics="additionalIpV4RouteGroup_rowDblClicked"
  onBeforeTopics="mainPaneGoingForward_before"
  onAfterValidationTopics="mainPaneGoingForward_after"
  onCompleteTopics="mainPaneCompleted"
  style="display: none;"
/>
