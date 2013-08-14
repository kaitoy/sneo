/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dto;

import java.io.Serializable;

import com.github.kaitoy.sneo.giane.model.Vlan;

public class VlanDto implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 5819690592116317232L;

  private Integer id;
  private String name;
  private Integer vid;

  public VlanDto(Vlan model) {
    this.id = model.getId();
    this.name = model.getName();
    this.vid = model.getVid();
  }

  public Integer getId() { return id; }

  public void setId(Integer id) { this.id = id; }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getVid() {
    return vid;
  }

  public void setVid(Integer vid) {
    this.vid = vid;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) { return true; }
    if (!this.getClass().isInstance(obj)) { return false; }
    return this.id == ((VlanDto)obj).getId();
  }

  @Override
  public int hashCode() {
    return id;
  }

}
