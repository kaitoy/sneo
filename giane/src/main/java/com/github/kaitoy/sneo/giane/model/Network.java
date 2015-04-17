/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2015 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.github.kaitoy.sneo.network.dto.L2ConnectionDto;
import com.github.kaitoy.sneo.network.dto.NetworkDto;
import com.github.kaitoy.sneo.network.dto.NodeDto;
import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;

@Entity
@Table(name = "NETWORK")
public class Network extends AbstractModel implements FormMessage {

  /**
   *
   */
  private static final long serialVersionUID = 3149782097855948582L;

  private String name;
  private String descr;
  private List<Node> nodes;
  private List<L2Connection> l2Connections;

  @Column(name = "NAME", nullable = false, length = 200, unique = true)
  public String getName() {
    return name;
  }

  @RequiredStringValidator(
    key = "RequiredStringValidator.error",
    trim = true,
    shortCircuit = true // Stops checking if detects error
  )
  @StringLengthFieldValidator(
    key = "StringLengthFieldValidator.error.max",
    trim = true,
    maxLength = "200",
    shortCircuit = true
  )
  @RegexFieldValidator(
    key = "RegexFieldValidator.error.objectName",
    // this field's value is/will be used for an MBean object name, and may be used in a command line.
    expression = "[^,=:\"*?]+",
    shortCircuit = true
  )
  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "DESCR", nullable = true, length = 5000, unique = false)
  public String getDescr() {
    return descr;
  }

  @StringLengthFieldValidator(
    key = "StringLengthFieldValidator.error.max",
    trim = true,
    maxLength = "5000",
    shortCircuit = true // Stops checking if detects error
  )
  public void setDescr(String descr) {
    this.descr = descr;
  }

  @OneToMany(
    mappedBy = "network",
    fetch = FetchType.LAZY,
    orphanRemoval = true,
    cascade = {
      CascadeType.REMOVE
    }
  )
  public List<Node> getNodes() {
    return nodes;
  }

  public void setNodes(List<Node> nodes) {
    this.nodes = nodes;
  }

  @OneToMany(
    mappedBy = "network",
    fetch = FetchType.LAZY,
    orphanRemoval = true,
    cascade = {
      CascadeType.REMOVE
    }
  )
  public List<L2Connection> getL2Connections() {
    return l2Connections;
  }

  public void setL2Connections(List<L2Connection> l2Connections) {
    this.l2Connections = l2Connections;
  }

  public NetworkDto toDto() {
    List<NodeDto> nodeDtos = new ArrayList<NodeDto>();
    for (Node node: nodes) {
      nodeDtos.add(node.toDto());
    }

    List<L2ConnectionDto> l2ConnectionDtos = new ArrayList<L2ConnectionDto>();
    for (L2Connection l2Connection: l2Connections) {
      l2ConnectionDtos.add(l2Connection.toDto());
    }

    NetworkDto dto = new NetworkDto();
    dto.setId(getId());
    dto.setName(name);
    dto.setNodes(nodeDtos);
    dto.setL2Connections(l2ConnectionDtos);
    return dto;
  }

}
