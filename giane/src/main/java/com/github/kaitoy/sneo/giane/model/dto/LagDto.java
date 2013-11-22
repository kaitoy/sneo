/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dto;

import java.io.Serializable;
import com.github.kaitoy.sneo.giane.model.Lag;

public class LagDto implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 8773235969210498771L;

  private Integer id;
  private String name;
  private Integer channelGroupNumber;

  public LagDto(Lag model) {
    this.id = model.getId();
    this.name = model.getName();
    this.setChannelGroupNumber(model.getChannelGroupNumber());
  }

  public Integer getId() { return id; }

  public void setId(Integer id) { this.id = id; }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getChannelGroupNumber() {
    return channelGroupNumber;
  }

  public void setChannelGroupNumber(Integer channelGroupNumber) {
    this.channelGroupNumber = channelGroupNumber;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) { return true; }
    if (!this.getClass().isInstance(obj)) { return false; }
    return this.id.equals(((LagDto)obj).getId());
  }

  @Override
  public int hashCode() {
    return id;
  }

}
