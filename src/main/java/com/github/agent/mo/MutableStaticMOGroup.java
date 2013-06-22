/*_##########################################################################
  _##
  _##  Copyright (C) 2011-2012  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.agent.mo;

import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Null;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.agent.DefaultMOScope;
import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.agent.MOGroup;
import org.snmp4j.agent.MOScope;
import org.snmp4j.agent.MOServer;
import org.snmp4j.agent.ManagedObject;
import org.snmp4j.agent.request.RequestStatus;
import org.snmp4j.agent.request.SubRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * The <code>StaticMutableMOGroup</code> can be used to easily implement static
 * (writable) managed objects.
 * <p>
 * Note: Dynamic variables (see {@link Variable#isDynamic}) cannot be used to
 * when using default {@link VariableBinding}s since {@link Variable}s are
 * cloned when added to them. In order to use dynamic objects (i.e., objects
 * that may change their value when being accessed), a sub-class of
 * {@link VariableBinding} needs to be used that overwrites its
 * {@link VariableBinding#setVariable} method.
 * </p>
 */
public class MutableStaticMOGroup implements ManagedObject, MOGroup {

//  private static final LogAdapter logger
//    = LogFactory.getLogger(MutableStaticMOGroup.class);

  private final OID root;
  private final MOScope scope;
  private final SortedMap<OID, Variable> variableBindings;
  private final Map<OID, VariableServer> varServerRegistry
    = new HashMap<OID, VariableServer>();
  private final SnmpAccessStatisticsGatherer snmpAccessStatisticsGatherer
    = new SnmpAccessStatisticsGathererImpl();
  private boolean enableSnmpAccessStatisticsGatherer = false;

  /**
   * Creates a static managed object group for the sub-tree with the specified
   * root OID.
   * @param root
   *    the root OID of the sub-tree to be registered by this managed object.
   * @param vbs
   *    the variable bindings to be returned in this sub-tree.
   */
  public MutableStaticMOGroup(OID root, VariableBinding[] vbs) {
    this.root = root;
    this.scope = new DefaultMOScope(root, true, root.nextPeer(), false);
    variableBindings = new TreeMap<OID, Variable>();
    for (int i = 0; i < vbs.length; i++) {
      if ((vbs[i].getOid() != null) && (vbs[i].getVariable() != null)) {
        if (
             (vbs[i].getOid().size() >= root.size())
          && (vbs[i].getOid().leftMostCompare(root.size(), root) == 0)
        ) {
          this.variableBindings.put(vbs[i].getOid(), vbs[i].getVariable());
        }
      }
    }
  }

  private MutableStaticMOGroup(
      OID root, SortedMap<OID, Variable> variableBindings,
      MOScope scope
    ) {
    this.root = root;
    this.variableBindings = variableBindings;
    this.scope = scope;
  }

  public OID getRoot() {
    return root;
  }

  public MOScope getScope() {
    return scope;
  }

  public
  SnmpAccessStatisticsGatherer getSnmpAccessStatisticsGatherer() {
    return snmpAccessStatisticsGatherer;
  }

  public void setEnableSnmpAccessStatisticsGatherer(
    boolean enableSnmpAccessStatisticsGatherer
  ) {
    this.enableSnmpAccessStatisticsGatherer
      = enableSnmpAccessStatisticsGatherer;

    if (!enableSnmpAccessStatisticsGatherer) {
      snmpAccessStatisticsGatherer.clear();
    }
  }

  public boolean isEnableSnmpAccessStatisticsGatherer() {
    return enableSnmpAccessStatisticsGatherer;
  }

  public void put(OID oid, Variable var) {
    synchronized (variableBindings) {
      variableBindings.put(oid, var);
    }
  }

  public void putAll(Map<OID, Variable> vbs) {
    synchronized (variableBindings) {
      variableBindings.putAll(vbs);
    }
  }

  public void putAll(List<VariableBinding> vbs) {
    Map<OID, Variable> vbsMap = new HashMap<OID, Variable>();
    for (VariableBinding vb: vbs) {
      vbsMap.put(vb.getOid(), vb.getVariable());
    }

    synchronized (variableBindings) {
      variableBindings.putAll(vbsMap);
    }
  }

  public void registerMOs(
    MOServer server, OctetString context
  ) throws DuplicateRegistrationException {
    server.register(this, context);
  }

  public void unregisterMOs(MOServer server, OctetString context) {
    server.unregister(this, context);
  }

  public void registerVariableServer(OID oid, VariableServer vs) {
    synchronized (varServerRegistry) {
      varServerRegistry.put(oid, vs);
    }
  }

  public int size() {
    synchronized (variableBindings) {
      return variableBindings.size();
    }
  }

  public OID find(MOScope range) {
    OID targetOid = range.getLowerBound();

    SortedMap<OID, Variable> tail;
    if (range.isLowerIncluded()) {
      synchronized (variableBindings) {
        tail = variableBindings.tailMap(targetOid);
      }
    }
    else {
      synchronized (variableBindings) {
        tail = variableBindings.tailMap(targetOid.successor());
      }
    }

    if (tail.size() == 0) {
      return null;
    }

    return tail.firstKey();
  }

  public void get(SubRequest request) {
    OID oid = request.getVariableBinding().getOid();

    Variable var = get(oid);
    if (var == null) {
      request.getVariableBinding().setVariable(Null.noSuchInstance);
      request.completed();

      if (enableSnmpAccessStatisticsGatherer) {
        snmpAccessStatisticsGatherer
          .snmpGetFailed(request.getRequest().getContext(), oid);
      }

      return;
    }
    request.getVariableBinding().setVariable(var);
    request.completed();

    if (enableSnmpAccessStatisticsGatherer) {
      snmpAccessStatisticsGatherer
        .snmpGetSucceeded(request.getRequest().getContext(), oid);
    }
  }

  public boolean next(SubRequest request) {
    MOScope scope = request.getQuery().getScope();
    OID requestedOid = scope.getLowerBound();
    OID nextOid = find(scope);

    if (nextOid == null) {
      request.getVariableBinding().setVariable(Null.noSuchInstance);
      request.completed();

      if (enableSnmpAccessStatisticsGatherer) {
        snmpAccessStatisticsGatherer
          .snmpNextFailed(request.getRequest().getContext(), requestedOid);
      }

      return false;
    }

    request.getVariableBinding().setOid(nextOid);
    request.getVariableBinding().setVariable(get(nextOid));
    request.completed();

    if (enableSnmpAccessStatisticsGatherer) {
      snmpAccessStatisticsGatherer
        .snmpNextSucceeded(request.getRequest().getContext(), requestedOid);
    }

    return true;
  }

  public Variable get(OID oid) {
    synchronized (varServerRegistry) {
      if (varServerRegistry.containsKey(oid)) {
        return varServerRegistry.get(oid).get();
      }
    }
    synchronized (variableBindings) {
      return variableBindings.get(oid);
    }
  }

  private VariableBinding next(OID oid) {
    SortedMap<OID, Variable> tail;
    synchronized (variableBindings) {
      tail = variableBindings.tailMap(oid.successor());
    }

    if (tail.size() == 0) {
      return null;
    }

    OID first = tail.firstKey();
    return new VariableBinding(first, get(first));
  }

  public List<VariableBinding> walk(OID oid, int count) {
    List<VariableBinding> varbinds = new ArrayList<VariableBinding>();

    OID nextTarget = oid;
    for (int i = 0; i < count; i++) {
      VariableBinding vb = next(nextTarget);

      if (vb == null || !vb.getOid().startsWith(oid)) {
        return varbinds;
      }

      varbinds.add(vb);
      nextTarget = vb.getOid();
    }

    return varbinds;
  }

  public Variable set(OID oid, Variable var) {
    synchronized (variableBindings) {
      return variableBindings.put(oid, var);
    }
  }

  /**
   * Checks whether the new value contained in the supplied sub-request is a
   * valid value for this object. The checks are performed by firing a
   * {@link MOValueValidationEvent} the registered listeners.
   *
   * @param request
   *    the <code>SubRequest</code> with the new value.
   * @return
   *    {@link SnmpConstants#SNMP_ERROR_SUCCESS} if the new value is OK,
   *    any other appropriate SNMPv2/v3 error status if not.
   */
//  private int isValueOK(SubRequest request) {
//    Variable var = request.getVariableBinding().getVariable();
//
//    switch (var.getSyntax()) {
//      case SMIConstants.SYNTAX_INTEGER : // == INTEGER32
//        break;
//      case SMIConstants.SYNTAX_OCTET_STRING :
//        break;
//      case SMIConstants.SYNTAX_OBJECT_IDENTIFIER :
//        break;
//      case SMIConstants.SYNTAX_IPADDRESS :
//        break;
//      case SMIConstants.SYNTAX_COUNTER32 : // == GAUGE32
//      case SMIConstants.SYNTAX_UNSIGNED_INTEGER32 :
//      case SMIConstants.SYNTAX_TIMETICKS :
//        break;
//      case SMIConstants.SYNTAX_OPAQUE :
//        break;
//      case SMIConstants.SYNTAX_COUNTER64 :
//        break;
//      default :
//        return SnmpConstants.SNMP_ERROR_WRONG_TYPE;
//    }
//
//    return SnmpConstants.SNMP_ERROR_SUCCESS;
//  }

  /**
   * Prepare
   * @param request
   *    a request to process prepare SET request for.
   */
  public void prepare(SubRequest request) {
    RequestStatus status = request.getStatus();
    MOScope scp = request.getQuery().getScope();
    Variable variable = request.getVariableBinding().getVariable();
    OID oid = request.getVariableBinding().getOid();

    if (!scope.isCovered(scp)) {
      status.setErrorStatus(SnmpConstants.SNMP_ERROR_NO_CREATION);

      if (enableSnmpAccessStatisticsGatherer) {
        snmpAccessStatisticsGatherer.snmpSetFailed(request.getRequest().getContext(), oid);
      }

      return;
    }

    synchronized (variableBindings) {
      if (variableBindings.containsKey(oid)) {
        if (
          variable.getSyntax()
            != ((Variable)variableBindings.get(oid)).getSyntax()
        ) {
          status.setErrorStatus(SnmpConstants.SNMP_ERROR_WRONG_TYPE);

          if (enableSnmpAccessStatisticsGatherer) {
            snmpAccessStatisticsGatherer.snmpSetFailed(request.getRequest().getContext(), oid);
          }

          return;
        }

        //int valueOK = isValueOK(request);
        //status.setErrorStatus(valueOK);
        status.setErrorStatus(SnmpConstants.SNMP_ERROR_SUCCESS);

        request.completed();
      }
      else {
        status.setErrorStatus(SnmpConstants.SNMP_ERROR_SUCCESS);
        request.completed();
      }
    }
  }

  /**
   * commit
   * @param request
   *    a request to process commit SET request for.
   */
  public void commit(SubRequest request) {
    Variable variable = request.getVariableBinding().getVariable();
    OID oid = request.getVariableBinding().getOid();

    synchronized (variableBindings) {
      request.setUndoValue(variableBindings.get(oid));
      variableBindings.put(oid, variable);
    }

    if (enableSnmpAccessStatisticsGatherer) {
      snmpAccessStatisticsGatherer.snmpSetSucceeded(request.getRequest().getContext(), oid);
    }

    request.completed();
  }

  /**
   * undo
   * @param request
   *    a request to process undo SET request for.
   */
  public void undo(SubRequest request) {
    RequestStatus status = request.getStatus();
    if (
         (request.getUndoValue() != null)
      && (request.getUndoValue() instanceof Variable)
    ) {
      synchronized (variableBindings) {
        variableBindings.put(
          request.getVariableBinding().getOid(),
          (Variable)request.getUndoValue()
        );
      }

      status.setErrorStatus(SnmpConstants.SNMP_ERROR_SUCCESS);
      request.completed();
    }
    else {
      synchronized (variableBindings) {
        variableBindings.remove(request.getVariableBinding().getOid());
      }
      status.setErrorStatus(SnmpConstants.SNMP_ERROR_SUCCESS);
      request.completed();
    }
    // TODO mibAccessStatisticsもundo?
  }

  public void cleanup(SubRequest request) {
    request.setUndoValue(null);
    request.getStatus().setPhaseComplete(true);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append(this.getClass().getName());
    sb.append("[root=");
    sb.append(root);
    sb.append(",subtee count=");
    sb.append(variableBindings.size());
    sb.append("]");

    return sb.toString();
  }

  public MutableStaticMOGroup shallowCopy() {
    SortedMap<OID, Variable> vbsCopy
      = new TreeMap<OID, Variable>(variableBindings);
    // TODO should clone scope too.
    return new MutableStaticMOGroup(new OID(root), vbsCopy, scope);
  }

}
