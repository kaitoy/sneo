/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.validator;

import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class IpAddressPrefixLengthValidator extends FieldValidatorSupport {

  public void validate(Object object) throws ValidationException {
    String validatingFieldName = getFieldName();
    Integer prefixLength = (Integer)getFieldValue(validatingFieldName, object);
    String address = (String)getFieldValue("address", object);

    if (address == null) {
      // An invalid value is tried to be input to the address field
      return;
    }

    // assuming prefixLength is between 0 and 128.
    if (!address.contains(":")) {
      if (prefixLength > 32) {
        addFieldError(validatingFieldName, object);
      }
    }
  }

}
