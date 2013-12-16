/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.typeconverter;

import java.util.Locale;
import java.util.Map;
import org.apache.struts2.util.StrutsTypeConverter;
import com.opensymphony.xwork2.conversion.TypeConversionException;

/**
 * @author Kaito
 *
 */
public class LocaleConverter extends StrutsTypeConverter {

  @SuppressWarnings("rawtypes")
  @Override
  public Object convertFromString(Map context, String[] values, Class toClass) {
    try {
      return new Locale(values[0]);
    } catch (Exception e) {
      throw new TypeConversionException(e);
    }
  }

  @SuppressWarnings("rawtypes")
  @Override
  public String convertToString(Map context, Object o) {
    try {
      Locale locale = (Locale)o;
      return locale.toString();
    } catch (Exception e) {
      throw new TypeConversionException(e);
    }
  }

}
