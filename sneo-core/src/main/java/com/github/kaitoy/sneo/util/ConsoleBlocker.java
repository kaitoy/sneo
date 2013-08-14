package com.github.kaitoy.sneo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleBlocker {

  private ConsoleBlocker() { throw new AssertionError(); }

  public static void block() {
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e1) {}

    BufferedReader r = null;

    try {
      r = new BufferedReader(new InputStreamReader(System.in));
      System.out.println("** Hit Enter key to stop simulation **");
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
