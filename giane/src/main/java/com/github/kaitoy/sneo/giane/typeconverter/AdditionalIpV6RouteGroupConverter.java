/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.typeconverter;

import java.util.Map;
import org.apache.struts2.util.StrutsTypeConverter;
import com.github.kaitoy.sneo.giane.model.AdditionalIpV6RouteGroup;
import com.opensymphony.xwork2.conversion.TypeConversionException;

public class AdditionalIpV6RouteGroupConverter extends StrutsTypeConverter {

  @SuppressWarnings("rawtypes")
  @Override
  public Object convertFromString(Map context, String[] values, Class toClass) {
    if (values[0].length() == 0) {
      return null;
    }

    try {
      AdditionalIpV6RouteGroup routeg = new AdditionalIpV6RouteGroup();
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
      AdditionalIpV6RouteGroup routeg = (AdditionalIpV6RouteGroup)o;
      return routeg.getId().toString();
    } catch (Exception e) {
      throw new TypeConversionException(e);
    }
  }

}
