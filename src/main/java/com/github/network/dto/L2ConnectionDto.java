/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.network.dto;

import java.io.Serializable;
import java.util.List;

public class L2ConnectionDto implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 5662636061226823639L;

  private Integer id;
  private String name;
  private List<PhysicalNetworkInterfaceDto> physicalNetworkInterfaces;

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

  public List<PhysicalNetworkInterfaceDto> getPhysicalNetworkInterfaces() {
    return physicalNetworkInterfaces;
  }

  public void setPhysicalNetworkInterfaces(
    List<PhysicalNetworkInterfaceDto> physicalNetworkInterfaces
  ) {
    this.physicalNetworkInterfaces = physicalNetworkInterfaces;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) { return true; }
    if (!this.getClass().isInstance(obj)) { return false; }
    return this.id == ((L2ConnectionDto)obj).getId();
  }

  @Override
  public int hashCode() {
    return id;
  }

}
