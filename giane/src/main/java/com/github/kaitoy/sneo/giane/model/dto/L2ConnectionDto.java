/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dto;

import java.io.Serializable;
import com.github.kaitoy.sneo.giane.model.L2Connection;

public class L2ConnectionDto implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 7340257430856333913L;

  private Integer id;
  private String name;

  public L2ConnectionDto(L2Connection model) {
    this.id = model.getId();
    this.name = model.getName();
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
    return this.id.equals(((L2ConnectionDto)obj).getId());
  }

  @Override
  public int hashCode() {
    return id;
  }

}
