<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="association-container">
  <div id="associate_additionalIpV4RouteGroup_with_additionalIpV4Routes_wapper_column" class="association-wrapper-column">
    <s:url var="additionalIpV4RouteGroup_associated_additionalIpV4Route_grid_box_url" action="additional-ip-v4-route-group-associated-additional-ip-v4-route-grid-box"  escapeAmp="false">
      <s:param name="additionalIpV4RouteGroup_id" value="%{#parameters.additionalIpV4RouteGroup_id}" />
      <s:param name="additionalIpV4RouteGroup_name" value="%{#parameters.additionalIpV4RouteGroup_name}" />
    </s:url>
    <sj:div
      id="associate_additionalIpV4RouteGroup_with_additionalIpV4Routes_left_column"
      href="%{additionalIpV4RouteGroup_associated_additionalIpV4Route_grid_box_url}"
      indicator="associate_additionalIpV4RouteGroup_with_additionalIpV4Routes_left_column_indicator"
      cssClass="association-left-column"
    >
      <img
        id="associate_additionalIpV4RouteGroup_with_additionalIpV4Routes_left_column_indicator"
        src="images/loading_middle.gif"
        alt="Loading..."
        style="display: none;"
        class="association-grid-indicator"
      />
    </sj:div>

    <div id="associate_additionalIpV4RouteGroup_with_additionalIpV4Routes_middle_column" class="association-middle-column">
      <sj:a
        id="remove_from_additionalIpV4RouteGroup_unassociated_additionalIpV4Route_grid"
        onClickTopics="association"
        button="true"
      >
        <img src="images/arrow_left.png" alt="associate" class="association-arrow" />
      </sj:a>
      <br /><br />
      <sj:a
        id="remove_from_additionalIpV4RouteGroup_associated_additionalIpV4Route_grid"
        onClickTopics="association"
        button="true"
      >
        <img src="images/arrow_right.png" alt="unassociate" class="association-arrow" />
      </sj:a>
      <br /><br />
      <s:form id="save_additionalIpV4RouteGroup_associated_additionalIpV4Route_grid_form" theme="simple">
        <s:url var="associate_additionalIpV4RouteGroup_with_additionalIpV4Routes_url" action="associate-additional-ip-v4-route-group-with-additional-ip-v4-routes" escapeAmp="false">
          <s:param name="additionalIpV4RouteGroup_id" value="%{#parameters.additionalIpV4RouteGroup_id}" />
          <s:param name="additionalIpV4RouteGroup_vid" value="%{#parameters.additionalIpV4RouteGroup_vid}" />
        </s:url>
        <div class="type-hidden">
          <s:hidden id="additionalIpV4RouteGroup_associated_additionalIpV4Route_grid_id_list" name="idList" value="undefined" />
        </div>
        <sj:submit
          id="save_additionalIpV4RouteGroup_associated_additionalIpV4Route_grid"
          href="%{associate_additionalIpV4RouteGroup_with_additionalIpV4Routes_url}"
          targets="shared_dialog_box"
          replaceTarget="false"
          validate="false"
          onErrorTopics="save_additionalIpV4RouteGroup_associated_additionalIpV4Route_grid_error"
          button="true"
          indicator="associate_additionalIpV4RouteGroup_with_additionalIpV4Routes_indicator"
          value="%{getText('associateAction.save.button.value')}"
        />
        <br />
        <img id="associate_additionalIpV4RouteGroup_with_additionalIpV4Routes_indicator" src="images/loading_small.gif" alt="Loading..." style="display: none;" />
      </s:form>
    </div>
  </div>

  <s:url var="additionalIpV4RouteGroup_unassociated_additionalIpV4Route_grid_box_url" action="additional-ip-v4-route-group-unassociated-additional-ip-v4-route-grid-box"  escapeAmp="false">
    <s:param name="additionalIpV4RouteGroup_id" value="%{#parameters.additionalIpV4RouteGroup_id}" />
    <s:param name="additionalIpV4RouteGroup_name" value="%{#parameters.additionalIpV4RouteGroup_name}" />
  </s:url>
  <sj:div
    id="associate_additionalIpV4RouteGroup_with_additionalIpV4Routes_right_column"
    href="%{additionalIpV4RouteGroup_unassociated_additionalIpV4Route_grid_box_url}"
    indicator="associate_additionalIpV4RouteGroup_with_additionalIpV4Routes_right_column_indicator"
    cssClass="association-right-column"
  >
    <img
      id="associate_additionalIpV4RouteGroup_with_additionalIpV4Routes_right_column_indicator"
      src="images/loading_middle.gif"
      alt="Loading..."
      style="display: none;"
      class="association-grid-indicator"
    />
  </sj:div>

  <sj:dialog
    id="save_additionalIpV4RouteGroup_associated_additionalIpV4Route_grid_error_dialog"
    openTopics="save_additionalIpV4RouteGroup_associated_additionalIpV4Route_grid_error"
    showEffect="scale"
    hideEffect="puff"
    autoOpen="false"
    modal="true"
    title="%{getText('associateAction.error.dialog.title')}"
    buttons="{
      'OK': function() {$('#save_additionalIpV4RouteGroup_associated_additionalIpV4Route_grid_error_dialog').dialog('close'); }
    }"
    dialogClass="giane-dialog"
  >
    <s:text name="associateAction.error.dialog.text" />
  </sj:dialog>
</div>
