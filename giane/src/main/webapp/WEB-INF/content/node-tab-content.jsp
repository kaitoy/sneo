<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="left-column">
  <div>
    <s:form id="node_form" theme="simple" cssClass="giane-form">
      <fieldset>
        <legend><s:text name="node.form" /></legend>
        <div>
          <s:hidden id="node_id" name="model.id" />
        </div>
        <div>
          <s:textfield
            id="node_name"
            name="model.name"
            label="%{getText('node.name.label')}"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span id="node_form_nameError"></span>
        </div>
        <div>
          <s:textfield
            id="node_ttl"
            name="model.ttl"
            label="%{getText('node.ttl.label')}"
            required="true"
            requiredposition="left"
            theme="xhtml"
          />
          <span id="node_form_ttlError"></span>
        </div>
        <div>
          <s:textarea
            id="node_descr"
            name="model.descr"
            label="%{getText('node.descr.label')}"
            cols="30"
            required="false"
            requiredposition="left"
            resizable="false"
            theme="xhtml"
          />
          <span id="node_form_descrError"></span>
        </div>
        <div>
          <table class="submit-button-table">
            <tbody>
              <tr>
                <td class="create-button-cell">
                  <sj:submit
                    value="%{getText('form.createButton.label')}"
                    button="true"
                    cssClass="giane-form-button"
                    onClickTopics="createButtonClicked"
                  />
                  <s:url var="node_create_url" action="node-create">
                    <s:param name="network_id" value="%{#parameters.network_id}" />
                  </s:url>
                  <sj:submit
                    listenTopics="doCreate_node"
                    href="%{node_create_url}"
                    targets="trash_box"
                    replaceTarget="false"
                    indicator="node_create_indicator"
                    validate="true"
                    validateFunction="validation"
                    onBeforeTopics="removeErrors"
                    onSuccessTopics="removeErrors,nodeTableUpdated"
                    onErrorTopics="createError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                </td>
                <td class="create-button-indicator-cell">
                  <img id="node_create_indicator" src="images/loading_small.gif" alt="Loading..." style="display: none;" />
                </td>
                <td class="update-button-cell">
                  <sj:submit
                    value="%{getText('form.updateButton.label')}"
                    button="true"
                    cssClass="giane-form-button"
                    onClickTopics="updateButtonClicked"
                  />
                  <s:url var="node_update_url" action="node-update">
                    <s:param name="network_id" value="%{#parameters.network_id}" />
                  </s:url>
                  <sj:submit
                    listenTopics="doUpdate_node"
                    href="%{node_update_url}"
                    targets="trash_box"
                    replaceTarget="false"
                    indicator="node_update_indicator"
                    validate="true"
                    validateFunction="validation"
                    onBeforeTopics="removeErrors"
                    onSuccessTopics="removeErrors,nodeTableUpdated"
                    onErrorTopics="updateError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                  <img id="node_update_indicator" src="images/loading_small.gif" alt="Loading..." style="display: none;" />
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
    <jsp:include page="node-grid.jsp" />
  </div>
</div>

<s:url var="node_url" action="node" />
<sj:submit
  href="%{node_url}"
  formIds="node_form"
  targets="main"
  replaceTarget="false"
  indicator="main_indicator"
  listenTopics="node_rowDblClicked"
  style="display: none;"
/>

