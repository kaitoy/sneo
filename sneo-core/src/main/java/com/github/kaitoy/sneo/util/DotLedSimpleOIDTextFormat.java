/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2017  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.util;

import java.text.ParseException;

import org.snmp4j.util.OIDTextFormat;
import org.snmp4j.util.SimpleOIDTextFormat;

public class DotLedSimpleOIDTextFormat implements OIDTextFormat {

  private static final DotLedSimpleOIDTextFormat INSTANCE
    = new DotLedSimpleOIDTextFormat();

  private final SimpleOIDTextFormat SIMPLE_FORMAT
    = new SimpleOIDTextFormat();

  private DotLedSimpleOIDTextFormat() {}

  public static DotLedSimpleOIDTextFormat getInstance() {
    return INSTANCE;
  }

  @Override
  public String format(int[] value) {
    String simple = SIMPLE_FORMAT.format(value);
    if (simple.startsWith(".")) {
      return simple;
    }
    else {
      return "." + simple;
    }
  }

  @Override
  public String formatForRoundTrip(int[] value) {
    return format(value);
  }

  @Override
  public int[] parse(String text) throws ParseException {
    return SIMPLE_FORMAT.parse(text);
  }

}