
package com.github.kaitoy.util;

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

  public String format(int[] value) {
    String simple = SIMPLE_FORMAT.format(value);
    if (simple.startsWith(".")) {
      return simple;
    }
    else {
      return "." + simple;
    }
  }

  public int[] parse(String text) throws ParseException {
    return SIMPLE_FORMAT.parse(text);
  }

}