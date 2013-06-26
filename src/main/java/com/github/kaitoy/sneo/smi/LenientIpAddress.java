/*_##########################################################################
  _##
  _##  Copyright (C) 2011  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.smi;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.snmp4j.asn1.BER;
import org.snmp4j.asn1.BERInputStream;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.log.LogFactory;
import org.snmp4j.smi.AbstractVariable;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OID;

public class LenientIpAddress extends IpAddress {

  private static final long serialVersionUID = 2737191780491371834L;
  private static final LogAdapter logger =
    LogFactory.getLogger(AbstractVariable.class);

  private byte[] rawValue;

  public LenientIpAddress() {
    super();
    this.rawValue = getInetAddress().getAddress();
  }

  public LenientIpAddress(InetAddress address) {
    super(address);
    this.rawValue = getInetAddress().getAddress();
  }

  public LenientIpAddress(String address) {
    setValue(address);
  }

  public LenientIpAddress(byte[] addressBytes) {
    setAddress(addressBytes);
  }

  public String toString() {
    if(isValid()) {
      return super.toString();
    }
    else {
      return joinBytesWithDot(rawValue);
    }
  }

  public int hashCode() {
    if(isValid()) {
      return super.hashCode();
    }
    if (rawValue != null) {
      return rawValue.hashCode();
    }
    return 0;
  }

  public int compareTo(Object o) {
    if (isValid()) {
      return super.compareTo(o);
    }
    else {
      return toString().compareTo(((LenientIpAddress)o).toString());
    }
  }

  public void decodeBER(BERInputStream inputStream) throws IOException {
    BER.MutableByte type = new BER.MutableByte();
    byte[] value = BER.decodeString(inputStream, type);
    if (type.getValue() != BER.IPADDRESS) {
      throw new IOException(
              "Wrong type encountered when decoding Counter: "
                + type.getValue()
            );
    }
    if (value.length != 4) {
      logger.warn(
        "IpAddress encoding is invalid, wrong length: " + value.length
      );
      super.setInetAddress(null);
      rawValue = value;
    }
    else {
      setAddress(value);
    }
  }

  public void encodeBER(OutputStream outputStream) throws IOException {
    if (isValid()) {
      super.encodeBER(outputStream);
    }
    else {
      logger.warn("Encode invalid IpAddress: " + joinBytesWithDot(rawValue));
      BER.encodeString(outputStream, BER.IPADDRESS, rawValue);
    }
  }

  public int getBERLength() {
    if (isValid()) {
      return super.getBERLength();
    }
    else {
      return rawValue.length + BER.getBERLengthOfLength(rawValue.length) + 1;
    }
  }

  public void setAddress(byte[] rawVal) {
    try {
      super.setAddress(rawVal);
    }
    catch (UnknownHostException ex) {
      logger.warn("Invalid value for IpAddress: " + joinBytesWithDot(rawVal));
      super.setInetAddress(null);
    }

    rawValue = rawVal;
  }

  public void setInetAddress(java.net.InetAddress inetAddress) {
    super.setInetAddress(inetAddress);

    if (inetAddress != null) {
      rawValue = inetAddress.getAddress();
    }
    else {
      rawValue = null;
    }
  }

  public Object clone() {
    return new LenientIpAddress(rawValue);
  }

  public OID toSubIndex(boolean impliedLength) {
    if (isValid()) {
      return super.toSubIndex(impliedLength);
    }
    else {
      return new OID(toString());
    }
  }

  public void fromSubIndex(OID subIndex, boolean impliedLength) {
    setAddress(subIndex.toByteArray());
  }

  public void setValue(String value) {
    if (parseAddress(value)) {
      rawValue = getInetAddress().getAddress();
    }
    else {
      try {
        String[] tokens = value.split("\\.");
        byte[] rawVal = new byte[tokens.length];

        for (int i = 0; i < tokens.length; i++) {
          rawVal[i] = Byte.parseByte(tokens[i]);
        }
        rawValue = rawVal;
      }
      catch (NumberFormatException e) {
        throw new IllegalArgumentException(value + " cannot be parsed by "
          + getClass().getName());
      }
    }
  }

  public void setValue(byte[] value) {
    setAddress(value);
  }

  public byte[] toByteArray() {
    return rawValue;
  }

  public byte[] getRawValue() {
    return rawValue;
  }

  private String joinBytesWithDot(byte[] bytes) {
    if (bytes == null) {
      return "";
    }

    StringBuffer buf = new StringBuffer(bytes.length * 2);
    for (int i = 0; i < bytes.length - 1; i++) {
      buf.append(Byte.toString(bytes[i]));
      buf.append(".");
    }
    buf.append(Byte.toString(bytes[bytes.length - 1]));

    return buf.toString();
  }
}
