/*_##########################################################################
  _##
  _##  Copyright (C) 2011  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.agent.mo;


import org.snmp4j.smi.Variable;


public interface VariableServer {

  public Variable get();

}
