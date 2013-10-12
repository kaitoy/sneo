<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="left-column">
  <div>
    <s:form id="trapTargetGroup_form" theme="simple" cssClass="giane-form">
      <fieldset>
        <legend><s:text name="trapTargetGroup.form" /></legend>
        <div>
          <s:hidden id="trapTargetGroup_id" name="model.id" />
        </div>
        <div>
          <s:textfield
            id="trapTargetGroup_name"
            name="model.name"
            label="%{getText('trapTargetGroup.name.label')}"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span id="trapTargetGroup_form_nameError"></span>
        </div>
        <div>
          <s:textarea
            id="trapTargetGroup_descr"
            name="model.descr"
            label="%{getText('trapTargetGroup.descr.label')}"
            cols="30"
            required="false"
            requiredposition="left"
            resizable="false"
            theme="xhtml"
          />
          <span id="trapTargetGroup_form_descrError"></span>
        </div>
        <div>
          <table class="submits-table">
            <tbody>
              <tr>
                <td class="left-button-cell">
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
                <td class="left-button-indicator-cell">
                  <img
                    id="trapTargetGroup_create_indicator"
                    src="images/loading_small.gif"
                    alt="Loading..."
                    style="display: none;"
                  />
                </td>
                <td class="right-button-cell">
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

<div class="right-column">
  <div class="grid-box">
    <jsp:include page="trap-target-group-grid.jsp" />
  </div>
</div>

<s:url var="trapTargetGroup_url" action="trap-target-group" />
<sj:submit
  href="%{trapTargetGroup_url}"
  formIds="trapTargetGroup_form"
  targets="main"
  replaceTarget="false"
  indicator="main_indicator"
  listenTopics="trapTargetGroup_rowDblClicked"
  style="display: none;"
/>
