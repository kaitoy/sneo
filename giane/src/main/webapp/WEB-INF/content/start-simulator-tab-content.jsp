<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div class="giane-tab-content-form-column">
  <div>
    <s:form id="startSimulator_form" theme="simple" cssClass="giane-form">
      <fieldset>
        <legend><s:text name="simulation.start.form" /></legend>
        <div>
          <s:hidden id="startSimulator_id" name="simulationId" />
        </div>
        <div>
          <s:textfield
            id="startSimulator_name"
            name="name"
            label="%{getText('simulation.name.label')}"
            theme="xhtml"
            disabled="true"
            cssClass="giane-disabled-field"
          />
        </div>
        <div>
          <s:textfield
            id="startSimulator_network"
            name="name"
            label="%{getText('simulation.network.label')}"
            theme="xhtml"
            disabled="true"
            cssClass="giane-disabled-field"
          />
        </div>
        <div>
          <s:textarea
            id="startSimulator_descr"
            name="descr"
            label="%{getText('simulation.descr.label')}"
            cols="30"
            resizable="false"
            theme="xhtml"
            disabled="true"
            cssClass="giane-disabled-field"
          />
        </div>
        <div>
          <table class="submit-button-table">
            <tbody>
              <tr>
                <td class="two-buttons-first-cell">
                  <sj:submit
                    value="%{getText('form.startButton.label')}"
                    button="true"
                    cssClass="giane-form-button"
                    onClickTopics="startButtonClicked"
                  />
                  <s:url var="startSimulator_url" action="start-simulator" />
                  <sj:submit
                    listenTopics="doStart_startSimulator"
                    href="%{startSimulator_url}"
                    targets="shared_dialog_box"
                    replaceTarget="false"
                    indicator="startSimulator_indicator"
                    onSuccessTopics="runningSimulatorUpdated"
                    onErrorTopics="startError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                </td>
                <td class="two-buttons-left-indicator-cell">
                  <img id="startSimulator_indicator" src="images/loading_small.gif" alt="Loading..." style="display: none;" />
                </td>
                <td class="two-buttons-second-cell">
                  <sj:submit
                    value="%{getText('form.stopButton.label')}"
                    button="true"
                    cssClass="giane-form-button"
                    onClickTopics="stopButtonClicked"
                  />
                  <s:url var="stopSimulator_url" action="stop-simulator" />
                  <sj:submit
                    listenTopics="doStop_startSimulator"
                    href="%{stopSimulator_url}"
                    targets="shared_dialog_box"
                    replaceTarget="false"
                    indicator="stopimulator_indicator"
                    onSuccessTopics="runningSimulatorUpdated"
                    onErrorTopics="stopError"
                    clearForm="true"
                    cssStyle="display: none;"
                  />
                  <img id="stopSimulator_indicator" src="images/loading_small.gif" alt="Loading..." style="display: none;" />
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </fieldset>
    </s:form>
  </div>
</div>

<s:url var="startSimulator_grid_box_url" action="simulation-grid-box" escapeAmp="false">
  <s:param name="grid_id" value="'startSimulator_grid'" />
  <s:param name="navigatorDelete" value="false" />
  <s:param name="reloadTopics" value="'runningSimulatorUpdated'" />
  <s:param name="hide_running" value="false" />
</s:url>
<div class="giane-tab-content-grid-column">
  <sj:div href="%{startSimulator_grid_box_url}" indicator="startSimulator_grid_box_indicator" cssClass="giane-grid-box" />
  <img id="startSimulator_grid_box_indicator" src="images/loading_small.gif" alt="Loading..." style="display: none;" />
</div>
