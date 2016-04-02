/*_##########################################################################
  _##
  _##  Copyright (C) 2011-2013  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.agent;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import org.snmp4j.agent.DefaultMOContextScope;
import org.snmp4j.agent.DefaultMOQuery;
import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.agent.MOGroup;
import org.snmp4j.agent.ManagedObject;
import org.snmp4j.agent.mo.MOTableRow;
import org.snmp4j.agent.mo.snmp.RowStatus;
import org.snmp4j.agent.mo.snmp.SnmpCommunityMIB;
import org.snmp4j.agent.mo.snmp.StorageType;
import org.snmp4j.agent.mo.snmp.VacmMIB;
import org.snmp4j.agent.security.MutableVACM;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.log.LogFactory;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModel;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.Null;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import com.github.kaitoy.sneo.agent.mo.MutableStaticMOGroup;
import com.github.kaitoy.sneo.util.ColonSeparatedOidTypeValueVariableTextFormat;
import com.github.kaitoy.sneo.util.SneoVariableTextFormat;

public class FileMibCiscoAgent extends FileMibAgent {

  private static final LogAdapter logger
    = LogFactory.getLogger(FileMibCiscoAgent.class);
  private static final OID[] COMMUNITY_STRING_INDEXED_MIB_MODULE_ROOTS
    = AgentPropertiesLoader.getInstance().communityStringIndexedMibModuleRoots();

  private List<String> communityStringIndexes;
  private final String communityStringIndexDelimiter;

  private FileMibCiscoAgent(Builder b) {
    super(b);
    this.communityStringIndexes = b.communityStringIndexes;
    this.communityStringIndexDelimiter = b.communityStringIndexDelimiter;
  }

  public void setCommunityStringIndexes(
    List<String> communityStringIndexes
   ) {
    this.communityStringIndexes = communityStringIndexes;
  }

  public List<String> getCommunityStringIndexes() {
    return communityStringIndexes;
  }

  private String getContextNameForCommunityStringIndex(
    String communityStringIndex
  ) {
    if (communityStringIndex.length() == 0) {
      return getCommunityName();
    }
    else {
      StringBuilder sb = new StringBuilder();
      sb.append(getCommunityName())
        .append(communityStringIndexDelimiter)
        .append(communityStringIndex);
      return sb.toString();
    }
  }

  private String getFileMibPathForCommunityStringIndex(
    String communityStringIndex
  ) {
    if (communityStringIndex.length() == 0) {
      return getFileMibPath();
    }
    else {
      StringBuilder sb = new StringBuilder();
      sb.append(getFileMibPath())
        .append(communityStringIndexDelimiter)
        .append(communityStringIndex);
      return sb.toString();
    }
  }

  /**
   * Adds community to security name mappings needed for SNMPv1 and SNMPv2c.
   *
   * @param communityMIB the SnmpCommunityMIB holding coexistence
   *   configuration for community based security models.
   */
  @Override
  protected void addCommunities(SnmpCommunityMIB communityMIB) {
    super.addCommunities(communityMIB);

    if (communityStringIndexes == null || communityStringIndexes.size() == 0) {
      logger.info(
        "No community string index. Add no community name to snmpCommunityMIB."
      );
      return;
    }

    for (String communityStringIndex: communityStringIndexes) {
      String communityName
        = getContextNameForCommunityStringIndex(communityStringIndex);

      Variable[] com2sec = new Variable[] {
        new OctetString(communityName),         // community name
        new OctetString("com" + communityName), // security name
        getLocalEngineID(),                     // local engine ID
        new OctetString(communityName),         // default context name
        new OctetString(),                      // transport tag
        new Integer32(StorageType.nonVolatile), // storage type
        new Integer32(RowStatus.active)         // row status
      };

      MOTableRow row
        = communityMIB.getSnmpCommunityEntry().createRow(
            new OctetString(
              communityName + "2com" + communityName
            ).toSubIndex(true),
            com2sec
          );

      communityMIB.getSnmpCommunityEntry().addRow(row);
    }
  }

  /**
   * Adds initial VACM configuration.
   *
   * @param vacm
   *    the VacmMIB holding the agent's view configuration.
   */
  @Override
  protected void addViews(VacmMIB vacm) {
    super.addViews(vacm);

    if (communityStringIndexes == null || communityStringIndexes.size() == 0) {
      logger.info("No community string index. Add no view to VACM-MIB.");
      return;
    }

    if (getCommunityName() == null) {
      return;
    }

    for (String communityStringIndex: communityStringIndexes) {
      String communityName
        = getContextNameForCommunityStringIndex(communityStringIndex);

      vacm.addGroup(
        SecurityModel.SECURITY_MODEL_SNMPv1,
        new OctetString("com" + communityName),
        new OctetString("v1v2group"),
        StorageType.nonVolatile
      );
      vacm.addGroup(
        SecurityModel.SECURITY_MODEL_SNMPv2c,
        new OctetString("com" + communityName),
        new OctetString("v1v2group"),
        StorageType.nonVolatile
      );

      vacm.addAccess(
        new OctetString("v1v2group"),
        new OctetString(communityName),
        SecurityModel.SECURITY_MODEL_SNMPv1,
        SecurityLevel.NOAUTH_NOPRIV,
        MutableVACM.VACM_MATCH_EXACT,
        new OctetString("fullView"),
        new OctetString("fullView"),
        new OctetString("fullView"),
        StorageType.nonVolatile
      );
      vacm.addAccess(
        new OctetString("v1v2group"),
        new OctetString(communityName),
        SecurityModel.SECURITY_MODEL_SNMPv2c,
        SecurityLevel.NOAUTH_NOPRIV,
        MutableVACM.VACM_MATCH_EXACT,
        new OctetString("fullView"),
        new OctetString("fullView"),
        new OctetString("fullView"),
        StorageType.nonVolatile
      );
    }
  }

  /**
   * Register additional managed objects at the agent's server.
   */
  @Override
  protected void registerManagedObjects() {
    super.registerManagedObjects();

    if (communityStringIndexes == null || communityStringIndexes.size() == 0) {
      logger.info("No community string index. Load no additional fileMib.");
      return;
    }

    MOGroup mog = getFileMibMoGroup();
    if (!(mog instanceof MutableStaticMOGroup)) {
      throw new AssertionError();
    }

    for (String communityStringIndex: communityStringIndexes) {
      try {
        String fileMibPath
          = getFileMibPathForCommunityStringIndex(communityStringIndex);
        MutableStaticMOGroup newMog
          = ((MutableStaticMOGroup)mog).shallowCopy();

        for (OID root: COMMUNITY_STRING_INDEXED_MIB_MODULE_ROOTS) {
          List<VariableBinding> vbs
            = getFileMIBLoader().load(fileMibPath, root);
          newMog.putAll(vbs);
        }

        OID rootOfNewMog = newMog.getRoot();
        OctetString contextName
          = new OctetString(
              getContextNameForCommunityStringIndex(communityStringIndex)
            );

        DefaultMOContextScope scope
          = new DefaultMOContextScope(
              contextName,
              rootOfNewMog,
              true,
              rootOfNewMog,
              false
            );

        ManagedObject mo = server.lookup(new DefaultMOQuery(scope, false));
        if (mo != null) {
          StringBuilder sb = new StringBuilder();
          sb.append("Could not register subtree '").append(rootOfNewMog)
            .append("' for a community string index ").append(communityStringIndex)
            .append(" because ManagedObject ").append(mo)
            .append(" is already registered");
          logger.warn(sb.toString());
        }
        else {
          doRegister(contextName, newMog);
          StringBuilder sb = new StringBuilder();
          sb.append("Registered managed objects for a community string index ")
            .append(communityStringIndex).append(". root: ")
            .append(rootOfNewMog).append(" subtee count: ").append(newMog.size());
          logger.info(sb.toString());
        }
      } catch (DuplicateRegistrationException e) {
        logger.error("Error while reading fileMib '"
            + getFileMibPathForCommunityStringIndex(communityStringIndex) + "':"
            + e.getMessage(), e);
      } catch (FileNotFoundException e) {
        logger.error(e);
      } catch (IOException e) {
        logger.error("Error while reading fileMib '"
          + getFileMibPathForCommunityStringIndex(communityStringIndex) + "':"
          + e.getMessage(), e);
      }
    }
  }



  @Override
  public void init() throws IOException {
    for (String communityStringIndex: communityStringIndexes) {
      getServer()
        .addContext(
           new OctetString(getContextNameForCommunityStringIndex(communityStringIndex))
         );
    }
    super.init();
  }

//  @Override
//  protected void addShutdownHook() {
//    Runtime.getRuntime().addShutdownHook(
//      new Thread() {
//        public void run() {
//          FileMibCiscoAgent.this.stop();
//          saveConfig();
//        }
//      }
//    );
//  }

  public VariableBinding getMib(String strOid, String communityStringIndex) {
    if (
         communityStringIndex.length() != 0
      && !communityStringIndexes.contains(communityStringIndex)
    ) {
      return null;
    }

    OID oid = new OID(strOid);
    DefaultMOContextScope scope =
      new DefaultMOContextScope(
        new OctetString(
          getContextNameForCommunityStringIndex(communityStringIndex)
        ),
        oid, true, oid.nextPeer(), false
      );

    ManagedObject mo = server.lookup(new DefaultMOQuery(scope, false));

    if (mo == null) {
      return null;
    }

    if (mo instanceof MutableStaticMOGroup) {
      return new VariableBinding(oid, ((MutableStaticMOGroup)mo).get(oid));
    }
    else {
      return null;
    }
  }

  public List<VariableBinding> walkMib(
    String strOid, String communityStringIndex, int count
  ) {
    if (
         communityStringIndex.length() != 0
      && !communityStringIndexes.contains(communityStringIndex)
    ) {
      return Collections.emptyList();
    }

    OID oid = new OID(strOid);

    DefaultMOContextScope scope
      = new DefaultMOContextScope(
          new OctetString(
            getContextNameForCommunityStringIndex(communityStringIndex)
          ),
          oid, true, oid.nextPeer(), false
        );

    ManagedObject mo = server.lookup(new DefaultMOQuery(scope, false));

    if (mo == null) {
      return Collections.emptyList();
    }

    if (mo instanceof MutableStaticMOGroup) {
      return ((MutableStaticMOGroup)mo).walk(oid, count);
    }
    else {
      return Collections.emptyList();
    }
  }

  public Variable setMib(
    String strVarBind, String communityStringIndex
  ) throws ParseException {
    VariableBinding varBind
      = ColonSeparatedOidTypeValueVariableTextFormat.getInstance()
          .parseVariableBinding(strVarBind);
    OID oid = varBind.getOid();

    DefaultMOContextScope scope
      =  new DefaultMOContextScope(
           new OctetString(
             getContextNameForCommunityStringIndex(communityStringIndex)
           ),
           oid, true, oid.nextPeer(), false
         );

    ManagedObject mo = server.lookup(new DefaultMOQuery(scope, true));
    if (mo == null) {
      return Null.noSuchObject;
    }

    if (!(mo instanceof MutableStaticMOGroup)) {
      return Null.noSuchObject;
    }

    return ((MutableStaticMOGroup)mo).set(
             oid,
             varBind.getVariable()
           );
  }

  public static class Builder extends FileMibAgent.Builder {

    private List<String> communityStringIndexes = null;
    private String communityStringIndexDelimiter = null;

    public Builder() {}

    @Override
    public Builder address(String address) {
      super.address(address);
      return this;
    }

    @Override
    public Builder bcConfigFilePath(String bcConfigFilePath) {
      super.bcConfigFilePath(bcConfigFilePath);
      return this;
    }

    @Override
    public Builder configFilePath(String configFilePath) {
      super.configFilePath(configFilePath);
      return this;
    }

    @Override
    public Builder communityName(String communityName) {
      super.communityName(communityName);
      return this;
    }

    @Override
    public Builder securityName(String securityName) {
      super.securityName(securityName);
      return this;
    }

    @Override
    public Builder fileMibPath(String fileMibPath) {
      super.fileMibPath(fileMibPath);
      return this;
    }

    @Override
    public Builder trapTarget(String trapTarget) {
      super.trapTarget(trapTarget);
      return this;
    }

    @Override
    public Builder format(SneoVariableTextFormat format) {
      super.format(format);
      return this;
    }

    public Builder communityStringIndexes(
      List<String> communityStringIndexes
    ) {
      this.communityStringIndexes = communityStringIndexes;
      return this;
    }

    public Builder communityStringIndexDelimiter(String communityStringIndexDelimiter) {
      this.communityStringIndexDelimiter = communityStringIndexDelimiter;
      return this;
    }

    @Override
    public FileMibCiscoAgent build() {
      return new FileMibCiscoAgent(this);
    }
  }

}
