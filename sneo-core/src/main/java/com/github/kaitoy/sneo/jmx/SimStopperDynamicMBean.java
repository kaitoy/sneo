/*_##########################################################################
  _##
  _##  Copyright (C) 2015  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.jmx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import mx4j.AbstractDynamicMBean;

public class SimStopperDynamicMBean extends AbstractDynamicMBean {

  private final String mbeanClassName;
  private final String moClassName;

  public SimStopperDynamicMBean(SimStopper mo) {
    this.mbeanClassName = this.getClass().getName();
    this.moClassName = mo.getClass().getName();
    setResource(mo);
  }

  @Override
  protected String getMBeanDescription() {
    return "DynamicMBean of " + moClassName;
  }

  @Override
  protected String getMBeanClassName() {
    return mbeanClassName;
  }

  public static List<MBeanAttributeInfo> getMBeanAttributeInfoList() {
    return Collections.emptyList();
  }

  @Override
  protected MBeanAttributeInfo[] createMBeanAttributeInfo() {
    return getMBeanAttributeInfoList().toArray(new MBeanAttributeInfo[0]);
  }

  public static List<MBeanOperationInfo> getMBeanOperationInfoList() {
    List<MBeanOperationInfo> moiList = new ArrayList<MBeanOperationInfo>();

    moiList.add(
      new MBeanOperationInfo(
        "stop",
        "Stop the simulation.",
        new MBeanParameterInfo[0],
        Void.class.getName(),
        MBeanOperationInfo.ACTION
      )
    );

    return moiList;
  }

  @Override
  protected MBeanOperationInfo[] createMBeanOperationInfo() {
    return getMBeanOperationInfoList().toArray(new MBeanOperationInfo[0]);
  }

}
