/*_##########################################################################
  _##
  _##  Copyright (C) 2011  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.util;

import java.io.Serializable;

public class MutableInteger
implements Comparable<MutableInteger>, Serializable  {

  /**
   *
   */
  private static final long serialVersionUID = 632002492821931062L;

  private int value;

  public MutableInteger(int value) {
    this.value = value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public MutableInteger increment() {
    value++;
    return this;
  }

  public MutableInteger decrement() {
    value--;
    return this;
  }

  public MutableInteger add(int val) {
    value += val;
    return this;
  }

  public MutableInteger sub(int val) {
    value -= val;
    return this;
  }

  public MutableInteger mul(int val) {
    value *= val;
    return this;
  }

  public MutableInteger dev(int val) {
    value /= val;
    return this;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) { return true; }
    if (!this.getClass().isInstance(obj)) { return false; }

    return value == ((MutableInteger)obj).getValue();
  }

  @Override
  public int hashCode() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  public int compareTo(MutableInteger other) {
    return this.getValue() - other.getValue();
  }

}

