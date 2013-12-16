<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="association-container">
  <s:url
    var="trapTargetGroup_associated_trapTarget_grid_box_url"
    action="associate-trap-target-group-with-trap-targets-grid-box"
    escapeAmp="false"
  >
    <s:param name="trapTargetGroup_id" value="%{#parameters.trapTargetGroup_id}" />
    <s:param name="gridId" value="'trapTargetGroup_associated_trapTarget_grid'" />
    <s:param name="gridAction" value="'trap-target-group-associated-trap-target-grid'" />
    <s:param name="gridCaption" value="%{getText('trapTargetGroup.associated.trapTarget.grid.caption')}" />
  </s:url>
  <sj:div
    href="%{trapTargetGroup_associated_trapTarget_grid_box_url}"
    indicator="trapTargetGroup_associated_trapTarget_grid_box_indicator"
    cssClass="association-associated-grid-box"
  />
  <img
    id="trapTargetGroup_associated_trapTarget_grid_box_indicator"
    src="images/loading_middle.gif"
    alt="Loading..."
    style="display: none;"
    class="association-grid-indicator"
  />

  <div class="association-buttons-box">
    <div>
      <sj:a
        id="remove_from_trapTargetGroup_unassociated_trapTarget_grid"
        onClickTopics="association"
        button="true"
      >
        <img src="images/arrow_left.png" alt="associate" class="association-arrow" />
      </sj:a>
    </div>
    <div>
      <sj:a
        id="remove_from_trapTargetGroup_associated_trapTarget_grid"
        onClickTopics="association"
        button="true"
      >
        <img src="images/arrow_right.png" alt="unassociate" class="association-arrow" />
      </sj:a>
    </div>
    <div>
      <s:form id="save_trapTargetGroup_associated_trapTarget_grid_form" theme="simple">
        <s:url var="associate_trapTargetGroup_with_trapTargets_url" action="associate-trap-target-group-with-trap-targets" escapeAmp="false">
          <s:param name="trapTargetGroup_id" value="%{#parameters.trapTargetGroup_id}" />
        </s:url>
        <s:hidden id="trapTargetGroup_associated_trapTarget_grid_id_list" name="idList" value="undefined" />
        <div>
          <sj:submit
            id="save_trapTargetGroup_associated_trapTarget_grid"
            href="%{associate_trapTargetGroup_with_trapTargets_url}"
            targets="shared_dialog_box"
            replaceTarget="false"
            validate="false"
            button="true"
            indicator="associate_trapTargetGroup_with_trapTargets_indicator"
            value="%{getText('associateAction.save.button.value')}"
          />
        </div>
        <img id="associate_trapTargetGroup_with_trapTargets_indicator" src="images/loading_small.gif" alt="Loading..." style="display: none;" />
      </s:form>
    </div>
  </div>

  <s:url
    var="trapTargetGroup_unassociated_trapTarget_grid_box_url"
    action="associate-trap-target-group-with-trap-targets-grid-box"
    escapeAmp="false"
  >
    <s:param name="trapTargetGroup_id" value="%{#parameters.trapTargetGroup_id}" />
    <s:param name="gridId" value="'trapTargetGroup_unassociated_trapTarget_grid'" />
    <s:param name="gridAction" value="'trap-target-group-unassociated-trap-target-grid'" />
    <s:param name="gridCaption" value="%{getText('trapTargetGroup.unassociated.trapTarget.grid.caption')}" />
  </s:url>
  <sj:div
    href="%{trapTargetGroup_unassociated_trapTarget_grid_box_url}"
    indicator="trapTargetGroup_unassociated_trapTarget_grid_box_indicator"
    cssClass="association-unassociated-grid-box"
  />
  <img
    id="trapTargetGroup_unassociated_trapTarget_grid_box_indicator"
    src="images/loading_middle.gif"
    alt="Loading..."
    style="display: none;"
    class="association-grid-indicator"
  />
</div>
