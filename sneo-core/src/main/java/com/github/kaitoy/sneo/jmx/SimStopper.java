/*_##########################################################################
  _##
  _##  Copyright (C) 2015  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.jmx;

public final class SimStopper {

  private final StopProcedure procedure;

  public SimStopper(StopProcedure procedure) {
    this.procedure = procedure;
  }

  public void stop() {
    procedure.stop();
  }

  public static interface StopProcedure {

    public void stop();

  }

}
