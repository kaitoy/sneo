/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dto;

import java.io.Serializable;
import com.github.kaitoy.sneo.giane.model.TrapTarget;

public class TrapTargetDto implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 7261075084198678366L;

  private Integer id;
  private String name;
  private String address;
  private Integer port;
  private String descr;

  public TrapTargetDto(TrapTarget model) {
    this.id = model.getId();
    this.name = model.getName();
    this.address = model.getAddress();
    this.port = model.getPort();
    this.setDescr(model.getDescr());
  }

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

  public String getDescr() {
    return descr;
  }

  public void setDescr(String descr) {
    this.descr = descr;
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
