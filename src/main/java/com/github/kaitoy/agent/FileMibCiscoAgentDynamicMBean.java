/*_##########################################################################
  _##
  _##  Copyright (C) 2011-2012  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.agent;

import java.util.Iterator;
import java.util.List;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import mx4j.AbstractDynamicMBean;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;

public class FileMibCiscoAgentDynamicMBean extends AbstractDynamicMBean {

  private final String mbeanClassName;
  private final String moClassName;

  public FileMibCiscoAgentDynamicMBean(FileMibCiscoAgent mo) {
    this.mbeanClassName = this.getClass().getName();
    this.moClassName = mo.getClass().getName();
    setResource(mo);
  }

  protected String getMBeanDescription() {
    return "DynamicMBean of " + moClassName;
  }

  protected String getMBeanClassName() {
    return mbeanClassName;
  }

  public static List<MBeanAttributeInfo> getMBeanAttributeInfoList() {
    List<MBeanAttributeInfo> maiList
      = FileMibAgentDynamicMBean.getMBeanAttributeInfoList();

    maiList.add(
      new MBeanAttributeInfo(
        "CommunityStringIndexes",
        List.class.getName(),
        "The list of community string index this agent has.",
        true,
        false,
        false
      )
    );

    return maiList;
  }

  protected MBeanAttributeInfo[] createMBeanAttributeInfo() {
    return getMBeanAttributeInfoList().toArray(new MBeanAttributeInfo[0]);
  }

  public static List<MBeanOperationInfo> getMBeanOperationInfoList() {
    List<MBeanOperationInfo> moiList
      = FileMibAgentDynamicMBean.getMBeanOperationInfoList();

    for (Iterator<MBeanOperationInfo> it = moiList.iterator(); it.hasNext();) {
      MBeanOperationInfo moi = it.next();
      if (
           moi.getName().equals("getMib")
        || moi.getName().equals("walkMib")
        || moi.getName().equals("setMib")
      ) {
        it.remove();
      }
    }

    moiList.add(
      new MBeanOperationInfo(
        "getMib",
        "Get value by specified oid.",
        new MBeanParameterInfo[] {
          new MBeanParameterInfo(
            "oid",
            String.class.getName(),
            "The numeric OID with instance number of target."
          ),
          new MBeanParameterInfo(
            "communityStringIndex",
            String.class.getName(),
            "Community string index"
          ),
        },
        VariableBinding.class.getName(),
        MBeanOperationInfo.INFO
      )
    );
    moiList.add(
      new MBeanOperationInfo(
        "walkMib",
        "Get values by specified oid for specified count.",
        new MBeanParameterInfo[] {
          new MBeanParameterInfo(
            "oid",
            String.class.getName(),
            "The numeric OID that specifies start point to search subtrees."
          ),
          new MBeanParameterInfo(
            "communityStringIndex",
            String.class.getName(),
            "Community string index"
          ),
          new MBeanParameterInfo(
            "count",
            int.class.getName(),
            "The number of MIB instances to get."
          ),

        },
        List.class.getName(),
        MBeanOperationInfo.INFO
      )
    );
    moiList.add(
      new MBeanOperationInfo(
        "setMib",
        "Set specified variable-binding.",
        new MBeanParameterInfo[] {
          new MBeanParameterInfo(
            "varBind",
            String.class.getName(),
            "The variable-binding to set in following format(same as fileMIB's default one):  "
              + "<OID>:<TYPE>:<VALUE>"
          ),
          new MBeanParameterInfo(
            "communityStringIndex",
            String.class.getName(),
            "Community string index"
          ),
        },
        Variable.class.getName(),
        MBeanOperationInfo.ACTION
      )
    );

    return moiList;
  }

  protected MBeanOperationInfo[] createMBeanOperationInfo() {
    return getMBeanOperationInfoList().toArray(new MBeanOperationInfo[0]);
  }

}
