/*_##########################################################################
  _##
  _##  Copyright (C) 2012  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.network;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.ReflectionException;
import mx4j.AbstractDynamicMBean;
import com.github.kaitoy.sneo.agent.FileMibAgentDynamicMBean;

public class NodeDynamicMBean extends AbstractDynamicMBean {

  private final String mbeanClassName;
  private final String moClassName;
  private final Node mo;

  public NodeDynamicMBean(final Node mo) {
    this.mbeanClassName = this.getClass().getName();
    this.moClassName = mo.getClass().getName();
    this.mo = mo;
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

  private List<MBeanAttributeInfo> getMBeanAttributeInfoList() {
    List<MBeanAttributeInfo> maiList
      = new ArrayList<MBeanAttributeInfo>();

    for (
      MBeanAttributeInfo mai: FileMibAgentDynamicMBean
                                .getMBeanAttributeInfoList()
    ) {
      if (mai.getName().equals("Running")) {
        maiList.add(
          new MBeanAttributeInfo(
            "Running",
            boolean.class.getName(),
            "The state of this node.",
            true,
            false,
            true
          )
        );
        maiList.add(
          new MBeanAttributeInfo(
            "RunningAgent",
            boolean.class.getName(),
            "The state of this node's SNMP agent.",
            true,
            false,
            true
          )
        );
        maiList.add(
          new MBeanAttributeInfo(
            "ListeningIcmp",
            boolean.class.getName(),
            "The state of this node's ICMP.",
            true,
            false,
            true
          )
        );
      }
      else {
        maiList.add(mai);
      }
    }

    maiList.add(
      new MBeanAttributeInfo(
        "Ttl",
        int.class.getName(),
        "TTL",
        true,
        false,
        false
      )
    );

    return maiList;
  }

  @Override
  protected MBeanAttributeInfo[] createMBeanAttributeInfo() {
    return getMBeanAttributeInfoList().toArray(new MBeanAttributeInfo[0]);
  }

  private List<MBeanOperationInfo> getMBeanOperationInfoList() {
    List<MBeanOperationInfo> moiList = new ArrayList<MBeanOperationInfo>();

    for (
      MBeanOperationInfo moi: FileMibAgentDynamicMBean
                                .getMBeanOperationInfoList()
    ) {
      if (moi.getName().equals("start")) {
        moiList.add(
          new MBeanOperationInfo(
            "start",
            "Start this node.",
            new MBeanParameterInfo[0],
            Void.class.getName(),
            MBeanOperationInfo.ACTION
          )
        );
        moiList.add(
          new MBeanOperationInfo(
            "startAgent",
            "Start SNMP agent.",
            new MBeanParameterInfo[0],
            Void.class.getName(),
            MBeanOperationInfo.ACTION
          )
        );
        moiList.add(
          new MBeanOperationInfo(
            "startListeningIcmp",
            "Start listening ICMP.",
            new MBeanParameterInfo[0],
            Void.class.getName(),
            MBeanOperationInfo.ACTION
          )
        );
      }
      else if (moi.getName().equals("stop")) {
        moiList.add(
          new MBeanOperationInfo(
            "stop",
            "Stop this node.",
            new MBeanParameterInfo[0],
            Void.class.getName(),
            MBeanOperationInfo.ACTION
          )
        );
        moiList.add(
          new MBeanOperationInfo(
            "stopAgent",
            "Stop SNMP agent.",
            new MBeanParameterInfo[0],
            Void.class.getName(),
            MBeanOperationInfo.ACTION
          )
        );
        moiList.add(
          new MBeanOperationInfo(
            "stopListeningIcmp",
            "Stop listening ICMP.",
            new MBeanParameterInfo[0],
            Void.class.getName(),
            MBeanOperationInfo.ACTION
          )
        );
      }
      else {
        moiList.add(moi);
      }
    }

    moiList.add(
      new MBeanOperationInfo(
        "getRoutingTableEntries",
        "Get this node's routing table entries.",
        new MBeanParameterInfo[0],
        List.class.getName(),
        MBeanOperationInfo.INFO
      )
    );
    moiList.add(
      new MBeanOperationInfo(
        "getNifs",
        "Get this node's network interfaces.",
        new MBeanParameterInfo[0],
        List.class.getName(),
        MBeanOperationInfo.INFO
      )
    );

    return moiList;
  }

  @Override
  protected MBeanOperationInfo[] createMBeanOperationInfo() {
    return getMBeanOperationInfoList()
             .toArray(new MBeanOperationInfo[0]);
  }

  @Override
  public
  Object getAttribute(String arg0)
  throws AttributeNotFoundException, MBeanException, ReflectionException {
    if (arg0.equals("Address")) {
      return mo.getAgent().getAddress();
    }
    else if (arg0.equals("CommunityName")) {
      return mo.getAgent().getCommunityName();
    }
    else if (arg0.equals("FormatName")) {
      return mo.getAgent().getFormatName();
    }
    else if (arg0.equals("FileMibPath")) {
      return mo.getAgent().getFileMibPath();
    }
    else if (arg0.equals("GatheringSnmpAccessStatistics")) {
      return mo.getAgent().isGatheringSnmpAccessStatistics();
    }
    else if (arg0.equals("SecurityName")) {
      return mo.getAgent().getSecurityName();
    }
    else if (arg0.equals("TrapTarget")) {
      return mo.getAgent().getTrapTarget();
    }
    else {
      return super.getAttribute(arg0);
    }
  }

  @Override
  public AttributeList getAttributes(String[] arg0) {
    AttributeList attrs = new AttributeList();

    for (String attr: arg0) {
      try {
        attrs.add(new Attribute(attr, getAttribute(attr)));
      } catch (AttributeNotFoundException e) {
        throw new IllegalArgumentException(
                "Failed to get attribute: " + attr,
                e
              );
      } catch (MBeanException e) {
        throw new IllegalArgumentException(
                "Failed to get attribute: " + attr,
                e
              );
      } catch (ReflectionException e) {
        throw new IllegalArgumentException(
                "Failed to get attribute: " + attr,
                e
              );
      }
    }

    return attrs;
  }

  @Override
  public Object invoke(String arg0, Object[] arg1, String[] arg2)
  throws MBeanException, ReflectionException {
    if (arg0.equals("reloadFileMib")) {
      mo.getAgent().reloadFileMib();
      return "No result";
    }
    else if (arg0.equals("getMib")) {
      return mo.getAgent().getMib((String)arg1[0]);
    }
    else if (arg0.equals("walkMib")) {
      return mo.getAgent().walkMib((String)arg1[0], (Integer)arg1[1]);
    }
    else if (arg0.equals("setMib")) {
      try {
        return mo.getAgent().setMib((String)arg1[0]);
      } catch (ParseException e) {
        throw new IllegalArgumentException(e);
      }
    }
    else if (arg0.equals("removeMessageProcessor")) {
      return mo.getAgent().removeMessageProcessor((String)arg1[0]);
    }
    else if (arg0.equals("reportSnmpAccessStatistics")) {
      return mo.getAgent().reportSnmpAccessStatistics();
    }
    else if (arg0.equals("getNifs")) {
      return mo.getNifs();
    }
    else {
      return super.invoke(arg0, arg1, arg2);
    }
  }

  @Override
  public void setAttribute(Attribute arg0)
  throws
    AttributeNotFoundException,
    InvalidAttributeValueException,
    MBeanException,
    ReflectionException
  {
    if (arg0.getName().equals("FileMibPath")) {
      mo.getAgent().setFileMibPath((String)arg0.getValue());
    }
    else if (arg0.getName().equals("GatheringSnmpAccessStatistics")) {
      mo.getAgent().setGatheringSnmpAccessStatistics((Boolean)arg0.getValue());
    }
    else {
      super.setAttribute(arg0);
    }
  }

  @Override
  public AttributeList setAttributes(AttributeList arg0) {
    AttributeList attrs = new AttributeList();

    for (Object attr: arg0) {
      try {
        setAttribute((Attribute)attr);
        attrs.add(attr);
      } catch (AttributeNotFoundException e) {
        throw new IllegalArgumentException(
                "Failed to set attribute: " + attr,
                e
              );
      } catch (InvalidAttributeValueException e) {
        throw new IllegalArgumentException(
                "Failed to set attribute: " + attr,
                e
              );
      } catch (MBeanException e) {
        throw new IllegalArgumentException(
                "Failed to set attribute: " + attr,
                e
              );
      } catch (ReflectionException e) {
        throw new IllegalArgumentException(
                "Failed to set attribute: " + attr,
                e
              );
      }
    }

    return attrs;
  }

}
