/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dto;

import java.io.Serializable;

import com.github.kaitoy.sneo.giane.model.IpAddress;

public class IpAddressDto implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 4278317055245966206L;

  private Integer id;
  private String address;
  private Integer prefixLength;

  public IpAddressDto(IpAddress model) {
    this.id = model.getId();
    this.address = model.getAddress();
    this.prefixLength = model.getPrefixLength();
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Integer getPrefixLength() {
    return prefixLength;
  }

  public void setPrefixLength(Integer prefixLength) {
    this.prefixLength = prefixLength;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) { return true; }
    if (!this.getClass().isInstance(obj)) { return false; }
    return this.id == ((IpAddressDto)obj).getId();
  }

  @Override
  public int hashCode() {
    return id;
  }

}
