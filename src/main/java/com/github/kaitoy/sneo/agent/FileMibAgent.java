/*_##########################################################################
  _##
  _##  Copyright (C) 2011-2012  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.agent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.snmp4j.TransportMapping;
import org.snmp4j.agent.BaseAgent;
import org.snmp4j.agent.CommandProcessor;
import org.snmp4j.agent.DefaultMOContextScope;
import org.snmp4j.agent.DefaultMOQuery;
import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.agent.MOGroup;
import org.snmp4j.agent.ManagedObject;
import org.snmp4j.agent.mo.MOTableRow;
import org.snmp4j.agent.mo.snmp.NotificationOriginatorImpl;
import org.snmp4j.agent.mo.snmp.RowStatus;
import org.snmp4j.agent.mo.snmp.SnmpCommunityMIB;
import org.snmp4j.agent.mo.snmp.SnmpNotificationMIB;
import org.snmp4j.agent.mo.snmp.SnmpTargetMIB;
import org.snmp4j.agent.mo.snmp.StorageType;
import org.snmp4j.agent.mo.snmp.SysUpTime;
import org.snmp4j.agent.mo.snmp.TransportDomains;
import org.snmp4j.agent.mo.snmp.VacmMIB;
import org.snmp4j.agent.mo.snmp4j.Snmp4jConfigMib;
import org.snmp4j.agent.mo.snmp4j.Snmp4jLogMib;
import org.snmp4j.agent.security.MutableVACM;
import org.snmp4j.asn1.BERInputStream;
import org.snmp4j.asn1.BEROutputStream;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.log.LogFactory;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MessageProcessingModel;
import org.snmp4j.mp.StatusInformation;
import org.snmp4j.security.AuthSHA;
import org.snmp4j.security.PrivDES;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModel;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityParameters;
import org.snmp4j.security.SecurityStateReference;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.Null;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.TransportMappings;
import com.github.kaitoy.sneo.agent.mo.MutableStaticMOGroup;
import com.github.kaitoy.sneo.agent.mo.SnmpAccessStatisticsGatherer;
import com.github.kaitoy.sneo.agent.mo.SnmpAccessStatisticsGathererImpl;
import com.github.kaitoy.sneo.agent.mo.SysUpTimeImpl;
import com.github.kaitoy.sneo.network.SneoContext;
import com.github.kaitoy.sneo.util.ColonSeparatedOidTypeValueVariableTextFormat;
import com.github.kaitoy.sneo.util.ContextfulWorkerPool;
import com.github.kaitoy.sneo.util.SneoVariableTextFormat;

public class FileMibAgent extends BaseAgent {

  private static final LogAdapter logger
    = LogFactory.getLogger(FileMibAgent.class.getPackage().getName());

  private static final String CONFIG_CONTEXT = "config";
  private static final String FILEMIB_CONTEXT = "";
  private static final OID FILEMIB_ROOT = new OID("1");
  private static final int ENTERPRISE_ID = 40303; // http://www.iana.org/assignments/enterprise-numbers
  private static final OID SYSUPTIME_OID = new OID("1.3.6.1.2.1.1.3.0");
  private static final OID AUTHENTICATION_PROTOCOL = AuthSHA.ID;
  private static final String AUTHENTICATION_PASSWORD = "password";
  private static final OID PRIVACY_PROTOCOL = PrivDES.ID;
  private static final String PRIVACY_PASSWORD = "password";

  private final String address;
  private final String communityName;
  private final String securityName;
  private final String trapTarget;
  private final FileMibLoader fileMIBLoader;
  private final ContextfulWorkerPool<SneoContext> contextfulWorkerPool;
  private final SysUpTimeImpl sysUpTime = new SysUpTimeImpl();
  private final Object thisLock = new Object();

  private volatile String fileMibPath;
  private boolean running = false;
  private boolean gatheringSnmpAccessStatistics = false;

  private Map<OctetString, List<MOGroup>> moGroupsPerContextName
    = new HashMap<OctetString, List<MOGroup>>();

  protected FileMibAgent(Builder b) {
    super(
      new File(b.bcConfigFilePath),
      new File(b.configFilePath),
      new CommandProcessor(createLocalEngineID(b.address))
    );

    this.address = b.address;
    this.communityName = b.communityName;
    this.securityName = b.securityName;
    this.fileMibPath = b.fileMibPath;
    this.trapTarget = b.trapTarget;
    this.fileMIBLoader = new FileMibLoader(b.format);
    this.contextfulWorkerPool
      = new ContextfulWorkerPool<SneoContext>(
          "CommandProcessor_" + this.address,
          AgentPropertiesLoader.getInstance().getWorkerPoolSize()
        );
  }

  private static OctetString createLocalEngineID(String address) {
    // RFC 3411

    OctetString engineID
      = new OctetString(
          new byte[] {
            (byte)(0x80 | (( ENTERPRISE_ID >> 24) & 0xFF)),
            (byte)((ENTERPRISE_ID >> 16) & 0xFF),
            (byte)((ENTERPRISE_ID >> 8) & 0xFF),
            (byte)(ENTERPRISE_ID & 0xFF),
          }
        );

    int idxColon = address.indexOf("p:"); // (tcp|udp):
    int idxSlash = address.indexOf("/");
    String ipAddr
      = address.substring(
          idxColon == -1 ? 0 : idxColon + 2,
          idxSlash == -1 ? address.length() : idxSlash
        );
    InetAddress inetAddr;
    try {
      inetAddr = InetAddress.getByName(ipAddr);
    } catch (UnknownHostException e) {
      throw new AssertionError("Never get here.");
    }

    /*
     * 0     - reserved, unused.
     * 1     - IPv4 address (4 octets)
     *          lowest non-special IP address
     * 2     - IPv6 address (16 octets)
     *          lowest non-special IP address
     * 3     - MAC address (6 octets)
     *          lowest IEEE MAC address, canonical
     *          order
     * 4     - Text, administratively assigned
     *          Maximum remaining length 27
     * 5     - Octets, administratively assigned
     *          Maximum remaining length 27
     * 6-127 - reserved, unused
     * 128-255 - as defined by the enterprise
     *          Maximum remaining length 27
     */
    if (inetAddr instanceof Inet4Address) {
      engineID.append((byte)1);
    }
    else {
      engineID.append((byte)2);
    }

    engineID.append(new OctetString(inetAddr.getAddress()));

    return engineID;
  }

  public OctetString getLocalEngineID() {
    return getAgent().getContextEngineID();
  }

  public String getAddress() {
    return address;
  }

  public InetAddress getInetAddress() {
    try {
      return InetAddress.getByName(
               address.substring(
                 address.indexOf(":") + 1,
                 address.indexOf("/")
               )
             );
    } catch (UnknownHostException e) {
      throw new AssertionError("Never get here.");
    }
  }

  public String getCommunityName() {
    return communityName;
  }

  public String getSecurityName() {
    return securityName;
  }

  public String getTrapTarget() {
    return trapTarget;
  }

  public FileMibLoader getFileMIBLoader() {
    return fileMIBLoader;
  }

  public String getFormatName() {
    return fileMIBLoader.getFormat().getClass().getSimpleName();
  }

  public String getFileMibPath() {
    return fileMibPath;
  }

  public void setFileMibPath(String fileMibPath) {
    this.fileMibPath = fileMibPath;
  }

  public ContextfulWorkerPool<SneoContext> getContextfulWorkerPool() {
    return contextfulWorkerPool;
  }

  public boolean isRunning() {
    synchronized (thisLock) {
      return running;
    }
  }

  public TransportMapping getTransportMapping() {
    return transportMappings[0];
  }

  public void setGatheringSnmpAccessStatistics(boolean gathering) {
    synchronized (thisLock) {
      for (List<MOGroup> mogList: moGroupsPerContextName.values()) {
        for (MOGroup mog: mogList) {
          if (mog instanceof MutableStaticMOGroup) {
            ((MutableStaticMOGroup)mog)
              .setEnableSnmpAccessStatisticsGatherer(gathering);
          }
        }
      }
      gatheringSnmpAccessStatistics = gathering;
    }
  }

  public boolean isGatheringSnmpAccessStatistics() {
    synchronized (thisLock) {
      return gatheringSnmpAccessStatistics;
    }
  }

  protected synchronized MOGroup getFileMibMoGroup() {
    synchronized (thisLock) {
      List<MOGroup> mogs
        = moGroupsPerContextName.get(new OctetString(FILEMIB_CONTEXT));

      if (mogs == null || mogs.size() != 1) {
        throw new IllegalStateException();
      }

      return mogs.get(0);
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
    if (communityName == null || communityName.equals("")) {
      logger.info("No community name.");
      return;
    }

    Variable[] com2sec
      = new Variable[] {
          new OctetString(communityName), // community name
          new OctetString("com" + communityName), // security name
          getLocalEngineID(), // local engine ID
          new OctetString(FILEMIB_CONTEXT), // default context name
          new OctetString(), // transport tag
          new Integer32(StorageType.volatile_), // storage type
          new Integer32(RowStatus.active) // row status
        };
    MOTableRow row
      = communityMIB.getSnmpCommunityEntry().createRow(
          new OctetString(communityName + "2com" + communityName)
            .toSubIndex(true),
          com2sec
        );
    communityMIB.getSnmpCommunityEntry().addRow(row);
  }

  /**
   * Adds initial notification targets and filters.
   *
   * @param targetMIB the SnmpTargetMIB holding the target configuration.
   * @param notificationMIB the SnmpNotificationMIB holding the notification
   *   (filter) configuration.
   */
  @Override
  protected void addNotificationTargets(SnmpTargetMIB targetMIB,
                                        SnmpNotificationMIB notificationMIB) {
    if (trapTarget == null || trapTarget.equals("")) {
      logger.info("No trap target.");
      return;
    }

    targetMIB.addDefaultTDomains();

    targetMIB.addTargetAddress(
      new OctetString("notificationV2c"),
      TransportDomains.transportDomainUdpIpv4,
      new OctetString(new UdpAddress(trapTarget).getValue()),
      200, 1,
      new OctetString("notify"),
      new OctetString("v2cParams"),
      StorageType.volatile_
    );
//    targetMIB.addTargetAddress(
//      new OctetString("notificationV3"),
//      TransportDomains.transportDomainUdpIpv4,
//      new OctetString(new UdpAddress(trapTarget + "/1162").getValue()),
//      200, 1,
//      new OctetString("notify"),
//      new OctetString("v3notifyParams"),
//      StorageType.volatile_
//    );

    targetMIB.addTargetParams(
      new OctetString("v2cParams"),
      MessageProcessingModel.MPv2c,
      SecurityModel.SECURITY_MODEL_SNMPv2c,
      new OctetString("com" + communityName),
      SecurityLevel.NOAUTH_NOPRIV,
      StorageType.volatile_
    );
//    targetMIB.addTargetParams(
//      new OctetString("v3notifyParams"),
//      MessageProcessingModel.MPv3,
//      SecurityModel.SECURITY_MODEL_USM,
//      new OctetString(securityName),
//      SecurityLevel.AUTH_PRIV,
//      StorageType.volatile_
//    );

    notificationMIB.addNotifyEntry(
      new OctetString("default"),
      new OctetString("notify"),
      SnmpNotificationMIB.SnmpNotifyTypeEnum.trap,
      StorageType.volatile_
    );
  }

  /**
   * Adds all the necessary initial users to the USM.
   *
   * @param usm the USM instance used by this agent.
   */
  @Override
  protected void addUsmUser(USM usm) {
    if (securityName == null || securityName.equals("")) {
      logger.info("No security name.");
      return;
    }

    UsmUser user = new UsmUser(
      new OctetString(securityName),
      AUTHENTICATION_PROTOCOL,
      new OctetString(AUTHENTICATION_PASSWORD),
      PRIVACY_PROTOCOL,
      new OctetString(PRIVACY_PASSWORD)
    );
    usm.addUser(user.getSecurityName(), null, user);
  }

  /**
   * Adds initial VACM configuration.
   *
   * @param vacm
   *    the VacmMIB holding the agent's view configuration.
   */
  @Override
  protected void addViews(VacmMIB vacm) {
    if (communityName != null && !communityName.equals("")) {
      vacm.addGroup(
        SecurityModel.SECURITY_MODEL_SNMPv1,
        new OctetString("com" + communityName),
        new OctetString("v1v2group"),
        StorageType.volatile_
      );
      vacm.addGroup(
        SecurityModel.SECURITY_MODEL_SNMPv2c,
        new OctetString("com" + communityName),
        new OctetString("v1v2group"),
        StorageType.volatile_
      );

      vacm.addAccess(
        new OctetString("v1v2group"),
        new OctetString(FILEMIB_CONTEXT),
        SecurityModel.SECURITY_MODEL_SNMPv1,
        SecurityLevel.NOAUTH_NOPRIV,
        MutableVACM.VACM_MATCH_EXACT,
        new OctetString("fullView"),
        new OctetString("fullView"),
        new OctetString("fullView"),
        StorageType.volatile_
      );
      vacm.addAccess(
        new OctetString("v1v2group"),
        new OctetString(FILEMIB_CONTEXT),
        SecurityModel.SECURITY_MODEL_SNMPv2c,
        SecurityLevel.NOAUTH_NOPRIV,
        MutableVACM.VACM_MATCH_EXACT,
        new OctetString("fullView"),
        new OctetString("fullView"),
        new OctetString("fullView"),
        StorageType.volatile_
      );

      // This enables a set-access to the VacmMIB.
      // See VacmMIB.vacmAccessIndex#isValidIndex()
      addSecurityModels();
    }

    if (securityName != null && !securityName.equals("")) {
      vacm.addGroup(
        SecurityModel.SECURITY_MODEL_USM,
        new OctetString(securityName),
        new OctetString("v3group"),
        StorageType.volatile_
      );

      vacm.addAccess(
        new OctetString("v3group"),
        new OctetString(FILEMIB_CONTEXT),
        SecurityModel.SECURITY_MODEL_USM,
        SecurityLevel.AUTH_PRIV,
        MutableVACM.VACM_MATCH_EXACT,
        new OctetString("fullView"),
        new OctetString("fullView"),
        new OctetString("fullView"),
        StorageType.volatile_
      );
      vacm.addAccess(
        new OctetString("v3group"),
        new OctetString(CONFIG_CONTEXT),
        SecurityModel.SECURITY_MODEL_USM,
        SecurityLevel.AUTH_PRIV,
        MutableVACM.VACM_MATCH_EXACT,
        new OctetString("fullView"),
        new OctetString("fullView"),
        new OctetString("fullView"),
        StorageType.volatile_
      );
    }

    vacm.addViewTreeFamily(
      new OctetString("fullView"),
      new OID("1"),
      new OctetString(),
      VacmMIB.vacmViewIncluded,
      StorageType.volatile_
    );
  }

  private void addSecurityModels() {
    SecurityModels.getInstance().addSecurityModel(
      new SecurityModel() {

        public int getID() {
          return SecurityModel.SECURITY_MODEL_SNMPv1;
        }

        public SecurityParameters newSecurityParametersInstance() {
          return null;
        }

        public SecurityStateReference newSecurityStateReference() {
          return null;
        }

        public int generateRequestMessage(
          int messageProcessingModel, byte[] globalData, int maxMessageSize,
          int securityModel, byte[] securityEngineID, byte[] securityName,
          int securityLevel, BERInputStream scopedPDU,
          SecurityParameters securityParameters, BEROutputStream wholeMsg
        ) throws IOException {
          return 0;
        }

        public int generateResponseMessage(
          int messageProcessingModel, byte[] globalData, int maxMessageSize,
          int securityModel, byte[] securityEngineID, byte[] securityName,
          int securityLevel, BERInputStream scopedPDU,
          SecurityStateReference securityStateReference,
          SecurityParameters securityParameters, BEROutputStream wholeMsg
        ) throws IOException {
          return 0;
        }

        public int processIncomingMsg(
          int messageProcessingModel, int maxMessageSize,
          SecurityParameters securityParameters, SecurityModel securityModel,
          int securityLevel, BERInputStream wholeMsg,
          OctetString securityEngineID, OctetString securityName,
          BEROutputStream scopedPDU, Integer32 maxSizeResponseScopedPDU,
          SecurityStateReference securityStateReference,
          StatusInformation statusInfo
        ) throws IOException {
          return 0;
        }

      }
    );

    SecurityModels.getInstance().addSecurityModel(
      new SecurityModel() {

        public int getID() {
          return SecurityModel.SECURITY_MODEL_SNMPv2c;
        }

        public SecurityParameters newSecurityParametersInstance() {
          return null;
        }

        public SecurityStateReference newSecurityStateReference() {
          return null;
        }

        public int generateRequestMessage(
          int messageProcessingModel, byte[] globalData, int maxMessageSize,
          int securityModel, byte[] securityEngineID, byte[] securityName,
          int securityLevel, BERInputStream scopedPDU,
          SecurityParameters securityParameters, BEROutputStream wholeMsg
        ) throws IOException {
          return 0;
        }

        public int generateResponseMessage(
          int messageProcessingModel, byte[] globalData, int maxMessageSize,
          int securityModel, byte[] securityEngineID, byte[] securityName,
          int securityLevel, BERInputStream scopedPDU,
          SecurityStateReference securityStateReference,
          SecurityParameters securityParameters, BEROutputStream wholeMsg
        ) throws IOException {
          return 0;
        }

        public int processIncomingMsg(
          int messageProcessingModel, int maxMessageSize,
          SecurityParameters securityParameters, SecurityModel securityModel,
          int securityLevel, BERInputStream wholeMsg,
          OctetString securityEngineID, OctetString securityName,
          BEROutputStream scopedPDU, Integer32 maxSizeResponseScopedPDU,
          SecurityStateReference securityStateReference,
          StatusInformation statusInfo
        ) throws IOException {
          return 0;
        }

      }
    );
  }

  /**
   * Register additional managed objects at the agent's server.
   */
  @Override
  protected void registerManagedObjects() {
    synchronized (thisLock) {
      try {
        OctetString contextName = new OctetString(FILEMIB_CONTEXT);
        List<VariableBinding> subtree
          = fileMIBLoader.load(fileMibPath, FILEMIB_ROOT);

        MutableStaticMOGroup group
          = new MutableStaticMOGroup(
              FILEMIB_ROOT,
              subtree.toArray(new VariableBinding[subtree.size()])
            );

        if (AgentPropertiesLoader.getInstance().emulateSysUpTime()) {
          group.registerVariableServer(SYSUPTIME_OID, sysUpTime);
        }

        DefaultMOContextScope scope
          = new DefaultMOContextScope(
              contextName,
              FILEMIB_ROOT,
              true,
              FILEMIB_ROOT.nextPeer(), // TODO おおざっぱ
              false
            );

        ManagedObject mo = server.lookup(new DefaultMOQuery(scope, false));
        if (mo != null) {
          logger.error(
            "Could not register subtree '" + FILEMIB_ROOT
              + "' because ManagedObject " + mo
              + " is already registered"
          );
        }
        else {
          doRegister(contextName, group);
          logger.info(
            "Registered managed objects. root: " + FILEMIB_ROOT
              + " subtee count: " + subtree.size()
          );
        }
      } catch (DuplicateRegistrationException e) {
        logger.error(
          "Error while reading fileMib '" + fileMibPath + "':" + e.getMessage(),
          e
        );
      } catch (FileNotFoundException e) {
        logger.error(e);
      } catch (IOException e) {
        logger.error(
          "Error while reading fileMib '" + fileMibPath + "':" + e.getMessage(),
          e
        );
      }
    }
  }

  protected void doRegister(
    OctetString contextName, MOGroup group
  ) throws DuplicateRegistrationException {
    synchronized (thisLock) {
      group.registerMOs(getServer(), contextName);

      if (moGroupsPerContextName.containsKey(contextName)) {
        List<MOGroup> groups = moGroupsPerContextName.get(contextName);
        groups.add(group);
      }
      else {
        List<MOGroup> groups = new ArrayList<MOGroup>();
        groups.add(group);
        moGroupsPerContextName.put(contextName, groups);
      }
    }
  }

  /**
   * Unregister additional managed objects from the agent's server.
   */
  @Override
  protected void unregisterManagedObjects() {
    synchronized (thisLock) {
      for (OctetString contextName: moGroupsPerContextName.keySet()) {
        for (MOGroup mog: moGroupsPerContextName.get(contextName)) {
          mog.unregisterMOs(server, contextName);
        }
      }
      moGroupsPerContextName.clear();
    }
  }

  @Override
  protected void initTransportMappings() throws IOException {
    transportMappings = new TransportMapping[1];
    Address addr = GenericAddress.parse(address);
    TransportMapping tm
      = TransportMappings.getInstance().createTransportMapping(addr);
    transportMappings[0] = tm;
  }

  @Override
  protected void initConfigMIB() {
    snmp4jLogMIB = new Snmp4jLogMib();
    // TODO defaultPersistenceProvider をなんとかすれば、可読なconifgファイルに
    if ((configFileURI != null) && (defaultPersistenceProvider != null)) {
      snmp4jConfigMIB = new Snmp4jConfigMib(snmpv2MIB.getSysUpTime()); // TODO
      snmp4jConfigMIB.setSnmpCommunityMIB(snmpCommunityMIB);
      snmp4jConfigMIB.setPrimaryProvider(defaultPersistenceProvider);
    }
  }

  @Override
  public void init() throws IOException {
    synchronized (thisLock) {
      if (getAgentState() != STATE_CREATED) {
        throw new IllegalStateException("Already be inited");
      }

      setDefaultContext(new OctetString(CONFIG_CONTEXT));
      getServer().addContext(new OctetString(CONFIG_CONTEXT));

      super.init();

      File BcFileDir
        = getBootCounterFile().getAbsoluteFile().getParentFile();
      if (!BcFileDir.exists()) {
        logger.info("Make directory for boot counter file: " + BcFileDir);
        BcFileDir.mkdirs();
      }

      File confFileDir
        = getConfigFile().getAbsoluteFile().getParentFile();
      if (!confFileDir.exists()) {
        logger.info("Make directory for config file: " + confFileDir);
        confFileDir.mkdirs();
      }

  // TODO
  //      if (getConfigFile().exists()) {
  //        loadConfig(ImportModes.REPLACE_CREATE);
  //      }

      SysUpTime upTime;
      if (AgentPropertiesLoader.getInstance().emulateSysUpTime()) {
        upTime = sysUpTime;
      }
      else {
        upTime = new SysUpTime() {
          private TimeTicks value
            = ((TimeTicks)getMib(SYSUPTIME_OID.toString()).getVariable());
          public TimeTicks get() { return value; }
        };

      }

      notificationOriginator
        = new NotificationOriginatorImpl(
            session,
            vacmMIB,
            upTime,
            snmpTargetMIB,
            snmpNotificationMIB,
            snmpCommunityMIB
          );

      getAgent().setWorkerPool(contextfulWorkerPool);

      // addShutdownHook();
      finishInit();
    }
  }

//  @Override
//  protected void addShutdownHook() {
//    Runtime.getRuntime().addShutdownHook(
//      new Thread() {
//        public void run() {
//          FileMibAgent.this.stop();
//          saveConfig();
//        }
//      }
//    );
//  }

  public void start() {
    synchronized (thisLock) {
      if (agentState < STATE_INIT_FINISHED) {
        throw new IllegalStateException("Not yet be inited.");
      }

      if (!running) {
        super.run();
        sysUpTime.start();
        running = true;
        sendColdStartNotification();
      }
    }
  }

  public void stop() {
    synchronized (thisLock) {
      if (running) {
        super.stop();
        sysUpTime.stop();
        running = false;
      }
    }
  }

  public void shutdown() {
    synchronized (thisLock) {
      if (running) {
        stop();
      }
      contextfulWorkerPool.stop();
      logger.info("shutdowned");
    }
  }

  public void reloadFileMib() {
    synchronized (thisLock) {
      unregisterManagedObjects();
      registerManagedObjects();
    }
  }

  public VariableBinding getMib(String strOid) {
    OID oid = new OID(strOid);

    DefaultMOContextScope scope =
      new DefaultMOContextScope(
        new OctetString(FILEMIB_CONTEXT),
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

  public List<VariableBinding> walkMib(String strOid, int count) {
    OID oid = new OID(strOid);

    DefaultMOContextScope scope
      =  new DefaultMOContextScope(
           new OctetString(FILEMIB_CONTEXT),
           oid, true, oid.nextPeer(), false
         );

    ManagedObject mo = server.lookup(new DefaultMOQuery(scope, false));
    if (mo == null) {
      return new ArrayList<VariableBinding>(0);
    }

    if (mo instanceof MutableStaticMOGroup) {
      return ((MutableStaticMOGroup)mo).walk(oid, count);
    }
    else {
      return new ArrayList<VariableBinding>(0);
    }
  }

  public Variable setMib(String strVarBind) throws ParseException {
    VariableBinding varBind
      = ColonSeparatedOidTypeValueVariableTextFormat.getInstance()
          .parseVariableBinding(strVarBind);
    OID oid = varBind.getOid();

    DefaultMOContextScope scope
      =  new DefaultMOContextScope(
           new OctetString(FILEMIB_CONTEXT),
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

  public void removeMessageProcessor(String version) {
    if (version.equals("1")) {
      dispatcher.removeMessageProcessingModel(new MPv1());
    }
    else if (version.equals("2c")) {
      dispatcher.removeMessageProcessingModel(new MPv2c());
    }
  }

  public String reportSnmpAccessStatistics() {
    synchronized (thisLock) {
      SnmpAccessStatisticsGatherer sasc
       = new SnmpAccessStatisticsGathererImpl();

      for (List<MOGroup> mogList: moGroupsPerContextName.values()) {
        for (MOGroup mog: mogList) {
          if (mog instanceof MutableStaticMOGroup) {
            sasc
              = sasc.merge(
                  ((MutableStaticMOGroup)mog).getSnmpAccessStatisticsGatherer()
                );
          }
        }
      }

      return sasc.getReport();
    }
  }

  // TODO toString()

  public static class Builder {

    private String address = null;
    private String bcConfigFilePath = "BC.cfg";
    private String configFilePath = "Config.cfg";
    private String communityName = "cm2get";
    private String securityName = "cm2get";
    private String fileMibPath = null;
    private String trapTarget = null;
    private SneoVariableTextFormat format
      = ColonSeparatedOidTypeValueVariableTextFormat.getInstance();

    public Builder() {}

    public Builder address(String address) {
      this.address = address;
      return this;
    }

    public Builder bcConfigFilePath(String bcConfigFilePath) {
      this.bcConfigFilePath = bcConfigFilePath;
      return this;
    }

     public Builder configFilePath(String configFilePath) {
      this.configFilePath = configFilePath;
      return this;
    }

    public Builder communityName(String communityName) {
      this.communityName = communityName;
      return this;
    }
    public Builder securityName(String securityName) {
      this.securityName = securityName;
      return this;
    }

    public Builder fileMibPath(String fileMibPath) {
      this.fileMibPath = fileMibPath;
      return this;
    }

    public Builder trapTarget(String trapTarget) {
      this.trapTarget = trapTarget;
      return this;
    }

    public Builder format(SneoVariableTextFormat format) {
      this.format = format;
      return this;
    }

    public FileMibAgent build() {
      return new FileMibAgent(this);
    }

  }

}
