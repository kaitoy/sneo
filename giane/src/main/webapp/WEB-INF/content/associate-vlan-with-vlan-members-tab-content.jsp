<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="association-container">
  <div id="associate_vlan_with_vlanMembers_wapper_column" class="association-wrapper-column">
    <s:url var="vlan_associated_vlanMember_grid_box_url" action="vlan-associated-vlan-member-grid-box"  escapeAmp="false">
      <s:param name="node_id" value="%{#parameters.node_id}" />
      <s:param name="vlan_id" value="%{#parameters.vlan_id}" />
      <s:param name="vlan_name" value="%{#parameters.vlan_name}" />
    </s:url>
    <sj:div
      id="associate_vlan_with_vlanMembers_left_column"
      href="%{vlan_associated_vlanMember_grid_box_url}"
      indicator="associate_vlan_with_vlanMembers_left_column_indicator"
      cssClass="association-left-column"
    >
      <img
        id="associate_vlan_with_vlanMembers_left_column_indicator"
        src="images/loading_middle.gif"
        alt="Loading..."
        style="display: none;"
        class="association-grid-indicator"
      />
    </sj:div>

    <div id="associate_vlan_with_vlanMembers_middle_column" class="association-middle-column">
      <sj:a
        id="remove_from_vlan_unassociated_vlanMember_grid"
        onClickTopics="association"
        button="true"
      >
        <img src="images/arrow_left.png" alt="associate" class="association-arrow" />
      </sj:a>
      <br /><br />
      <sj:a
        id="remove_from_vlan_associated_vlanMember_grid"
        onClickTopics="association"
        button="true"
      >
        <img src="images/arrow_right.png" alt="unassociate" class="association-arrow" />
      </sj:a>
      <br /><br />
      <s:form id="save_vlan_associated_vlanMember_grid_form" theme="simple">
        <s:url var="associate_vlan_with_vlanMembers_url" action="associate-vlan-with-vlan-members" escapeAmp="false">
          <s:param name="vlan_id" value="%{#parameters.vlan_id}" />
        </s:url>
        <div class="type-hidden">
          <s:hidden id="vlan_associated_vlanMember_grid_id_list" name="idList" value="undefined" />
        </div>
        <sj:submit
          id="save_vlan_associated_vlanMember_grid"
          href="%{associate_vlan_with_vlanMembers_url}"
          targets="shared_dialog_box"
          replaceTarget="false"
          validate="false"
          onErrorTopics="save_vlan_associated_vlanMember_grid_error"
          button="true"
          indicator="associate_vlan_with_vlanMembers_indicator"
          value="%{getText('associateAction.save.button.value')}"
        />
        <br />
        <img id="associate_vlan_with_vlanMembers_indicator" src="images/loading_small.gif" alt="Loading..." style="display: none;" />
      </s:form>
    </div>
  </div>

  <s:url var="vlan_unassociated_vlanMember_grid_box_url" action="vlan-unassociated-vlan-member-grid-box"  escapeAmp="false">
    <s:param name="node_id" value="%{#parameters.node_id}" />
    <s:param name="vlan_id" value="%{#parameters.vlan_id}" />
    <s:param name="vlan_name" value="%{#parameters.vlan_name}" />
  </s:url>
  <sj:div
    id="associate_vlan_with_vlanMembers_right_column"
    href="%{vlan_unassociated_vlanMember_grid_box_url}"
    indicator="associate_vlan_with_vlanMembers_right_column_indicator"
    cssClass="association-right-column"
  >
    <img
      id="associate_vlan_with_vlanMembers_right_column_indicator"
      src="images/loading_middle.gif"
      alt="Loading..."
      style="display: none;"
      class="association-grid-indicator"
    />
  </sj:div>

  <sj:dialog
    id="save_vlan_associated_vlanMember_grid_error_dialog"
    openTopics="save_vlan_associated_vlanMember_grid_error"
    showEffect="scale"
    hideEffect="puff"
    autoOpen="false"
    modal="true"
    title="%{getText('associateAction.error.dialog.title')}"
    buttons="{
      'OK': function() {$('#save_vlan_associated_vlanMember_grid_error_dialog').dialog('close'); }
    }"
    dialogClass="dialog"
  >
    <s:text name="associateAction.error.dialog.text" />
  </sj:dialog>
</div>
