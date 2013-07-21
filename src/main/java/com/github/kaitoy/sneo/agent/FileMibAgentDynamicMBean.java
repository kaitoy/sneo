/*_##########################################################################
  _##
  _##  Copyright (C) 2011-2012  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.agent;

import java.util.ArrayList;
import java.util.List;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import mx4j.AbstractDynamicMBean;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;

public class FileMibAgentDynamicMBean extends AbstractDynamicMBean {

  private final String mbeanClassName;
  private final String moClassName;

  public FileMibAgentDynamicMBean(FileMibAgent mo) {
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
    List<MBeanAttributeInfo> maiList = new ArrayList<MBeanAttributeInfo>();

    maiList.add(
      new MBeanAttributeInfo(
        "Address",
        String.class.getName(),
        "The IP address and port of this agent.",
        true,
        false,
        false
      )
    );
    maiList.add(
      new MBeanAttributeInfo(
        "CommunityName",
        String.class.getName(),
        "The community name of this agent.",
        true,
        false,
        false
      )
    );
    maiList.add(
      new MBeanAttributeInfo(
        "SecurityName",
        String.class.getName(),
        "The security name of this agent.",
        true,
        false,
        false
      )
    );
    maiList.add(
      new MBeanAttributeInfo(
        "TrapTarget",
        String.class.getName(),
        "The target IP address to send trap . format: <IP_address>/<port_number>",
        true,
        false,
        false
      )
    );
    maiList.add(
      new MBeanAttributeInfo(
        "FormatName",
        String.class.getName(),
        "The format of fileMIB this agent has loaded.",
        true,
        false,
        false
      )
    );
    maiList.add(
      new MBeanAttributeInfo(
        "FileMibPath",
        String.class.getName(),
        "The path of a fileMIB.",
        true,
        true,
        false
      )
    );
    maiList.add(
      new MBeanAttributeInfo(
        "Running",
        boolean.class.getName(),
        "The state of this SNMP agent.",
        true,
        false,
        true
      )
    );
    maiList.add(
      new MBeanAttributeInfo(
        "GatheringSnmpAccessStatistics",
        boolean.class.getName(),
        "The state of SNMP access statistics gatherer.",
        true,
        true,
        true
      )
    );

    return maiList;
  }

  protected MBeanAttributeInfo[] createMBeanAttributeInfo() {
    return getMBeanAttributeInfoList().toArray(new MBeanAttributeInfo[0]);
  }

  public static List<MBeanOperationInfo> getMBeanOperationInfoList() {
    List<MBeanOperationInfo> moiList = new ArrayList<MBeanOperationInfo>();

    moiList.add(
      new MBeanOperationInfo(
        "start",
        "Start this SNMP agent.",
        new MBeanParameterInfo[0],
        Void.class.getName(),
        MBeanOperationInfo.ACTION
      )
    );
    moiList.add(
      new MBeanOperationInfo(
        "stop",
        "Stop this SNMP agent.",
        new MBeanParameterInfo[0],
        Void.class.getName(),
        MBeanOperationInfo.ACTION
      )
    );
    moiList.add(
      new MBeanOperationInfo(
        "reloadFileMib",
        "Reload fileMIB(s).",
        new MBeanParameterInfo[0],
        Void.class.getName(),
        MBeanOperationInfo.ACTION
      )
    );
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
            "count",
            int.class.getName(),
            "The number of MIB instances to get"
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
            "The variable-binding to set in following format(same as fileMIB's):  "
              + "<OID>:<TYPE>:<VALUE>"
          ),
        },
        Variable.class.getName(),
        MBeanOperationInfo.ACTION
      )
    );
    moiList.add(
      new MBeanOperationInfo(
        "removeMessageProcessor",
        "Remove a Message Processor",
        new MBeanParameterInfo[] {
          new MBeanParameterInfo(
            "version",
            String.class.getName(),
            "1 or 2c "
          ),
        },
        String.class.getName(),
        MBeanOperationInfo.INFO
      )
    );
    moiList.add(
      new MBeanOperationInfo(
        "reportSnmpAccessStatistics",
        "Report SNMP access statistics.",
        new MBeanParameterInfo[0],
        String.class.getName(),
        MBeanOperationInfo.INFO
      )
    );
    return moiList;
  }

  protected MBeanOperationInfo[] createMBeanOperationInfo() {
    return getMBeanOperationInfoList().toArray(new MBeanOperationInfo[0]);
  }

}
