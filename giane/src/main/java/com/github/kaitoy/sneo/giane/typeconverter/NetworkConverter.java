/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.typeconverter;

import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

import com.github.kaitoy.sneo.giane.model.Network;
import com.opensymphony.xwork2.conversion.TypeConversionException;

/**
 * @author Kaito
 *
 */
public class NetworkConverter extends StrutsTypeConverter {

  @SuppressWarnings("rawtypes")
  @Override
  public Object convertFromString(Map context, String[] values, Class toClass) {
    try {
      Network network = new Network();
      network.setId(Integer.valueOf(values[0]));
      return network;
    } catch (Exception e) {
      throw new TypeConversionException(e);
    }
  }

  @SuppressWarnings("rawtypes")
  @Override
  public String convertToString(Map context, Object o) {
    try {
      Network network = (Network)o;
      return network.getId().toString();
    } catch (Exception e) {
      throw new TypeConversionException(e);
    }
  }

}
