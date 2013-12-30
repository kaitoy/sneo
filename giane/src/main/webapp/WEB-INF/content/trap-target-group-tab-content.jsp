<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="giane-tab-content-form-column">
  <div>
    <s:form id="trapTargetGroup_form" theme="simple" cssClass="giane-form">
      <fieldset>
        <legend><s:text name="trapTargetGroup.form" /></legend>
        <div class="giane-form-field-box">
          <s:hidden id="trapTargetGroup_id" name="model.id" />
        </div>
        <div class="giane-form-field-box">
          <s:textfield
            id="trapTargetGroup_name"
            name="model.name"
            label="%{getText('entityGroup.name.label')}"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span class="giane-form-error-message" id="trapTargetGroup_form_nameError"></span>
        </div>
        <div class="giane-form-field-box">
          <s:textarea
            id="trapTargetGroup_descr"
            name="model.descr"
            label="%{getText('entityGroup.descr.label')}"
            required="false"
            requiredposition="left"
            theme="xhtml"
          />
          <span class="giane-form-error-message" id="trapTargetGroup_form_descrError"></span>
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
                  <s:url var="trapTargetGroup_create_url" action="trap-target-group-create" />
                  <sj:submit
                    listenTopics="doCreate_trapTargetGroup"
                    href="%{trapTargetGroup_create_url}"
                    targets="trash_box"
                    replaceTarget="false"
                    indicator="trapTargetGroup_create_indicator"
                    validate="true"
                    validateFunction="validation"
                    onBeforeTopics="removeErrors"
                    onSuccessTopics="removeErrors,trapTargetGroupTableUpdated"
                    onErrorTopics="createError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                </td>
                <td class="two-buttons-left-indicator-cell">
                  <img
                    id="trapTargetGroup_create_indicator"
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
                  <s:url var="trapTargetGroup_update_url" action="trap-target-group-update" />
                  <sj:submit
                    listenTopics="doUpdate_trapTargetGroup"
                    href="%{trapTargetGroup_update_url}"
                    targets="trash_box"
                    replaceTarget="false"
                    indicator="trapTargetGroup_update_indicator"
                    validate="true"
                    validateFunction="validation"
                    onBeforeTopics="removeErrors"
                    onSuccessTopics="removeErrors,trapTargetGroupTableUpdated"
                    onErrorTopics="updateError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                  <img
                    id="trapTargetGroup_update_indicator"
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

<s:url var="trapTargetGroup_grid_box_url" action="trap-target-group-grid-box" escapeAmp="false">
  <s:param name="modelNameCamel" value="'trapTargetGroup'" />
  <s:param name="modelNameHyphen" value="'trap-target-group'" />
  <s:param name="gridCaption" value="%{getText('trapTargetGroup.grid.caption')}" />
</s:url>
<div class="giane-tab-content-grid-column">
  <sj:div href="%{trapTargetGroup_grid_box_url}" indicator="trapTargetGroup_grid_box_indicator" cssClass="giane-grid-box" />
  <img id="trapTargetGroup_grid_box_indicator" src="images/loading_small.gif" alt="Loading..." style="display: none;" />
</div>

<s:url var="trapTargetGroup_url" action="trap-target-group">
  <s:param name="tabIndex" value="%{#parameters.tabIndex}" />
</s:url>
<sj:submit
  href="%{trapTargetGroup_url}"
  formIds="trapTargetGroup_form"
  targets="config_main"
  replaceTarget="false"
  indicator="config_main_indicator"
  validate="true"
  validateFunction="checkRowSelection"
  listenTopics="trapTargetGroup_rowDblClicked"
  onBeforeTopics="configMainPaneGoingForward_before"
  onAfterValidationTopics="configMainPaneGoingForward_after"
  onCompleteTopics="configMainPaneCompleted"
  style="display: none;"
/>
