/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dto;

import java.io.Serializable;

import com.github.kaitoy.sneo.giane.model.Simulation;

public class SimulationDto implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 693339634198948539L;

  private Integer id;
  private String name;
  private String network;

  public SimulationDto(Simulation model) {
    this.id = model.getId();
    this.name = model.getName();
    this.network = model.getNetwork().getName();
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

  public String getNetwork() {
    return network;
  }

  public void setNetwork(String network) {
    this.network = network;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) { return true; }
    if (!this.getClass().isInstance(obj)) { return false; }
    return this.id == ((SimulationDto)obj).getId();
  }

  @Override
  public int hashCode() {
    return id;
  }

}
