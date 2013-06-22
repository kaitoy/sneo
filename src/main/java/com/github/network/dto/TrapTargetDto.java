/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.network.dto;

import java.io.Serializable;

public class TrapTargetDto implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 934893135378595458L;

  private Integer id;
  private String name;
  private String address;
  private Integer port;

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

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) { return true; }
    if (!this.getClass().isInstance(obj)) { return false; }
    return this.id == ((TrapTargetDto)obj).getId();
  }

  @Override
  public int hashCode() {
    return id;
  }

}
