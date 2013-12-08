<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<table id="startSimulator_container">
  <tbody>
    <tr>
      <td class="start-button-cell">
          <s:url var="startSimulator_start_url" action="start-simulator" >
            <s:param name="simulation_id" value="%{#parameters.simulation_id}" />
          </s:url>
          <sj:a
            id="startSimulator_start_button"
            href="%{startSimulator_start_url}"
            targets="shared_dialog_box"
            replaceTarget="false"
            button="true"
            indicator="config_main_indicator"
          >
            <s:text name="startSimulator.start.button.label" />
          </sj:a>
      </td>
      <td class="stop-button-cell">
          <s:url var="startSimulator_stop_url" action="stop-simulator" >
            <s:param name="simulation_id" value="%{#parameters.simulation_id}" />
          </s:url>
          <sj:a
            id="startSimulator_stop_button"
            href="%{startSimulator_stop_url}"
            targets="shared_dialog_box"
            replaceTarget="false"
            button="true"
            indicator="config_main_indicator"
          >
            <s:text name="startSimulator.stop.button.label" />
          </sj:a>
      </td>
    </tr>
  </tbody>
</table>

