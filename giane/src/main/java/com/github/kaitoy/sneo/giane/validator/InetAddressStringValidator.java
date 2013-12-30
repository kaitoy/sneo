/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.validator;

import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class InetAddressStringValidator extends FieldValidatorSupport {

  public void validate(Object object) throws ValidationException {
    String validatingFieldName = getFieldName();
    String inetAddrStr = (String)getFieldValue(validatingFieldName, object);

    if (inetAddrStr.contains(":")) {
      if (!Inet6AddressStringValidator.isValid(inetAddrStr)) {
        addFieldError(validatingFieldName, object);
        return;
      }
    }
    else {
      if (!Inet4AddressStringValidator.isValid(inetAddrStr)) {
        addFieldError(validatingFieldName, object);
        return;
      }
    }
  }

}
