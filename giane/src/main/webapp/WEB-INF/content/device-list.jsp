<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<table class="odd_even_table">
  <thead>
    <tr>
      <th><s:text name="device.id.label" /></th>
      <th><s:text name="device.name.label" /></th>
      <th><s:text name="device.description.label" /></th>
      <th><s:text name="device.ipAddresses.label" /></th>
    </tr>
  </thead>
  <tfoot>
    <tr>
        <td colspan="4" bgcolor="#CCCCCC" align="right">
          <s:property value="deviceList.size()" />
        </td>
    </tr>
  </tfoot>
  <tbody>
    <s:iterator var="device" value="deviceList" status="devStatus">
      <tr class="<s:if test="#devStatus.odd == true ">odd</s:if><s:else>even</s:else>">
        <td><s:property value="#devStatus.count" /></td>
        <td><s:property value="#device.name" /></td>
        <td><s:property value="#device.description" /></td>
        <td>
          <s:iterator var="address" value="#device.addresses" status="addrStatus">
            <s:if test="!#addrStatus.first">
              <br />
            </s:if>
            <s:property value="#address.address" />
          </s:iterator>
        </td>
      </tr>
    </s:iterator>
  </tbody>
</table>
