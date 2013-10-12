<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="association-container">
  <div id="associate_l2Connection_with_physicalNetworkInterfaces_wapper_column" class="association-wrapper-column">
    <s:url var="l2Connection_associated_physicalNetworkInterface_grid_box_url" action="l2-connection-associated-physical-network-interface-grid-box"  escapeAmp="false">
      <s:param name="network_id" value="%{#parameters.network_id}" />
      <s:param name="l2Connection_id" value="%{#parameters.l2Connection_id}" />
      <s:param name="l2Connection_name" value="%{#parameters.l2Connection_name}" />
    </s:url>
    <sj:div
      id="associate_l2Connection_with_physicalNetworkInterfaces_left_column"
      href="%{l2Connection_associated_physicalNetworkInterface_grid_box_url}"
      indicator="associate_l2Connection_with_physicalNetworkInterfaces_left_column_indicator"
      cssClass="association-left-column"
    >
      <img
        id="associate_l2Connection_with_physicalNetworkInterfaces_left_column_indicator"
        src="images/loading_middle.gif"
        alt="Loading..."
        style="display: none;"
        class="association-grid-indicator"
      />
    </sj:div>

    <div id="associate_l2Connection_with_physicalNetworkInterfaces_middle_column" class="association-middle-column">
      <sj:a
        id="remove_from_l2Connection_unassociated_physicalNetworkInterface_grid"
        onClickTopics="association_group"
        button="true"
      >
        <img src="images/arrow_left.png" alt="associate" class="association-arrow" />
      </sj:a>
      <br /><br />
      <sj:a
        id="remove_from_l2Connection_associated_physicalNetworkInterface_grid"
        onClickTopics="association_group"
        button="true"
      >
        <img src="images/arrow_right.png" alt="unassociate" class="association-arrow" />
      </sj:a>
      <br /><br />
      <s:form id="save_l2Connection_associated_physicalNetworkInterface_grid_form" theme="simple">
        <s:url var="associate_l2Connection_with_physicalNetworkInterfaces_url" action="associate-l2-connection-with-physical-network-interfaces">
          <s:param name="l2Connection_id" value="%{#parameters.l2Connection_id}" />
        </s:url>
        <div class="type-hidden">
          <s:hidden id="l2Connection_associated_physicalNetworkInterface_grid_id_list" name="idList" value="undefined" />
        </div>
        <sj:submit
          id="save_l2Connection_associated_physicalNetworkInterface_grid"
          href="%{associate_l2Connection_with_physicalNetworkInterfaces_url}"
          targets="shared_dialog_box"
          replaceTarget="false"
          validate="false"
          onErrorTopics="save_l2Connection_associated_physicalNetworkInterface_grid_error"
          button="true"
          indicator="associate_l2Connection_with_physicalNetworkInterfaces_indicator"
          value="%{getText('save.associated.grid.button.value')}"
        />
        <br />
        <img id="associate_l2Connection_with_physicalNetworkInterfaces_indicator" src="images/loading_small.gif" alt="Loading..." style="display: none;" />
      </s:form>
    </div>
  </div>

  <s:url var="l2Connection_unassociated_physicalNetworkInterface_grid_box_url" action="l2-connection-unassociated-physical-network-interface-grid-box"  escapeAmp="false">
    <s:param name="network_id" value="%{#parameters.network_id}" />
    <s:param name="l2Connection_id" value="%{#parameters.l2Connection_id}" />
    <s:param name="l2Connection_name" value="%{#parameters.l2Connection_name}" />
  </s:url>
  <sj:div
    id="associate_l2Connection_with_physicalNetworkInterfaces_right_column"
    href="%{l2Connection_unassociated_physicalNetworkInterface_grid_box_url}"
    indicator="associate_l2Connection_with_physicalNetworkInterfaces_right_column_indicator"
    cssClass="association-right-column"
  >
    <img
      id="associate_l2Connection_with_physicalNetworkInterfaces_right_column_indicator"
      src="images/loading_middle.gif"
      alt="Loading..."
      style="display: none;"
      class="association-grid-indicator"
    />
  </sj:div>

  <sj:dialog
    id="save_l2Connection_associated_physicalNetworkInterface_grid_error_dialog"
    openTopics="save_l2Connection_associated_physicalNetworkInterface_grid_error"
    showEffect="scale"
    hideEffect="puff"
    autoOpen="false"
    modal="true"
    title="%{getText('save.associated.grid.error.dialog.title')}"
    dialogClass="dialog"
  >
    <s:text name="save.associated.grid.error.dialog.text" />
  </sj:dialog>
</div>
