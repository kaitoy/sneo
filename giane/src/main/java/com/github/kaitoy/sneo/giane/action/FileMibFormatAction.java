/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.action;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;

import com.github.kaitoy.sneo.giane.model.FileMibFormat;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class FileMibFormatAction extends ActionSupport {

  /**
   *
   */
  private static final long serialVersionUID = 8778028043131694655L;

  private static final FileMibFormat[] formats = FileMibFormat.values();

  public FileMibFormat[] getFormats() { return formats; }

//  @SkipValidation
//  public String execute() throws Exception {
//    return "list";
//  }

}
