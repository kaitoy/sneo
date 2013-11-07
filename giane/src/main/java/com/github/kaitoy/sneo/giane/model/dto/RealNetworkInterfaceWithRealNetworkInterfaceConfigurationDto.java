/*_##########################################################################
  _##
  _##  Copyright (C) 2012  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dto;

import java.io.Serializable;
import com.github.kaitoy.sneo.giane.model.RealNetworkInterface;
import com.github.kaitoy.sneo.giane.model.RealNetworkInterfaceConfiguration;

public class RealNetworkInterfaceWithRealNetworkInterfaceConfigurationDto implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -5103147767597714532L;

  private Integer id;
  private String name;
  private String realNetworkInterfaceConfiguration;

  public RealNetworkInterfaceWithRealNetworkInterfaceConfigurationDto(
    RealNetworkInterface model, RealNetworkInterfaceConfiguration conf
  ) {
    this.id = model.getId();
    this.name = model.getName();
    if (conf != null) {
      this.realNetworkInterfaceConfiguration = conf.getName();
    }
    else {
      this.realNetworkInterfaceConfiguration = null;
    }
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

  public String getRealNetworkInterfaceConfiguration() {
    return realNetworkInterfaceConfiguration;
  }

  public void setRealNetworkInterfaceConfiguration(
    String realNetworkInterfaceConfiguration
  ) {
    this.realNetworkInterfaceConfiguration = realNetworkInterfaceConfiguration;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) { return true; }
    if (!this.getClass().isInstance(obj)) { return false; }
    return this.id.equals(((RealNetworkInterfaceWithRealNetworkInterfaceConfigurationDto)obj).getId());
  }

  @Override
  public int hashCode() {
    return id;
  }

}
