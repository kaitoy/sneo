/*_##########################################################################
  _##
  _##  Copyright (C) 2013  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dto;

import com.github.kaitoy.sneo.giane.model.AdditionalIpV4Route;

public class AdditionalIpV4RouteDto extends IpV4RouteDto {

  /**
   *
   */
  private static final long serialVersionUID = 4900077162076357354L;

  private String name;

  public AdditionalIpV4RouteDto(AdditionalIpV4Route model) {
    super(model);
    this.name = model.getName();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
