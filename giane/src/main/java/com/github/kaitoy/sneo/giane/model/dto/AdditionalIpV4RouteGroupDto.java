/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dto;

import java.io.Serializable;
import com.github.kaitoy.sneo.giane.model.AdditionalIpV4RouteGroup;

public class AdditionalIpV4RouteGroupDto implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 2965774844551306441L;

  private Integer id;
  private String name;
  private String descr;

  public AdditionalIpV4RouteGroupDto(AdditionalIpV4RouteGroup model) {
    this.id = model.getId();
    this.name = model.getName();
    this.descr = model.getDescr();
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
    return this.id.equals(((AdditionalIpV4RouteGroupDto)obj).getId());
  }

  @Override
  public int hashCode() {
    return id;
  }

}
