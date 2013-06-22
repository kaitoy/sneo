/*_##########################################################################
  _##
  _##  Copyright (C) 2011  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.agent.mo;

import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;

public interface SnmpAccessStatisticsGatherer {

  public void snmpGetSucceeded(OctetString context, OID oid);
  public void snmpGetFailed(OctetString context, OID oid);
  public void snmpNextSucceeded(OctetString context, OID oid);
  public void snmpNextFailed(OctetString context, OID oid);
  public void snmpSetSucceeded(OctetString context, OID oid);
  public void snmpSetFailed(OctetString context, OID oid);

  public int getSnmpGetSucceededCount(OctetString context, OID oid);
  public int getSnmpGetFailedCount(OctetString context, OID oid);
  public int getSnmpNextSucceededCount(OctetString context, OID oid);
  public int getSnmpNextFailedCount(OctetString context, OID oid);
  public int getSnmpSetSucceededCount(OctetString context, OID oid);
  public int getSnmpSetFailedCount(OctetString context, OID oid);

  public void clear();

  public SnmpAccessStatisticsGatherer merge(SnmpAccessStatisticsGatherer other);

  public String getReport();

}
