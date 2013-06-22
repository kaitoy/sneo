/*_##########################################################################
  _##
  _##  Copyright (C) 2011-2012  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.agent.mo;

import java.util.HashMap;
import java.util.Map;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import com.github.util.MutableInteger;

public
class SnmpAccessStatisticsGathererImpl
implements SnmpAccessStatisticsGatherer {

  private final Map<OidWithContext, MutableInteger> snmpGetSucceededCount
    = new HashMap<OidWithContext, MutableInteger>();
  private final Map<OidWithContext, MutableInteger> snmpGetFailedCount
    = new HashMap<OidWithContext, MutableInteger>();
  private final Map<OidWithContext, MutableInteger> snmpNextSucceededCount
    = new HashMap<OidWithContext, MutableInteger>();
  private final Map<OidWithContext, MutableInteger> snmpNextFailedCount
    = new HashMap<OidWithContext, MutableInteger>();
  private final Map<OidWithContext, MutableInteger> snmpSetSucceededCount
    = new HashMap<OidWithContext, MutableInteger>();
  private final Map<OidWithContext, MutableInteger> snmpSetFailedCount
    = new HashMap<OidWithContext, MutableInteger>();

  public Map<OidWithContext, MutableInteger> getSnmpGetSucceededCount() {
    synchronized (snmpGetSucceededCount) {
      return snmpGetSucceededCount;
    }
  }

  public Map<OidWithContext, MutableInteger> getSnmpGetFailedCount() {
    synchronized (snmpGetFailedCount) {
      return snmpGetFailedCount;
    }
  }

  public Map<OidWithContext, MutableInteger> getSnmpNextSucceededCount() {
    synchronized (snmpNextSucceededCount) {
      return snmpNextSucceededCount;
    }
  }

  public Map<OidWithContext, MutableInteger> getSnmpNextFailedCount() {
    synchronized (snmpNextFailedCount) {
      return snmpNextFailedCount;
    }
  }

  public Map<OidWithContext, MutableInteger> getSnmpSetSucceededCount() {
    synchronized (snmpSetSucceededCount) {
      return snmpSetSucceededCount;
    }
  }

  public Map<OidWithContext, MutableInteger> getSnmpSetFailedCount() {
    synchronized (snmpSetFailedCount) {
      return snmpSetFailedCount;
    }
  }

  public void snmpGetSucceeded(OctetString context, OID oid) {
    countUp(context, oid, snmpGetSucceededCount);
  }

  public void snmpGetFailed(OctetString context, OID oid) {
    countUp(context, oid, snmpGetFailedCount);
  }

  public void snmpNextSucceeded(OctetString context, OID oid) {
    countUp(context, oid, snmpNextSucceededCount);
  }

  public void snmpNextFailed(OctetString context, OID oid) {
    countUp(context, oid, snmpNextFailedCount);
  }

  public void snmpSetSucceeded(OctetString context, OID oid) {
    countUp(context, oid, snmpSetSucceededCount);
  }

  public void snmpSetFailed(OctetString context, OID oid) {
    countUp(context, oid, snmpSetFailedCount);
  }

  private void countUp(
    OctetString context, OID oid, Map<OidWithContext, MutableInteger> counter
  ) {
    OidWithContext key = new OidWithContext(context, oid);

    synchronized (counter) {
      if (counter.containsKey(key)) {
        counter.get(key).increment();
      }
      else {
        counter.put(key, new MutableInteger(1));
      }
    }
  }

  public int getSnmpGetSucceededCount(OctetString context, OID oid) {
    return getCount(context, oid, snmpGetSucceededCount);
  }

  public int getSnmpGetFailedCount(OctetString context, OID oid) {
    return getCount(context, oid, snmpGetFailedCount);
  }

  public int getSnmpNextSucceededCount(OctetString context, OID oid) {
    return getCount(context, oid, snmpNextSucceededCount);
  }

  public int getSnmpNextFailedCount(OctetString context, OID oid) {
    return getCount(context, oid, snmpNextFailedCount);
  }

  public int getSnmpSetSucceededCount(OctetString context, OID oid) {
    return getCount(context, oid, snmpSetSucceededCount);
  }

  public int getSnmpSetFailedCount(OctetString context, OID oid) {
    return getCount(context, oid, snmpSetFailedCount);
  }

  private int getCount(
    OctetString context, OID oid, Map<OidWithContext, MutableInteger> counter
  ) {
    synchronized (counter) {
      return counter.get(new OidWithContext(context, oid)).getValue();
    }
  }

  public void clear() {
    synchronized (snmpGetSucceededCount) {
      snmpGetSucceededCount.clear();
    }
    synchronized (snmpGetFailedCount) {
      snmpGetFailedCount.clear();
    }
    synchronized (snmpNextSucceededCount) {
      snmpNextSucceededCount.clear();
    }
    synchronized (snmpNextFailedCount) {
      snmpNextFailedCount.clear();
    }
    synchronized (snmpSetSucceededCount) {
      snmpSetSucceededCount.clear();
    }
    synchronized (snmpSetFailedCount) {
      snmpSetFailedCount.clear();
    }
  }

  public SnmpAccessStatisticsGatherer merge(
      SnmpAccessStatisticsGatherer other
    ) {
      if (!this.getClass().isInstance(other)) {
        throw new IllegalArgumentException(
                "Can't merge with " + other.getClass().getName()
              );
      }

      SnmpAccessStatisticsGathererImpl newOne
        = new SnmpAccessStatisticsGathererImpl();

      addCount(newOne.getSnmpGetSucceededCount(), getSnmpGetSucceededCount());
      addCount(newOne.getSnmpGetFailedCount(), getSnmpGetFailedCount());
      addCount(newOne.getSnmpNextSucceededCount(), getSnmpNextSucceededCount());
      addCount(newOne.getSnmpNextFailedCount(), getSnmpNextFailedCount());
      addCount(newOne.getSnmpSetSucceededCount(), getSnmpSetSucceededCount());
      addCount(newOne.getSnmpSetFailedCount(), getSnmpSetFailedCount());

      SnmpAccessStatisticsGathererImpl impl
        = (SnmpAccessStatisticsGathererImpl)other;

      addCount(newOne.getSnmpGetSucceededCount(), impl.getSnmpGetSucceededCount());
      addCount(newOne.getSnmpGetFailedCount(), impl.getSnmpGetFailedCount());
      addCount(newOne.getSnmpNextSucceededCount(), impl.getSnmpNextSucceededCount());
      addCount(newOne.getSnmpNextFailedCount(), impl.getSnmpNextFailedCount());
      addCount(newOne.getSnmpSetSucceededCount(), impl.getSnmpSetSucceededCount());
      addCount(newOne.getSnmpSetFailedCount(), impl.getSnmpSetFailedCount());

      return newOne;
    }

    private void addCount(
      Map<OidWithContext, MutableInteger> dstCounter,
      Map<OidWithContext, MutableInteger> srcCounter
    ) {
      for (OidWithContext key: srcCounter.keySet()) {
        if (dstCounter.containsKey(key)) {
          dstCounter.get(key).add(srcCounter.get(key).getValue());
        }
        else {
          dstCounter.put(
            key, new MutableInteger(srcCounter.get(key).getValue())
          ); // TODO key もnewする。
        }
      }
    }

  public String getReport() {
    return toString();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append(counterToString(snmpGetSucceededCount, "snmpGetSucceededCount"));
    sb.append(counterToString(snmpGetFailedCount, "snmpGetFailedCount"));
    sb.append(counterToString(snmpNextSucceededCount, "snmpNextSucceededCount"));
    sb.append(counterToString(snmpNextFailedCount, "snmpNextFailedCount"));
    sb.append(counterToString(snmpSetSucceededCount, "snmpSetSucceededCount"));
    sb.append(counterToString(snmpSetFailedCount, "snmpSetFailedCount"));

    return sb.toString();
  }

  private String counterToString(
    Map<OidWithContext, MutableInteger> counter, String counterName
  ) {
    StringBuilder sb = new StringBuilder();

    sb.append("[");
    sb.append(counterName);
    sb.append("]\n");

    synchronized (counter) {
      for (OidWithContext key: counter.keySet()) {
        sb.append(key);
        sb.append("=");
        sb.append(counter.get(key));
        sb.append("\n");
      }
    }

    return sb.toString();
  }

  private class OidWithContext { // TODO 不変に(防御的コピー)

    private final OctetString context;
    private final OID oid;


    public OidWithContext(OctetString context, OID oid) {
      this.context = context;
      this.oid = oid;
    }

    public OctetString getContext() {
      return context;
    }

    public OID getOid() {
      return oid;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == this) { return true; }
      if (!this.getClass().isInstance(obj)) { return false; }

      OidWithContext other = (OidWithContext)obj;
      return    oid.equals(other.getOid())
             && context.equals(other.getContext());
    }

    @Override
    public int hashCode() {
      return toString().hashCode();
    }

    @Override
    public String toString() {
      return context.toString() + oid.toString();
    }

  }

}
