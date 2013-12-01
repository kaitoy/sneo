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
  private boolean trunk;

  public VlanMemberDto(VlanMember model) {
    this.id = model.getId();
    this.setName(model.getName());
    this.trunk = model.isTrunk();
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

  public boolean isTrunk() {
    return trunk;
  }

  public void setTrunk(boolean trunk) {
    this.trunk = trunk;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) { return true; }
    if (!this.getClass().isInstance(obj)) { return false; }
    return this.id.equals(((VlanMemberDto)obj).getId());
  }

  @Override
  public int hashCode() {
    return id;
  }

}
