/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model;

import com.github.kaitoy.sneo.util.ColonSeparatedOidTypeValueVariableTextFormat;
import com.github.kaitoy.sneo.util.NetSnmpVariableTextFormat;
import com.github.kaitoy.sneo.util.SneoVariableTextFormat;

public enum FileMibFormat {

  DEFAULT(ColonSeparatedOidTypeValueVariableTextFormat.getInstance()),
  NET_SNMP(NetSnmpVariableTextFormat.getInstance());

  private SneoVariableTextFormat format;

  private FileMibFormat(SneoVariableTextFormat format) {
    this.format = format;
  }

  public SneoVariableTextFormat getFormat() {
    return format;
  }

}
