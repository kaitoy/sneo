/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.typeconverter;

import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

import com.github.kaitoy.sneo.giane.model.RealNetworkInterfaceConfiguration;
import com.opensymphony.xwork2.conversion.TypeConversionException;

/**
 * @author Kaito
 *
 */
public class RealNetworkInterfaceConfigurationConverter
extends StrutsTypeConverter {

  @SuppressWarnings("rawtypes")
  @Override
  public Object convertFromString(
    Map context, String[] values, Class toClass
  ) {
    if (values[0].length() == 0) {
      return null;
    }

    try {
      RealNetworkInterfaceConfiguration conf
        = new RealNetworkInterfaceConfiguration();
      conf.setId(Integer.valueOf(values[0]));
      return conf;
    } catch (Exception e) {
      throw new TypeConversionException(e);
    }
  }

  @SuppressWarnings("rawtypes")
  @Override
  public String convertToString(Map context, Object o) {
    try {
      RealNetworkInterfaceConfiguration conf
        = (RealNetworkInterfaceConfiguration)o;
      return conf.getId().toString();
    } catch (Exception e) {
      throw new TypeConversionException(e);
    }
  }

}
