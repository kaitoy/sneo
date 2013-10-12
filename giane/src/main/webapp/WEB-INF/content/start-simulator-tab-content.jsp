<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<table id="startSimulator_container">
  <tbody>
    <tr>
      <td style="text-align:right;">
          <s:url var="startSimulator_start_url" action="start-simulator" >
            <s:param name="simulation_id" value="%{#parameters.simulation_id}" />
          </s:url>
          <sj:a
            id="startSimulator_start_button"
            href="%{startSimulator_start_url}"
            targets="startSimulator_dialog"
            replaceTarget="false"
            onSuccessTopics="startSimulator_start_success"
            onErrorTopics="startSimulator_start_error"
            button="true"
            indicator="main_indicator"
          >
            <s:text name="startSimulator.start.button.label" />
          </sj:a>
      </td>
      <td style="text-align:left;">
        <!--
        <sj:div
          id="startSimulator_stop_wrapper"
          events="click"
          effect="bounce"
          effectMode="effect"
          effectOptions="{ times:3 }"
          effectDuration="300"
        >
         -->
          <s:url var="startSimulator_stop_url" action="stop-simulator" >
            <s:param name="simulation_id" value="%{#parameters.simulation_id}" />
          </s:url>
          <sj:a
            id="startSimulator_stop_button"
            href="%{startSimulator_stop_url}"
            targets="startSimulator_dialog"
            replaceTarget="false"
            onSuccessTopics="startSimulator_stop_success"
            onErrorTopics="startSimulator_stop_error"
            button="true"
            indicator="main_indicator"
          >
            <s:text name="startSimulator.stop.button.label" />
          </sj:a>
        <!-- </sj:div> -->
      </td>
    </tr>
  </tbody>
</table>

<s:if test="%{#parameters.running}">
  <script>$.publish("startSimulator_start_success");</script>
</s:if>
<s:else>
  <script>$.publish("startSimulator_stop_success");</script>
</s:else>

<div id="startSimulator_dialog"></div>

<sj:dialog
  id="startSimulator_start_error_dialog"
  openTopics="startSimulator_start_error"
  showEffect="scale"
  hideEffect="puff"
  autoOpen="false"
  modal="true"
  title="%{getText('startSimulator.start.error.dialog.title')}"
  dialogClass="dialog"
>
  <s:text name="startSimulator.start.error.dialog.text" />
</sj:dialog>

<sj:dialog
  id="startSimulator_stop_error_dialog"
  openTopics="startSimulator_stop_error"
  showEffect="scale"
  hideEffect="puff"
  autoOpen="false"
  modal="true"
  title="%{getText('startSimulator.stop.error.dialog.title')}"
  dialogClass="dialog"
>
  <s:text name="startSimulator.stop.error.dialog.text" />
</sj:dialog>
