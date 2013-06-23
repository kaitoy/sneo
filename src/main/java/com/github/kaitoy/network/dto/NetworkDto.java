/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.network.dto;

import java.io.Serializable;
import java.util.List;

public class NetworkDto implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -1389244413041638932L;

  private Integer id;
  private String name;
  private List<NodeDto> nodes;
  private List<L2ConnectionDto> l2Connections;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<NodeDto> getNodes() {
    return nodes;
  }

  public void setNodes(List<NodeDto> nodes) {
    this.nodes = nodes;
  }

  public List<L2ConnectionDto> getL2Connections() {
    return l2Connections;
  }

  public void setL2Connections(List<L2ConnectionDto> l2Connections) {
    this.l2Connections = l2Connections;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) { return true; }
    if (!this.getClass().isInstance(obj)) { return false; }
    return this.id == ((NetworkDto)obj).getId();
  }

  @Override
  public int hashCode() {
    return id;
  }

}
