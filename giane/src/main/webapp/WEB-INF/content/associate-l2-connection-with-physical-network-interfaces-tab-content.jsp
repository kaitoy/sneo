<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="association-container clearfix">
  <s:url
    var="l2Connection_associated_physicalNetworkInterface_grid_box_url"
    action="associate-l2-connection-with-physical-network-interfaces-grid-box"
    escapeAmp="false"
  >
    <s:param name="l2Connection_id" value="%{#parameters.l2Connection_id}" />
    <s:param name="gridId" value="'l2Connection_associated_physicalNetworkInterface_grid'" />
    <s:param name="gridAction" value="'l2-connection-associated-physical-network-interface-grid'" />
    <s:param name="gridCaption" value="'l2Connection.associated.physicalNetworkInterface.grid.caption'" />
    <s:param name="gridWidth" value="300" />
  </s:url>
  <sj:div
    href="%{l2Connection_associated_physicalNetworkInterface_grid_box_url}"
    indicator="l2Connection_associated_physicalNetworkInterface_grid_box_indicator"
    cssClass="association-associated-grid-box"
  />
  <img
    id="l2Connection_associated_physicalNetworkInterface_grid_box_indicator"
    src="images/loading_middle.gif"
    alt="Loading..."
    style="display: none;"
    class="association-grid-indicator"
  />

  <div class="association-buttons-box">
    <div>
      <sj:a
        id="remove_from_l2Connection_unassociated_physicalNetworkInterface_grid"
        onClickTopics="association_group"
        button="true"
      >
        <img src="images/arrow_left.png" alt="associate" class="association-arrow" />
      </sj:a>
    </div>
    <div>
      <sj:a
        id="remove_from_l2Connection_associated_physicalNetworkInterface_grid"
        onClickTopics="association_group"
        button="true"
      >
        <img src="images/arrow_right.png" alt="unassociate" class="association-arrow" />
      </sj:a>
    </div>
    <div>
      <s:form id="save_l2Connection_associated_physicalNetworkInterface_grid_form" theme="simple">
        <s:url var="associate_l2Connection_with_physicalNetworkInterfaces_url" action="associate-l2-connection-with-physical-network-interfaces">
          <s:param name="l2Connection_id" value="%{#parameters.l2Connection_id}" />
        </s:url>
        <s:hidden id="l2Connection_associated_physicalNetworkInterface_grid_id_list" name="idList" value="undefined" />
        <div>
          <sj:submit
            id="save_l2Connection_associated_physicalNetworkInterface_grid"
            href="%{associate_l2Connection_with_physicalNetworkInterfaces_url}"
            targets="shared_dialog_box"
            replaceTarget="false"
            validate="false"
            button="true"
            indicator="associate_l2Connection_with_physicalNetworkInterfaces_indicator"
            value="%{getText('associateAction.save.button.value')}"
          />
        </div>
        <img id="associate_l2Connection_with_physicalNetworkInterfaces_indicator" src="images/loading_small.gif" alt="Loading..." style="display: none;" />
      </s:form>
    </div>
  </div>
  
  <s:url
    var="l2Connection_unassociated_physicalNetworkInterface_grid_box_url"
    action="associate-l2-connection-with-physical-network-interfaces-grid-box"
    escapeAmp="false"
  >
    <s:param name="network_id" value="%{#parameters.network_id}" />
    <s:param name="l2Connection_id" value="%{#parameters.l2Connection_id}" />
    <s:param name="gridId" value="'l2Connection_unassociated_physicalNetworkInterface_grid'" />
    <s:param name="gridAction" value="'l2-connection-unassociated-physical-network-interface-grid'" />
    <s:param name="gridCaption" value="'l2Connection.unassociated.physicalNetworkInterface.grid.caption'" />
    <s:param name="groupField">
      ["nodeName"]
    </s:param>
    <s:param name="gridWidth" value="450" />
  </s:url>
  <sj:div
    href="%{l2Connection_unassociated_physicalNetworkInterface_grid_box_url}"
    indicator="l2Connection_unassociated_physicalNetworkInterface_grid_box_indicator"
    cssClass="association-unassociated-grid-box"
  />
  <img
    id="l2Connection_unassociated_physicalNetworkInterface_grid_box_indicator"
    src="images/loading_middle.gif"
    alt="Loading..."
    style="display: none;"
    class="association-grid-indicator"
  />
</div>
