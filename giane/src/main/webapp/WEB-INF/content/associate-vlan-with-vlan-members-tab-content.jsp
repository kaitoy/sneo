<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="association-container clearfix">
  <s:url
    var="vlan_associated_vlanMember_grid_box_url"
    action="associate-vlan-with-vlan-members-grid-box"
    escapeAmp="false"
  >
    <s:param name="node_id" value="%{#parameters.node_id}" />
    <s:param name="vlan_id" value="%{#parameters.vlan_id}" />
    <s:param name="gridId" value="'vlan_associated_vlanMember_grid'" />
    <s:param name="gridAction" value="'vlan-associated-vlan-member-grid'" />
    <s:param name="gridCaption" value="'vlan.associated.vlanMember.grid.caption'" />
  </s:url>
  <sj:div
    href="%{vlan_associated_vlanMember_grid_box_url}"
    indicator="vlan_associated_vlanMember_grid_box_indicator"
    cssClass="association-associated-grid-box"
  />
  <img
    id="vlan_associated_vlanMember_grid_box_indicator"
    src="images/loading_middle.gif"
    alt="Loading..."
    style="display: none;"
    class="association-grid-indicator"
  />

  <div class="association-buttons-box">
    <div>
      <sj:a
        id="remove_from_vlan_unassociated_vlanMember_grid"
        onClickTopics="association"
        button="true"
      >
        <img src="images/arrow_left.png" alt="associate" class="association-arrow" />
      </sj:a>
    </div>
    <div>
      <sj:a
        id="remove_from_vlan_associated_vlanMember_grid"
        onClickTopics="association"
        button="true"
      >
        <img src="images/arrow_right.png" alt="unassociate" class="association-arrow" />
      </sj:a>
    </div>
    <div>
      <s:form id="save_vlan_associated_vlanMember_grid_form" theme="simple">
        <s:url var="associate_vlan_with_vlanMembers_url" action="associate-vlan-with-vlan-members" escapeAmp="false">
          <s:param name="vlan_id" value="%{#parameters.vlan_id}" />
        </s:url>
        <s:hidden id="vlan_associated_vlanMember_grid_id_list" name="idList" value="undefined" />
        <div>
          <sj:submit
            id="save_vlan_associated_vlanMember_grid"
            href="%{associate_vlan_with_vlanMembers_url}"
            targets="shared_dialog_box"
            replaceTarget="false"
            validate="false"
            button="true"
            indicator="associate_vlan_with_vlanMembers_indicator"
            value="%{getText('associateAction.save.button.value')}"
          />
        </div>
        <img id="associate_vlan_with_vlanMembers_indicator" src="images/loading_small.gif" alt="Loading..." style="display: none;" />
      </s:form>
    </div>
  </div>

  <s:url
    var="vlan_unassociated_vlanMember_grid_box_url"
    action="associate-vlan-with-vlan-members-grid-box"
    escapeAmp="false"
  >
    <s:param name="node_id" value="%{#parameters.node_id}" />
    <s:param name="vlan_id" value="%{#parameters.vlan_id}" />
    <s:param name="gridId" value="'vlan_unassociated_vlanMember_grid'" />
    <s:param name="gridAction" value="'vlan-unassociated-vlan-member-grid'" />
    <s:param name="gridCaption" value="'vlan.unassociated.vlanMember.grid.caption'" />
  </s:url>
  <sj:div
    href="%{vlan_unassociated_vlanMember_grid_box_url}"
    indicator="vlan_unassociated_vlanMember_grid_box_indicator"
    cssClass="association-unassociated-grid-box"
  />
  <img
    id="vlan_unassociated_vlanMember_grid_box_indicator"
    src="images/loading_middle.gif"
    alt="Loading..."
    style="display: none;"
    class="association-grid-indicator"
  />
</div>
