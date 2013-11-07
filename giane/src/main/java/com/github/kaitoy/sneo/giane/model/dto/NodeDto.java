/*_##########################################################################
  _##
  _##  Copyright (C) 2013  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dto;

import java.io.Serializable;
import com.github.kaitoy.sneo.giane.model.Node;

public class NodeDto implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 7125712645831526494L;

  private Integer id;
  private String name;
  private Integer ttl;
  private String descr;

  public NodeDto(Node model) {
    this.id = model.getId();
    this.name = model.getName();
    this.ttl = model.getTtl();
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

  public Integer getTtl() {
    return ttl;
  }

  public void setTtl(Integer ttl) {
    this.ttl = ttl;
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
    return this.id.equals(((NodeDto)obj).getId());
  }

  @Override
  public int hashCode() {
    return id;
  }

}
