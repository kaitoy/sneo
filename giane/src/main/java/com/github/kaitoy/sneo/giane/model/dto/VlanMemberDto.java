/*_##########################################################################
  _##
  _##  Copyright (C) 2012  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dto;

import java.io.Serializable;

import com.github.kaitoy.sneo.giane.model.VlanMember;

public class VlanMemberDto implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 7553824662673438768L;

  private Integer id;
  private String name;

  public VlanMemberDto(VlanMember model) {
    this.id = model.getId();
    this.setName(model.getName());
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

  @Override
  public boolean equals(Object obj) {
    if (obj == this) { return true; }
    if (!this.getClass().isInstance(obj)) { return false; }
    return this.id == ((VlanMemberDto)obj).getId();
  }

  @Override
  public int hashCode() {
    return id;
  }

}
