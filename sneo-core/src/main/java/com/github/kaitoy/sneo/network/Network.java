package com.github.kaitoy.sneo.network;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.management.ObjectName;
import mx4j.log.Log4JLogger;
import org.pcap4j.util.MacAddress;
import org.snmp4j.SNMP4JSettings;
import org.snmp4j.log.Log4jLogFactory;
import org.snmp4j.log.LogFactory;
import com.github.kaitoy.sneo.agent.FileMibAgent;
import com.github.kaitoy.sneo.agent.FileMibCiscoAgent;
import com.github.kaitoy.sneo.jmx.JmxAgent;
import com.github.kaitoy.sneo.network.dto.IpAddressDto;
import com.github.kaitoy.sneo.network.dto.IpV4RouteDto;
import com.github.kaitoy.sneo.network.dto.L2ConnectionDto;
import com.github.kaitoy.sneo.network.dto.LagDto;
import com.github.kaitoy.sneo.network.dto.NetworkDto;
import com.github.kaitoy.sneo.network.dto.NodeDto;
import com.github.kaitoy.sneo.network.dto.PhysicalNetworkInterfaceDto;
import com.github.kaitoy.sneo.network.dto.RealNetworkInterfaceDto;
import com.github.kaitoy.sneo.network.dto.SnmpAgentDto;
import com.github.kaitoy.sneo.network.dto.TrapTargetDto;
import com.github.kaitoy.sneo.network.dto.TrapTargetGroupDto;
import com.github.kaitoy.sneo.network.dto.VlanDto;
import com.github.kaitoy.sneo.network.dto.VlanMemberDto;
import com.github.kaitoy.sneo.smi.SmiSyntaxesPropertiesManager;
import com.github.kaitoy.sneo.transport.TransportsPropertiesManager;
import com.github.kaitoy.sneo.util.ColonSeparatedOidTypeValueVariableTextFormat;


public class Network {

  static {
    LogFactory.setLogFactory(new Log4jLogFactory());
    mx4j.log.Log.redirectTo(new Log4JLogger());
    SmiSyntaxesPropertiesManager.getInstance().useExtendedSmi();
    TransportsPropertiesManager.getInstance().extendTransportMappings();
    SNMP4JSettings.setVariableTextFormat(
      ColonSeparatedOidTypeValueVariableTextFormat.getInstance()
    );
  }

  private final String name;
  private List<FileMibAgent> agents = new ArrayList<FileMibAgent>();
  private List<Node> nodes = new ArrayList<Node>();
  private List<L2Connection> l2conns = new ArrayList<L2Connection>();
  private Map<Integer, PhysicalNetworkInterface> physNifs
    = new HashMap<Integer, PhysicalNetworkInterface>();

  public Network(NetworkDto networkDto) {
    this.name = networkDto.getName();

    for (NodeDto nodeDto: networkDto.getNodes()) {
      nodes.add(newNode(nodeDto));
    }
    for (L2ConnectionDto l2Dto: networkDto.getL2Connections()) {
      l2conns.add(newL2Connection(l2Dto));
    }
  }

  public List<FileMibAgent> getAgents() {
    return agents;
  }

  public List<Node> getNodes() {
    return nodes;
  }

  public void start(JmxAgent jmxAgent) {
    for (Node node: nodes) {
      node.start();
      jmxAgent.registerPojo(node, formObjectName(node));
    }
    for (FileMibAgent agent: agents) {
      agent.start();
    }
    for (L2Connection l2conn: l2conns) {
      l2conn.start();
    }
  }

  public void stop(JmxAgent jmxAgent) {
    for (Node node: nodes) {
      jmxAgent.unregisterMBean(formObjectName(node));
      node.shutdown();
    }
    for (FileMibAgent agent: agents) {
      agent.shutdown();
    }
    for (L2Connection l2conn: l2conns) {
      l2conn.shutdown();
    }
  }

  private String formObjectName(Node node) {
    StringBuilder sb = new StringBuilder(200);
    sb.append(ObjectName.quote(name))
      .append(":")
      .append("type=")
      .append(ObjectName.quote(node.getClass().getSimpleName()))
      .append(",name=")
      .append(ObjectName.quote(node.getName()))
      .append(",address=")
      .append(ObjectName.quote(node.getAgent().getAddress()));
    return sb.toString();
  }

  private FileMibAgent newSnmpAgent(SnmpAgentDto snmpAgentDto) {
    FileMibAgent.Builder vAgentBuilder;

    List<String> communityStringIndexes
      = snmpAgentDto.getCommunityStringIndexes();
    if (communityStringIndexes != null && communityStringIndexes.size() != 0) {
      vAgentBuilder
        = new FileMibCiscoAgent.Builder()
            .communityStringIndexes(communityStringIndexes);
    }
    else {
      vAgentBuilder = new FileMibAgent.Builder();
    }

    String address = snmpAgentDto.getAddress();
    vAgentBuilder
      .address("udp:" + address + "/" + snmpAgentDto.getPort())
      .bcConfigFilePath("conf" + File.separator + address + "_bc.conf")
      .configFilePath("conf" + File.separator + address + ".conf")
      .communityName(snmpAgentDto.getCommunityName())
      .securityName(snmpAgentDto.getSecurityName())
      .fileMibPath(snmpAgentDto.getFileMibPath())
      .format(snmpAgentDto.getFileMibFormat());

    TrapTargetGroupDto trapTargetGroup = snmpAgentDto.getTrapTargetGroup();
    if (trapTargetGroup != null) {
      List<TrapTargetDto> trapTargets = trapTargetGroup.getTrapTargets();
      if (trapTargets != null && trapTargets.size() != 0) {
        TrapTargetDto tt = trapTargets.get(0);
        vAgentBuilder.trapTarget(tt.getAddress() + "/" + tt.getPort());
      }
    }

    FileMibAgent agent = vAgentBuilder.build();
    try {
      agent.init();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
    return agent;
  }

  private Node newNode(NodeDto nodeDto) {
    FileMibAgent agent = newSnmpAgent(nodeDto.getAgent());
    agents.add(agent);

    Node node = new Node(nodeDto.getName(), agent, nodeDto.getTtl());

    for (PhysicalNetworkInterfaceDto nifDto: nodeDto.getPhysicalNetworkInterfaces()) {
      node.addNif(nifDto.getName(), nifDto.isTrunk());
      for (IpAddressDto ipAddrDto: nifDto.getIpAddresses()) {
        node.addIpAddress(
          nifDto.getName(),
          ipAddrDto.getAddress(),
          ipAddrDto.getPrefixLength()
        );
      }

      physNifs.put(
        nifDto.getId(),
        (PhysicalNetworkInterface)node.getNif(nifDto.getName())
      );
    }

    for (RealNetworkInterfaceDto nifDto: nodeDto.getRealNetworkInterfaces()) {
      node.addRealNif(
        nifDto.getName(),
        MacAddress.getByName(nifDto.getMacAddress()),
        nifDto.getDeviceName()
      );
      for (IpAddressDto ipAddrDto: nifDto.getIpAddresses()) {
        node.addIpAddress(
          nifDto.getName(),
          ipAddrDto.getAddress(),
          ipAddrDto.getPrefixLength()
        );
      }
    }

    for (LagDto lagDto: nodeDto.getLags()) {
      node.addLag(lagDto.getName(), lagDto.getChannelGroupNumber());
      for (IpAddressDto ipAddrDto: lagDto.getIpAddresses()) {
        node.addIpAddress(
          lagDto.getName(),
          ipAddrDto.getAddress(),
          ipAddrDto.getPrefixLength()
        );
      }
      for (PhysicalNetworkInterfaceDto nifDto: lagDto.getPhysicalNetworkInterfaces()) {
        node.addNifToLag(nifDto.getName(), lagDto.getChannelGroupNumber());
      }
    }

    for (VlanDto vlanDto: nodeDto.getVlans()) {
      node.addVlan(vlanDto.getName(), vlanDto.getVid());
      for (IpAddressDto ipAddrDto: vlanDto.getIpAddresses()) {
        node.addIpAddress(
          vlanDto.getName(),
          ipAddrDto.getAddress(),
          ipAddrDto.getPrefixLength()
        );
      }
      for (VlanMemberDto nifDto: vlanDto.getVlanMembers()) {
        String aggregatorName = null;
        if (nifDto instanceof PhysicalNetworkInterfaceDto) {
          aggregatorName
            = ((PhysicalNetworkInterfaceDto)nifDto).getAggregatorName();
        }

        if (aggregatorName != null) {
          // Add an aggregater instead of a NIF itself
          // so forwarding done in VlanInterface works well.
          node.addNifToVlan(aggregatorName, vlanDto.getVid());
        }
        else {
          node.addNifToVlan(nifDto.getName(), vlanDto.getVid());
        }
      }
    }

    for (IpV4RouteDto routeDto: nodeDto.getIpV4Routes()) {
      node.addRoute(
        routeDto.getNetworkDestination(),
        routeDto.getNetmask(),
        routeDto.getGateway(),
        routeDto.getMetric()
      );
    }

    return node;
  }

  private L2Connection newL2Connection(L2ConnectionDto l2Dto) {
    List<PhysicalNetworkInterface> vnifs
      = new ArrayList<PhysicalNetworkInterface>();
    for (PhysicalNetworkInterfaceDto nifDto: l2Dto.getPhysicalNetworkInterfaces()) {
      vnifs.add(physNifs.get(nifDto.getId()));
    }

    return L2Connection.connect(
             l2Dto.getName(),
             vnifs.toArray(new PhysicalNetworkInterface[0])
           );
  }

}
