/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.typeconverter;

import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

import com.github.kaitoy.sneo.giane.model.AdditionalIpV4RouteGroup;
import com.opensymphony.xwork2.conversion.TypeConversionException;

/**
 * @author Kaito
 *
 */
public class AdditionalIpV4RouteGroupConverter extends StrutsTypeConverter {

  @SuppressWarnings("rawtypes")
  @Override
  public Object convertFromString(Map context, String[] values, Class toClass) {
    if (values[0].length() == 0) {
      return null;
    }

    try {
      AdditionalIpV4RouteGroup routeg = new AdditionalIpV4RouteGroup();
      routeg.setId(Integer.valueOf(values[0]));
      return routeg;
    } catch (Exception e) {
      throw new TypeConversionException(e);
    }
  }

  @SuppressWarnings("rawtypes")
  @Override
  public String convertToString(Map context, Object o) {
    try {
      AdditionalIpV4RouteGroup routeg = (AdditionalIpV4RouteGroup)o;
      return routeg.getId().toString();
    } catch (Exception e) {
      throw new TypeConversionException(e);
    }
  }

}
