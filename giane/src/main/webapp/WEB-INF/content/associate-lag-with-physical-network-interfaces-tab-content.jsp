<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="association-container">
  <div id="associate_lag_with_physicalNetworkInterfaces_wapper_column" class="association-wrapper-column">
    <s:url
      var="lag_associated_physicalNetworkInterface_grid_box_url"
      action="lag-associated-physical-network-interface-grid-box"
      escapeAmp="false"
    >
      <s:param name="node_id" value="%{#parameters.node_id}" />
      <s:param name="lag_id" value="%{#parameters.lag_id}" />
      <s:param name="lag_name" value="%{#parameters.lag_name}" />
    </s:url>
    <sj:div
      id="associate_lag_with_physicalNetworkInterfaces_left_column"
      href="%{lag_associated_physicalNetworkInterface_grid_box_url}"
      indicator="associate_lag_with_physicalNetworkInterfaces_left_column_indicator"
      cssClass="association-left-column"
    >
      <img
        id="associate_lag_with_physicalNetworkInterfaces_left_column_indicator"
        src="images/loading_middle.gif"
        alt="Loading..."
        style="display: none;"
        class="association-grid-indicator"
      />
    </sj:div>

    <div id="associate_lag_with_physicalNetworkInterfaces_middle_column" class="association-middle-column">
      <sj:a
        id="remove_from_lag_unassociated_physicalNetworkInterface_grid"
        onClickTopics="association"
        button="true"
      >
        <img src="images/arrow_left.png" alt="associate" class="association-arrow" />
      </sj:a>
      <br /><br />
      <sj:a
        id="remove_from_lag_associated_physicalNetworkInterface_grid"
        onClickTopics="association"
        button="true"
      >
        <img src="images/arrow_right.png" alt="unassociate" class="association-arrow" />
      </sj:a>
      <br /><br />
      <s:form id="save_lag_associated_physicalNetworkInterface_grid_form" theme="simple">
        <s:url
          var="associate_lag_with_physicalNetworkInterfaces_url"
          action="associate-lag-with-physical-network-interfaces"
          escapeAmp="false"
        >
          <s:param name="lag_id" value="%{#parameters.lag_id}" />
          <s:param name="lag_name" value="%{#parameters.lag_name}" />
        </s:url>
        <div class="type-hidden">
          <s:hidden id="lag_associated_physicalNetworkInterface_grid_id_list" name="idList" value="undefined" />
        </div>
        <sj:submit
          id="save_lag_associated_physicalNetworkInterface_grid"
          href="%{associate_lag_with_physicalNetworkInterfaces_url}"
          targets="shared_dialog_box"
          replaceTarget="false"
          validate="false"
          onErrorTopics="save_lag_associated_physicalNetworkInterface_grid_error"
          button="true"
          indicator="associate_lag_with_physicalNetworkInterfaces_indicator"
          value="%{getText('associateAction.save.button.value')}"
        />
        <br />
        <img
          id="associate_lag_with_physicalNetworkInterfaces_indicator"
          src="images/loading_small.gif"
          alt="Loading..."
          style="display: none;"
        />
      </s:form>
    </div>
  </div>

  <s:url
    var="lag_unassociated_physicalNetworkInterface_grid_box_url"
    action="lag-unassociated-physical-network-interface-grid-box"
    escapeAmp="false"
  >
    <s:param name="node_id" value="%{#parameters.node_id}" />
    <s:param name="lag_id" value="%{#parameters.lag_id}" />
    <s:param name="lag_name" value="%{#parameters.lag_name}" />
  </s:url>
  <sj:div
    id="associate_lag_with_physicalNetworkInterfaces_right_column"
    href="%{lag_unassociated_physicalNetworkInterface_grid_box_url}"
    indicator="associate_lag_with_physicalNetworkInterfaces_right_column_indicator"
    cssClass="association-right-column"
  >
    <img
      id="associate_lag_with_physicalNetworkInterfaces_right_column_indicator"
      src="images/loading_middle.gif"
      alt="Loading..."
      style="display: none;"
      class="association-grid-indicator"
    />
  </sj:div>

  <sj:dialog
    id="save_lag_associated_physicalNetworkInterface_grid_error_dialog"
    openTopics="save_lag_associated_physicalNetworkInterface_grid_error"
    showEffect="scale"
    hideEffect="puff"
    autoOpen="false"
    modal="true"
    title="%{getText('associateAction.error.dialog.title')}"
    dialogClass="dialog"
  >
    <s:text name="associateAction.error.dialog.text" />
  </sj:dialog>
</div>
