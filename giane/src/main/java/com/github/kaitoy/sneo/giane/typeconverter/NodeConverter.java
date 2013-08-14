/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.typeconverter;

import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

import com.github.kaitoy.sneo.giane.model.Node;
import com.opensymphony.xwork2.conversion.TypeConversionException;

/**
 * @author Kaito
 *
 */
public class NodeConverter extends StrutsTypeConverter {

  @SuppressWarnings("rawtypes")
  @Override
  public Object convertFromString(Map context, String[] values, Class toClass) {
    if (values[0].length() == 0) {
      return null;
    }

    try {
      Node node = new Node();
      node.setId(Integer.valueOf(values[0]));
      return node;
    } catch (Exception e) {
      throw new TypeConversionException(e);
    }
  }

  @SuppressWarnings("rawtypes")
  @Override
  public String convertToString(Map context, Object o) {
    try {
      Node node = (Node)o;
      return node.getId().toString();
    } catch (Exception e) {
      throw new TypeConversionException(e);
    }
  }

}
