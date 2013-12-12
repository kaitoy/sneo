/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleBlocker {

  private ConsoleBlocker() { throw new AssertionError(); }

  public static void block(String message) {
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e1) {}

    BufferedReader r = null;

    try {
      r = new BufferedReader(new InputStreamReader(System.in));
      System.out.println(message);
      r.readLine();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (r != null) {
          r.close();
        }
      } catch (IOException e) {}
    }
  }

}
