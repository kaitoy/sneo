/*_##########################################################################
  _##
  _##  Copyright (C) 2013  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dto;

import com.github.kaitoy.sneo.giane.model.AdditionalIpV6Route;


public class AdditionalIpV6RouteDto extends IpV6RouteDto {

  /**
   *
   */
  private static final long serialVersionUID = 2288894736594322275L;

  private String name;
  private String descr;

  public AdditionalIpV6RouteDto(AdditionalIpV6Route model) {
    super(model);
    this.name = model.getName();
    this.descr = model.getDescr();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescr() {
    return descr;
  }

  public void setDescr(String descr) {
    this.descr = descr;
  }

}
