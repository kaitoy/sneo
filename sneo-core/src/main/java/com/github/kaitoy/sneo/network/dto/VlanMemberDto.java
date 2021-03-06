/*_##########################################################################
  _##
  _##  Copyright (C) 2013  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.network.dto;

import java.io.Serializable;

public interface VlanMemberDto extends Serializable  {

  public Integer getId();

  public String getName();

  public boolean isTrunk();

}
