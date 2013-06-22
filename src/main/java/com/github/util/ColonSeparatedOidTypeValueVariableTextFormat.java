/*_##########################################################################
  _##
  _##  Copyright (C) 2011  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.util;


import java.text.ParseException;

import org.snmp4j.asn1.BER;
import org.snmp4j.smi.AbstractVariable;
import org.snmp4j.smi.AssignableFromString;
import org.snmp4j.smi.BitString;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.SMIConstants;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.util.OIDTextFormat;
import org.snmp4j.util.SimpleOIDTextFormat;

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
class ColonSeparatedOidTypeValueVariableTextFormat
implements SneoVariableTextFormat {

  private static final OIDTextFormat oidFormat = new SimpleOIDTextFormat();
  private static final String TYPE_NAME_4_OCTETSTRING_IN_HEX
    = "OCTET STRING HEX";
  private static final String SEPARATOR = ":";
  private static final ColonSeparatedOidTypeValueVariableTextFormat INSTANCE
    = new ColonSeparatedOidTypeValueVariableTextFormat();

  /**
   * Creates a simple variable text format.
   */
  private ColonSeparatedOidTypeValueVariableTextFormat() {}

  public static ColonSeparatedOidTypeValueVariableTextFormat getInstance() {
    return INSTANCE;
  }

  public void init() {}

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
    String valueString;
    String typeString = variable.getSyntaxString();

    switch (variable.getSyntax()) {
      case SMIConstants.SYNTAX_TIMETICKS :
        valueString = String.valueOf(((TimeTicks) variable).getValue());
        break;
      case SMIConstants.SYNTAX_OCTET_STRING :
        if (!is_PrintableOneLine(((OctetString)variable))) {
          typeString = TYPE_NAME_4_OCTETSTRING_IN_HEX;
          valueString = ((OctetString)variable).toHexString();
        }
        else {
          valueString = variable.toString();
        }
        break;
      case BER.BITSTRING:
        valueString = ((BitString)variable).toHexString();
        break;
      default :
        valueString = variable.toString();
        break;
    }

    if (withOID) {
      return oidFormat.format(instanceOID.getValue())
               + SEPARATOR + typeString + SEPARATOR + valueString;
    }
    else {
      return valueString;
    }
  }

  /**
   * This operation is not supported by {@link ColonSeparatedOidTypeValueVariableTextFormat}.
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
    else {
      throw new AssertionError("Never get here.");
    }
    return v;
  }

  /**
   * This operation is not supported by {@link ColonSeparatedOidTypeValueVariableTextFormat}.
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
  public Variable parse(OID classOrInstanceOID, String text) throws ParseException {
    throw new UnsupportedOperationException();
  }

  public VariableBinding parseVariableBinding(String text) throws ParseException {
    String[] tokens = text.split(SEPARATOR, 3);
    if (tokens.length != 3) {
      throw new ParseException("Invalid format: "+ text, 0);
    }

    OID oid = new OID(oidFormat.parse(tokens[0]));
    String typeName = tokens[1];
    String valueString = tokens[2];

    if (typeName.equals(TYPE_NAME_4_OCTETSTRING_IN_HEX)) {
      OctetString os
        = (OctetString)parse(SMIConstants.SYNTAX_OCTET_STRING, "");
      os.setValue(OctetString.fromHexString(valueString).getValue());
      return new VariableBinding(oid, os);
    }
    else if (typeName.equals(AbstractVariable.getSyntaxString(BER.BITSTRING))) {
      BitString bs = (BitString)parse(BER.BITSTRING, "");
      bs.setValue(BitString.fromHexString(valueString).getValue());
      return new VariableBinding(oid, bs);
    }
    else {
      return new VariableBinding(
        oid,
        parse(AbstractVariable.getSyntaxFromString(typeName), valueString)
      );
    }
  }

  private boolean is_PrintableOneLine(OctetString o) {
    if (!o.isPrintable()) {
      return false;
    }

    byte[] value = o.getValue();
    for (int i = 0; i < value.length; i++) {
      char c = (char)value[i];
      if (
           Character.isWhitespace(c)
        && ((c & 0xFF) != 0x09) && ((c & 0xFF) != 0x20)
      ) {
        return false;
      }
    }
    return true;
  }

}
