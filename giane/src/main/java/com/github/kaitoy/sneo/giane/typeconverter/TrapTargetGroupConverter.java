/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.typeconverter;

import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

import com.github.kaitoy.sneo.giane.model.TrapTargetGroup;
import com.opensymphony.xwork2.conversion.TypeConversionException;

/**
 * @author Kaito
 *
 */
public class TrapTargetGroupConverter extends StrutsTypeConverter {

  @SuppressWarnings("rawtypes")
  @Override
  public Object convertFromString(Map context, String[] values, Class toClass) {
    if (values[0].length() == 0) {
      return null;
    }

    try {
      TrapTargetGroup ttg = new TrapTargetGroup();
      ttg.setId(Integer.valueOf(values[0]));
      return ttg;
    } catch (Exception e) {
      throw new TypeConversionException(e);
    }
  }

  @SuppressWarnings("rawtypes")
  @Override
  public String convertToString(Map context, Object o) {
    try {
      TrapTargetGroup ttg = (TrapTargetGroup)o;
      return ttg.getId().toString();
    } catch (Exception e) {
      throw new TypeConversionException(e);
    }
  }

}
