<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="association-container">
  <s:url
    var="lag_associated_physicalNetworkInterface_grid_box_url"
    action="associate-lag-with-physical-network-interfaces-grid-box"
    escapeAmp="false"
  >
    <s:param name="node_id" value="%{#parameters.node_id}" />
    <s:param name="lag_id" value="%{#parameters.lag_id}" />
    <s:param name="gridId" value="'lag_associated_physicalNetworkInterface_grid'" />
    <s:param name="gridAction" value="'lag-associated-physical-network-interface-grid'" />
    <s:param name="gridCaption" value="%{getText('lag.associated.physicalNetworkInterface.grid.caption')}" />
  </s:url>
  <sj:div
    href="%{lag_associated_physicalNetworkInterface_grid_box_url}"
    indicator="lag_associated_physicalNetworkInterface_grid_box_indicator"
    cssClass="association-associated-grid-box"
  />
  <img
    id="lag_associated_physicalNetworkInterface_grid_box_indicator"
    src="images/loading_middle.gif"
    alt="Loading..."
    style="display: none;"
    class="association-grid-indicator"
  />
  
  <div class="association-buttons-box">
    <div>
      <sj:a
        id="remove_from_lag_unassociated_physicalNetworkInterface_grid"
        onClickTopics="association"
        button="true"
      >
        <img src="images/arrow_left.png" alt="associate" class="association-arrow" />
      </sj:a>
    </div>
    <div>
      <sj:a
        id="remove_from_lag_associated_physicalNetworkInterface_grid"
        onClickTopics="association"
        button="true"
      >
        <img src="images/arrow_right.png" alt="unassociate" class="association-arrow" />
      </sj:a>
    </div>
    <div>
      <s:form id="save_lag_associated_physicalNetworkInterface_grid_form" theme="simple">
        <s:url
          var="associate_lag_with_physicalNetworkInterfaces_url"
          action="associate-lag-with-physical-network-interfaces"
          escapeAmp="false"
        >
          <s:param name="lag_id" value="%{#parameters.lag_id}" />
        </s:url>
        <s:hidden id="lag_associated_physicalNetworkInterface_grid_id_list" name="idList" value="undefined" />
        <div>
          <sj:submit
            id="save_lag_associated_physicalNetworkInterface_grid"
            href="%{associate_lag_with_physicalNetworkInterfaces_url}"
            targets="shared_dialog_box"
            replaceTarget="false"
            validate="false"
            button="true"
            indicator="associate_lag_with_physicalNetworkInterfaces_indicator"
            value="%{getText('associateAction.save.button.value')}"
          />
        </div>
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
    action="associate-lag-with-physical-network-interfaces-grid-box"
    escapeAmp="false"
  >
    <s:param name="node_id" value="%{#parameters.node_id}" />
    <s:param name="lag_id" value="%{#parameters.lag_id}" />
    <s:param name="gridId" value="'lag_unassociated_physicalNetworkInterface_grid'" />
    <s:param name="gridAction" value="'lag-unassociated-physical-network-interface-grid'" />
    <s:param name="gridCaption" value="%{getText('lag.unassociated.physicalNetworkInterface.grid.caption')}" />
  </s:url>
  <sj:div
    href="%{lag_unassociated_physicalNetworkInterface_grid_box_url}"
    indicator="lag_unassociated_physicalNetworkInterface_grid_box_indicator"
    cssClass="association-unassociated-grid-box"
  />
  <img
    id="lag_unassociated_physicalNetworkInterface_grid_box_indicator"
    src="images/loading_middle.gif"
    alt="Loading..."
    style="display: none;"
    class="association-grid-indicator"
  />
</div>
