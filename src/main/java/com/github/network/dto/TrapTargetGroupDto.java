/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.network.dto;

import java.io.Serializable;
import java.util.List;

public class TrapTargetGroupDto implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 3332464934840929771L;

  private Integer id;
  private String name;
  private List<TrapTargetDto> trapTargets;

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

  public List<TrapTargetDto> getTrapTargets() {
    return trapTargets;
  }

  public void setTrapTargets(List<TrapTargetDto> trapTargets) {
    this.trapTargets = trapTargets;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) { return true; }
    if (!this.getClass().isInstance(obj)) { return false; }
    return this.id == ((TrapTargetGroupDto)obj).getId();
  }

  @Override
  public int hashCode() {
    return id;
  }

}
