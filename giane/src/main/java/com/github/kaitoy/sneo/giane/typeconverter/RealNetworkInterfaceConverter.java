/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.typeconverter;

import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

import com.github.kaitoy.sneo.giane.model.RealNetworkInterface;
import com.opensymphony.xwork2.conversion.TypeConversionException;

/**
 * @author Kaito
 *
 */
public class RealNetworkInterfaceConverter extends StrutsTypeConverter {

  @SuppressWarnings("rawtypes")
  @Override
  public Object convertFromString(Map context, String[] values, Class toClass) {
    if (values[0].length() == 0) {
      return null;
    }

    try {
      RealNetworkInterface nif = new RealNetworkInterface();
      nif.setId(Integer.valueOf(values[0]));
      return nif;
    } catch (Exception e) {
      throw new TypeConversionException(e);
    }
  }

  @SuppressWarnings("rawtypes")
  @Override
  public String convertToString(Map context, Object o) {
    try {
      RealNetworkInterface nif = (RealNetworkInterface)o;
      return nif.getId().toString();
    } catch (Exception e) {
      throw new TypeConversionException(e);
    }
  }

}
