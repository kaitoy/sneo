/*_##########################################################################
  _##
  _##  Copyright (C) 2013  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dto;

import com.github.kaitoy.sneo.giane.model.RealNetworkInterface;

public class RealNetworkInterfaceDto extends VlanMemberDto {

  /**
   *
   */
  private static final long serialVersionUID = 2406472931850361349L;

  public RealNetworkInterfaceDto(RealNetworkInterface model) {
    super(model);
  }

}
