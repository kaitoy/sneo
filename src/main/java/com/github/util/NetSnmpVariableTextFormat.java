/*_##########################################################################
  _##
  _##  Copyright (C) 2011  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.util;


import java.net.InetAddress;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.snmp4j.asn1.BER;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.log.LogFactory;
import org.snmp4j.smi.AbstractVariable;
import org.snmp4j.smi.AssignableFromInteger;
import org.snmp4j.smi.AssignableFromLong;
import org.snmp4j.smi.AssignableFromString;
import org.snmp4j.smi.BitString;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Opaque;
import org.snmp4j.smi.SMIConstants;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.util.VariableTextFormat;
import static org.snmp4j.smi.SMIConstants.SYNTAX_INTEGER;
import static org.snmp4j.smi.SMIConstants.SYNTAX_OCTET_STRING;
import static org.snmp4j.smi.SMIConstants.SYNTAX_NULL;
import static org.snmp4j.smi.SMIConstants.SYNTAX_OBJECT_IDENTIFIER;
import static org.snmp4j.smi.SMIConstants.SYNTAX_IPADDRESS;
import static org.snmp4j.smi.SMIConstants.SYNTAX_COUNTER32;
import static org.snmp4j.smi.SMIConstants.SYNTAX_GAUGE32;
import static org.snmp4j.smi.SMIConstants.SYNTAX_TIMETICKS;
import static org.snmp4j.smi.SMIConstants.SYNTAX_OPAQUE;
import static org.snmp4j.smi.SMIConstants.SYNTAX_COUNTER64;
import static org.snmp4j.smi.SMIConstants.EXCEPTION_NO_SUCH_OBJECT;
import static org.snmp4j.smi.SMIConstants.EXCEPTION_NO_SUCH_INSTANCE;
import static org.snmp4j.smi.SMIConstants.EXCEPTION_END_OF_MIB_VIEW;


/**
 * The <code>ColonSeparatedOidTypeValueVariableTextFormat</code> implements a simple textual
 * representation for SNMP variables based on their type only.
 * No MIB information is used (can be used).
 *
 * @author Kaito Yamada
 * @version 1.00
 * @since 1.00
 */
public
class NetSnmpVariableTextFormat
implements SneoVariableTextFormat {

  private static final LogAdapter logger
    = LogFactory.getLogger(VariableTextFormat.class);

  private static final String OID_TYPE_SEPARATOR = " = ";
  private static final String TYPE_VALUE_SEPARATOR = ": ";
  private static final char HEX_SEPARATOR = ' ';
  private static final char NETWORK_ADDRESS_SEPARATOR = ':';

  private static final Map<String, Integer> SYNTAX_OF_TYPE_NAME
    = new HashMap<String, Integer>() ;
  private static final Map<Integer, String> TYPE_NAME_OF_SYNTAX
    = new HashMap<Integer, String>() ;

  private static final String REGEX_REGULAR_PATTERN;
  private static final String REGEX_EMPTY_STRING_PATTERN;
  private static final String REGEX_STRING_HEX_CONTINUATION_PATTERN;
  private static final String REGEX_BITS_CONTINUATION_PATTERN;
  private static final String REGEX_END_OF_MIB_VIEW_PATTERN;
  private static final String REGEX_NO_SUCH_INSTANCE_PATTERN;
  private static final String REGEX_NO_SUCH_OBJECT_PATTERN;
  private static final String REGEX_NULL_PATTERN;

  private static final int STRING_HEX_LENGTH_PER_LINE = 16 * 3; // ([\dA-F]{2}[ ]){16}
  private static final String STRING_HEX_LINE_SEPARATOR
    = System.getProperty("line.Separator", "\n");

  static {
    SYNTAX_OF_TYPE_NAME.put("INTEGER", SYNTAX_INTEGER);
    SYNTAX_OF_TYPE_NAME.put("STRING", SYNTAX_OCTET_STRING);
    SYNTAX_OF_TYPE_NAME.put("BITS", (int)BER.BITSTRING);
    SYNTAX_OF_TYPE_NAME.put("Hex-STRING", SYNTAX_OCTET_STRING);
    SYNTAX_OF_TYPE_NAME.put("Network Address", SYNTAX_IPADDRESS);
    SYNTAX_OF_TYPE_NAME.put("OID", SYNTAX_OBJECT_IDENTIFIER);
    SYNTAX_OF_TYPE_NAME.put("Timeticks", SYNTAX_TIMETICKS);
    SYNTAX_OF_TYPE_NAME.put("Counter32", SYNTAX_COUNTER32);
    SYNTAX_OF_TYPE_NAME.put("Counter64", SYNTAX_COUNTER64);
    SYNTAX_OF_TYPE_NAME.put("No more variables left in this MIB View (It is past the end of the MIB tree)", EXCEPTION_END_OF_MIB_VIEW);
    SYNTAX_OF_TYPE_NAME.put("Gauge32", SYNTAX_GAUGE32);
    SYNTAX_OF_TYPE_NAME.put("Unsigned32", SYNTAX_GAUGE32);
    SYNTAX_OF_TYPE_NAME.put("IpAddress", SYNTAX_IPADDRESS);
    SYNTAX_OF_TYPE_NAME.put("No Such Instance currently exists at this OID", EXCEPTION_NO_SUCH_INSTANCE);
    SYNTAX_OF_TYPE_NAME.put("No Such Object available on this agent at this OID", EXCEPTION_NO_SUCH_OBJECT);
    SYNTAX_OF_TYPE_NAME.put("NULL", SYNTAX_NULL);
    SYNTAX_OF_TYPE_NAME.put("Opaque", SYNTAX_OPAQUE);
    SYNTAX_OF_TYPE_NAME.put("OPAQUE", SYNTAX_OPAQUE);

    TYPE_NAME_OF_SYNTAX.put(SYNTAX_INTEGER, "INTEGER");
    TYPE_NAME_OF_SYNTAX.put((int)BER.BITSTRING, "BITS");
    TYPE_NAME_OF_SYNTAX.put(SYNTAX_OCTET_STRING, "STRING");
    TYPE_NAME_OF_SYNTAX.put(SYNTAX_OBJECT_IDENTIFIER, "OID");
    TYPE_NAME_OF_SYNTAX.put(SYNTAX_TIMETICKS, "Timeticks");
    TYPE_NAME_OF_SYNTAX.put(SYNTAX_COUNTER32, "Counter32");
    TYPE_NAME_OF_SYNTAX.put(SYNTAX_COUNTER64, "Counter64");
    TYPE_NAME_OF_SYNTAX.put(EXCEPTION_END_OF_MIB_VIEW, "No more variables left in this MIB View (It is past the end of the MIB tree)");
    TYPE_NAME_OF_SYNTAX.put(SYNTAX_GAUGE32, "Gauge32");
    TYPE_NAME_OF_SYNTAX.put(SYNTAX_IPADDRESS, "IpAddress");
    TYPE_NAME_OF_SYNTAX.put(EXCEPTION_NO_SUCH_INSTANCE, "No Such Instance currently exists at this OID");
    TYPE_NAME_OF_SYNTAX.put(EXCEPTION_NO_SUCH_OBJECT, "No Such Object available on this agent at this OID");
    TYPE_NAME_OF_SYNTAX.put(SYNTAX_NULL, "NULL");
    TYPE_NAME_OF_SYNTAX.put(SYNTAX_OPAQUE, "OPAQUE");

    StringBuilder sb = new StringBuilder();
    sb.append("(");
    for (String name: SYNTAX_OF_TYPE_NAME.keySet()) {
      sb.append(Pattern.quote(name));
      sb.append("|");
    }
    sb.deleteCharAt(sb.lastIndexOf("|"));
    sb.append(")");

    REGEX_REGULAR_PATTERN
      = "([.]\\d+)+" + Pattern.quote(OID_TYPE_SEPARATOR)
          + sb.toString() + Pattern.quote(TYPE_VALUE_SEPARATOR) + ".*";
    REGEX_EMPTY_STRING_PATTERN
      = "([.]\\d+)+" + Pattern.quote(OID_TYPE_SEPARATOR) + "\"\"";
    REGEX_STRING_HEX_CONTINUATION_PATTERN
      = "([\\dA-F]{2} )+";
    REGEX_BITS_CONTINUATION_PATTERN
      = "([\\dA-F]{2} )+(\\d+ )*";
    REGEX_END_OF_MIB_VIEW_PATTERN
      = "([.]\\d+)+" + Pattern.quote(OID_TYPE_SEPARATOR)
          + Pattern.quote(TYPE_NAME_OF_SYNTAX.get(EXCEPTION_END_OF_MIB_VIEW));
    REGEX_NO_SUCH_INSTANCE_PATTERN
      = "([.]\\d+)+" + Pattern.quote(OID_TYPE_SEPARATOR)
          + Pattern.quote(TYPE_NAME_OF_SYNTAX.get(EXCEPTION_NO_SUCH_INSTANCE));
    REGEX_NO_SUCH_OBJECT_PATTERN
      = "([.]\\d+)+" + Pattern.quote(OID_TYPE_SEPARATOR)
          + Pattern.quote(TYPE_NAME_OF_SYNTAX.get(EXCEPTION_NO_SUCH_OBJECT));
    REGEX_NULL_PATTERN
      = "([.]\\d+)+" + Pattern.quote(OID_TYPE_SEPARATOR)
          + Pattern.quote(TYPE_NAME_OF_SYNTAX.get(SYNTAX_NULL));
  }

  private VariableBinding prevStringHexVarBind = null;
  private VariableBinding prevStringVarBind = null;
  private VariableBinding prevBitsVarBind = null;

  /**
   * Creates a simple variable text format.
   */
  private NetSnmpVariableTextFormat() {}

  public static NetSnmpVariableTextFormat getInstance() {
    return new NetSnmpVariableTextFormat();
  }

  public void init() {
    prevStringHexVarBind = null;
    prevStringVarBind = null;
    prevBitsVarBind = null;
  }


  /**
   * Returns a textual representation of the supplied variable against the
   * optionally supplied instance OID.
   *
   * @param instanceOID the instance OID <code>variable</code> is associated
   *   with. If <code>null</code> the formatting cannot take any MIB
   *   specification of the variable into account and has to format it based
   *   on its type only.
   * @param variable
   *    the variable to format.
   * @param withOID
   *    if <code>true</code> the <code>instanceOID</code> should be included
   *    in the textual representation to form a {@link VariableBinding}
   *    representation.
   * @return the textual representation.
   */
  public String format(OID instanceOID, Variable variable, boolean withOID) {
    Integer syntax = variable.getSyntax();
    if (syntax == null) {
      throw new AssertionError("Never get here.");
    }

    String valueString;
    String typeName = TYPE_NAME_OF_SYNTAX.get(syntax);

    if (syntax.intValue() == SYNTAX_OCTET_STRING) {
      if (!is_Printable(((OctetString)variable))) {
        typeName = "Hex-STRING";
        valueString = formatHexStream((OctetString)variable);
      }
      else {
        valueString
          = "\"" + variable.toString().replaceAll("(\"|\\\\)", "\\\\$1") + "\"";
      }
    }
    else if (syntax.intValue() == SYNTAX_OPAQUE) {
      valueString = formatHexStream((Opaque)variable);
    }
    else if (syntax.byteValue() == BER.BITSTRING) {
      StringBuilder sb = new StringBuilder();
      sb.append(formatHexStream(((BitString)variable)));

      byte[] octets = ((BitString)variable).getValue();
      for (int octetIdx = 0; octetIdx < octets.length; octetIdx++) {
        int octet = 0xFF & octets[octetIdx];

        int mask = 128;
        for (int bitIdx = 0; bitIdx < 8; bitIdx++) {
          if ((octet & mask) != 0) {
            sb.append(octetIdx * 8 + bitIdx)
              .append(HEX_SEPARATOR);
          }
          mask >>= 1;
        }
      }

      valueString = sb.toString();
    }
    else if (syntax.intValue() == SYNTAX_TIMETICKS) {
      long LongValue = ((TimeTicks)variable).toLong();
      long workLongValue = LongValue;

      long hours = workLongValue / 360000L;
      workLongValue %= 360000;
      long minutes = workLongValue / 6000L;
      workLongValue %= 6000;
      long seconds = workLongValue / 100L;
      workLongValue %= 100;
      long hseconds = workLongValue;

      valueString =
        new StringBuilder()
          .append("(").append(LongValue).append(") ")
          .append(hours).append(":")
          .append(String.format("%02d", minutes)).append(":")
          .append(String.format("%02d", seconds)).append(".")
          .append(String.format("%02d", hseconds))
          .toString();
    }
    else if (syntax.intValue() == SYNTAX_OBJECT_IDENTIFIER) {
      valueString
        = SimpleOIDLeadByDotTextFormat.getInstance()
            .format(((OID)variable).getValue());
    }
    else if (syntax.intValue() == EXCEPTION_END_OF_MIB_VIEW) {
      valueString = null;
    }
    else if (syntax.intValue() == EXCEPTION_NO_SUCH_INSTANCE) {
      valueString = null;
    }
    else if (syntax.intValue() == EXCEPTION_NO_SUCH_OBJECT) {
      valueString = null;
    }
    else if (syntax.intValue() == SYNTAX_NULL) {
      valueString = null;
    }
    else if (syntax.intValue() == SYNTAX_IPADDRESS) {
      InetAddress addr = ((IpAddress)variable).getInetAddress();
      valueString = addr.toString().replaceAll(".*/", "");
    }
    else if (variable instanceof AssignableFromLong) {
      valueString
        = Long.toString(((AssignableFromLong)variable).toLong());
    }
    else if (variable instanceof AssignableFromInteger) {
      valueString
        = Integer.toString(((AssignableFromInteger)variable).toInt());
    }
    else {
      valueString = variable.toString();
    }

    if (withOID) {
      StringBuilder sb = new StringBuilder();

      if (typeName.equals("STRING") && valueString.equals("\"\"")) {
        sb.append(SimpleOIDLeadByDotTextFormat.getInstance().format(instanceOID.getValue()))
          .append(OID_TYPE_SEPARATOR).append(valueString);
      }
      else {
        sb.append(SimpleOIDLeadByDotTextFormat.getInstance().format(instanceOID.getValue()))
          .append(OID_TYPE_SEPARATOR).append(typeName);
        if (valueString != null) {
          sb.append(TYPE_VALUE_SEPARATOR ).append(valueString);
        }
      }

      return sb.toString();
    }
    else {
      return valueString;
    }
  }

  private String formatHexStream(OctetString o) {
    String hexString
      = o.toHexString(HEX_SEPARATOR).toUpperCase() + HEX_SEPARATOR;

    if (hexString.length() > STRING_HEX_LENGTH_PER_LINE) {
      StringBuilder sb = new StringBuilder();

      int i;
      for (
        i = 0;
        i + STRING_HEX_LENGTH_PER_LINE < hexString.length();
        i += STRING_HEX_LENGTH_PER_LINE
      ) {
        sb.append(hexString.substring(i, i + STRING_HEX_LENGTH_PER_LINE))
          .append(STRING_HEX_LINE_SEPARATOR);
      }
      sb.append(hexString.substring(i));

      return sb.toString();
    }
    else {
      return hexString;
    }
  }

  /**
   * This operation is not supported by {@link NetSnmpVariableTextFormat}.
   *
   * @param smiSyntax the SMI syntax identifier identifying the target
   *   <code>Variable</code>.
   * @param text a textual representation of the variable.
   * @return the new <code>Variable</code> instance.
   * @throws ParseException if the variable cannot be parsed successfully.
   */
  public Variable parse(int syntax, String value) {
    Variable v = AbstractVariable.createFromSyntax(syntax);
    if (v instanceof AssignableFromString) {
      ((AssignableFromString)v).setValue(value);
    }
    else if (syntax == SMIConstants.SYNTAX_NULL) {
      // Do nothing
    }
    else {
      throw new AssertionError("Never get here.");
    }
    return v;
  }

  /**
   * This operation is not supported by {@link NetSnmpVariableTextFormat}.
   *
   * @param classOrInstanceOID
   *    the instance OID <code>variable</code> is associated with. Must not
   *    be <code>null</code>.
   * @param text
   *    a textual representation of the variable.
   * @return
   *    the new <code>Variable</code> instance.
   * @throws ParseException
   *    if the variable cannot be parsed successfully.
   */
  public
  Variable parse(OID classOrInstanceOID, String text) throws ParseException {
    throw new UnsupportedOperationException();
  }

  public
  VariableBinding parseVariableBinding(String text) throws ParseException {
    OID oid;
    String typeName;
    String valueString;

    if (prevStringVarBind != null) {
      Pattern p = Pattern.compile("([^\\\\]|\\A)((\\\\){2})*\"\\Z");
      Matcher m = p.matcher(text);
      if (m.find()) {
        OctetString os = (OctetString)prevStringVarBind.getVariable();
        os.append(
          text.substring(0, text.length() - 1).replaceAll("\\\\(.)", "$1")
        );

        VariableBinding vb = prevStringVarBind;
        prevStringVarBind = null;
        return vb;
      }
      else {
        OctetString os = (OctetString)prevStringVarBind.getVariable();
        os.append(text.replaceAll("\\\\(.)", "$1"));
        os.append("\n"); // TODO もとの改行コードを復元する
        return prevStringVarBind;
      }
    }
    else if (prevStringHexVarBind != null) {
      if (text.matches(REGEX_STRING_HEX_CONTINUATION_PATTERN)) {
        try {
          OctetString os = (OctetString)prevStringHexVarBind.getVariable();
          os.append(OctetString.fromHexString(text, HEX_SEPARATOR).getValue());
          return prevStringHexVarBind;
        } catch (NumberFormatException e) {
          throw new ParseException("Invalid format: "+ text, 0);
        }
      }
    }
    else if (prevBitsVarBind != null) {
      if (text.matches(REGEX_BITS_CONTINUATION_PATTERN)) {
        try {
          prevBitsVarBind
            .setVariable(
              parseBits(
                ((BitString)prevBitsVarBind.getVariable())
                  .toHexString(HEX_SEPARATOR)
                    + HEX_SEPARATOR
                    + text
              )
            );
          return prevBitsVarBind;
        } catch (ParseException e) {
          throw new ParseException("Invalid format: "+ text, 0);
        }
      }
    }

    if (text.matches(REGEX_REGULAR_PATTERN)) {
      String[] tokens = text.split(OID_TYPE_SEPARATOR, 2);
      if (tokens.length != 2) {
        throw new AssertionError("Never get here");
      }

      oid
        = new OID(
            SimpleOIDLeadByDotTextFormat.getInstance().parse(tokens[0])
          );

      tokens = tokens[1].split(TYPE_VALUE_SEPARATOR, 2);
      if (tokens.length != 2) {
        throw new AssertionError("Never get here");
      }

      typeName = tokens[0];
      valueString = tokens[1];
    }
    else if (text.matches(REGEX_EMPTY_STRING_PATTERN)) {
      String[] tokens = text.split(OID_TYPE_SEPARATOR, 2);
      if (tokens.length != 2) {
        throw new AssertionError("Never get here");
      }

      oid
        = new OID(
            SimpleOIDLeadByDotTextFormat.getInstance().parse(tokens[0])
          );
      typeName = "STRING";
      valueString = "\"\"";
    }
    else if (text.matches(REGEX_END_OF_MIB_VIEW_PATTERN)) {
      return null;
    }
    else if (text.matches(REGEX_NO_SUCH_INSTANCE_PATTERN)) {
      return null;
    }
    else if (text.matches(REGEX_NO_SUCH_OBJECT_PATTERN)) {
      return null;
    }
    else if (text.matches(REGEX_NULL_PATTERN)) {
      String[] tokens = text.split(OID_TYPE_SEPARATOR, 2);
      if (tokens.length != 2) {
        throw new AssertionError("Never get here");
      }

      oid
        = new OID(
            SimpleOIDLeadByDotTextFormat.getInstance().parse(tokens[0])
          );
      typeName = tokens[1];
      valueString = null;
    }
    else {
      throw new ParseException("Invalid format: "+ text, 0);
    }

    VariableBinding vb;
    if (typeName.equals("Hex-STRING")) {
      OctetString os
        = (OctetString)AbstractVariable.createFromSyntax(SYNTAX_OCTET_STRING);
      os.setValue(
        OctetString.fromHexString(valueString, HEX_SEPARATOR).getValue()
      );
      vb = new VariableBinding(oid, os);
    }
    else if (typeName.equals("STRING")) {
      if (!valueString.startsWith("\"")) {
        throw new ParseException("Invalid format: "+ text, 0);
      }

      OctetString os
        = (OctetString)AbstractVariable.createFromSyntax(SYNTAX_OCTET_STRING);

      Pattern p = Pattern.compile("[^\\\\]((\\\\){2})*\"\\Z");
      Matcher m = p.matcher(valueString);
      if (valueString.length() > 1 && m.find()) {
        os.setValue(
          valueString
            .substring(1, valueString.length() - 1)
              .replaceAll("\\\\(.)", "$1")
        );
        vb = new VariableBinding(oid, os);
      }
      else {
        os.setValue(valueString.substring(1).replaceAll("\\\\(.)", "$1"));
        os.append("\n"); // TODO もとの改行コードを復元する
        vb = new VariableBinding(oid, os);
        prevStringVarBind = vb;
      }
    }
    else if (typeName.equals("BITS")) {
      try {
        vb = new VariableBinding(oid, parseBits(valueString));
      } catch (ParseException e) {
        throw new ParseException("Invalid format: "+ text, 0);
      }
    }
    else if (typeName.equals("Timeticks")) {
      vb = new VariableBinding(
             oid,
             parse(
               SYNTAX_OF_TYPE_NAME.get(typeName),
               valueString.substring(
                 valueString.indexOf('(') + 1,
                 valueString.indexOf(')')
               )
             )
           );
    }
    else if (typeName.equals("Network Address")) {
      IpAddress ipAddr
        = (IpAddress)AbstractVariable.createFromSyntax(SYNTAX_IPADDRESS);
      ipAddr.setValue(
        OctetString.fromHexString(
          valueString, NETWORK_ADDRESS_SEPARATOR
        ).getValue()
      );
      vb = new VariableBinding(oid, ipAddr);
    }
    else if (typeName.equals("Opaque")) {
      logger.warn("The result of parsing Opaque may be incorrect: " + text);
      vb = new VariableBinding(
             oid,
             parse(SYNTAX_OF_TYPE_NAME.get(typeName), valueString)
           );
    }
    else {
      vb = new VariableBinding(
             oid,
             parse(SYNTAX_OF_TYPE_NAME.get(typeName), valueString)
           );
    }

    prevStringHexVarBind = typeName.equals("Hex-STRING") ? vb : null;
    prevBitsVarBind = typeName.equals("BITS") ? vb : null;

    return vb;
  }

  private boolean is_Printable(OctetString o) {
    for (byte b: o.getValue()) {
      switch(b) {
        case 0x09: // HT
        case 0x0a: // LF
        case 0x0b: // VT
        case 0x0c: // NP(New Page)
        case 0x0d: // CR
          continue;
        default:
          if (b < 0x20 || b > 0x7e) {
            return false;
          }
      }
    }
    return true;
  }

  private BitString parseBits(String valueString) throws ParseException {
    String[] strOctets = valueString.split(String.valueOf(HEX_SEPARATOR));

    StringBuilder valuePattern = new StringBuilder();
    StringBuilder parsedOctets = new StringBuilder();
    boolean match = false;
    for (int octIdx = 0; octIdx < strOctets.length; octIdx++) {
      int octet;
      try {
        octet = Integer.parseInt(strOctets[octIdx], 16);
        parsedOctets.append(strOctets[octIdx]).append(HEX_SEPARATOR);
      } catch (NumberFormatException e) {
        throw
          new ParseException("Invalid value for BITS: "+ valueString, octIdx);
      }

      int mask = 128;
      for (int bitIdx = 0; bitIdx < 8; bitIdx++) {
        if ((octet & mask) != 0) {
          valuePattern.append(octIdx * 8 + bitIdx).append(HEX_SEPARATOR);
        }
        mask >>= 1;
      }


      if (
        valueString.matches(parsedOctets + "(00 )*" + valuePattern)
      ) {
        match = true;
        break;
      }
    }

    BitString bs
      = (BitString)AbstractVariable.createFromSyntax(BER.ASN_BIT_STR);
    if (match) {
      bs.setValue(
        OctetString.fromHexString(
          valueString.substring(
            0,
            valueString.lastIndexOf(valuePattern.toString())
          ),
          HEX_SEPARATOR
        ).getValue()
      );
    }
    else {
      bs.setValue(
        OctetString.fromHexString(
          valueString,
          HEX_SEPARATOR
        ).getValue()
      );
    }

    return bs;
  }
}
