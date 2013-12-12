<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="association-container">
  <div id="associate_trapTargetGroup_with_trapTargets_wapper_column" class="association-wrapper-column">
    <s:url var="trapTargetGroup_associated_trapTarget_grid_box_url" action="trap-target-group-associated-trap-target-grid-box"  escapeAmp="false">
      <s:param name="trapTargetGroup_id" value="%{#parameters.trapTargetGroup_id}" />
      <s:param name="trapTargetGroup_name" value="%{#parameters.trapTargetGroup_name}" />
    </s:url>
    <sj:div
      id="associate_trapTargetGroup_with_trapTargets_left_column"
      href="%{trapTargetGroup_associated_trapTarget_grid_box_url}"
      indicator="associate_trapTargetGroup_with_trapTargets_left_column_indicator"
      cssClass="association-left-column"
    >
      <img
        id="associate_trapTargetGroup_with_trapTargets_left_column_indicator"
        src="images/loading_middle.gif"
        alt="Loading..."
        style="display: none;"
        class="association-grid-indicator"
      />
    </sj:div>

    <div id="associate_trapTargetGroup_with_trapTargets_middle_column" class="association-middle-column">
      <sj:a
        id="remove_from_trapTargetGroup_unassociated_trapTarget_grid"
        onClickTopics="association"
        button="true"
      >
        <img src="images/arrow_left.png" alt="associate" class="association-arrow" />
      </sj:a>
      <br /><br />
      <sj:a
        id="remove_from_trapTargetGroup_associated_trapTarget_grid"
        onClickTopics="association"
        button="true"
      >
        <img src="images/arrow_right.png" alt="unassociate" class="association-arrow" />
      </sj:a>
      <br /><br />
      <s:form id="save_trapTargetGroup_associated_trapTarget_grid_form" theme="simple">
        <s:url var="associate_trapTargetGroup_with_trapTargets_url" action="associate-trap-target-group-with-trap-targets" escapeAmp="false">
          <s:param name="trapTargetGroup_id" value="%{#parameters.trapTargetGroup_id}" />
          <s:param name="trapTargetGroup_vid" value="%{#parameters.trapTargetGroup_vid}" />
        </s:url>
        <div class="type-hidden">
          <s:hidden id="trapTargetGroup_associated_trapTarget_grid_id_list" name="idList" value="undefined" />
        </div>
        <sj:submit
          id="save_trapTargetGroup_associated_trapTarget_grid"
          href="%{associate_trapTargetGroup_with_trapTargets_url}"
          targets="shared_dialog_box"
          replaceTarget="false"
          validate="false"
          onErrorTopics="save_trapTargetGroup_associated_trapTarget_grid_error"
          button="true"
          indicator="associate_trapTargetGroup_with_trapTargets_indicator"
          value="%{getText('associateAction.save.button.value')}"
        />
        <br />
        <img id="associate_trapTargetGroup_with_trapTargets_indicator" src="images/loading_small.gif" alt="Loading..." style="display: none;" />
      </s:form>
    </div>
  </div>

  <s:url var="trapTargetGroup_unassociated_trapTarget_grid_box_url" action="trap-target-group-unassociated-trap-target-grid-box"  escapeAmp="false">
    <s:param name="trapTargetGroup_id" value="%{#parameters.trapTargetGroup_id}" />
    <s:param name="trapTargetGroup_name" value="%{#parameters.trapTargetGroup_name}" />
  </s:url>
  <sj:div
    id="associate_trapTargetGroup_with_trapTargets_right_column"
    href="%{trapTargetGroup_unassociated_trapTarget_grid_box_url}"
    indicator="associate_trapTargetGroup_with_trapTargets_right_column_indicator"
    cssClass="association-right-column"
  >
    <img
      id="associate_trapTargetGroup_with_trapTargets_right_column_indicator"
      src="images/loading_middle.gif"
      alt="Loading..."
      style="display: none;"
      class="association-grid-indicator"
    />
  </sj:div>

  <sj:dialog
    id="save_trapTargetGroup_associated_trapTarget_grid_error_dialog"
    openTopics="save_trapTargetGroup_associated_trapTarget_grid_error"
    showEffect="scale"
    hideEffect="puff"
    autoOpen="false"
    modal="true"
    title="%{getText('associateAction.error.dialog.title')}"
    buttons="{
      'OK': function() {$('#save_trapTargetGroup_associated_trapTarget_grid_error_dialog').dialog('close'); }
    }"
    dialogClass="giane-dialog"
  >
    <s:text name="associateAction.error.dialog.text" />
  </sj:dialog>
</div>
