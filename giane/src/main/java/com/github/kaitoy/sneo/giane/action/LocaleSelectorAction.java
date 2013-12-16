/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.action;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import com.opensymphony.xwork2.interceptor.I18nInterceptor;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class LocaleSelectorAction extends ActionSupport
implements ServletRequestAware, SessionAware {

  /**
   *
   */
  private static final long serialVersionUID = -6114265184234489677L;

  private static final Map<Locale, String> locales = new HashMap<Locale, String>();
  private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

  private Locale currentLocale;
  private HttpServletRequest request;
  private Map<String, Object> session;

  static {
    locales.put(Locale.ENGLISH, "English");
    locales.put(Locale.JAPANESE, "Japanese");
  }

  public Locale getCurrentLocale() {
    return currentLocale;
  }

  @TypeConversion(converter = "com.github.kaitoy.sneo.giane.typeconverter.LocaleConverter")
  public void setCurrentLocale(Locale currentLocale) {
    this.currentLocale = currentLocale;
  }

  public Map<Locale, String> getLocales() {
    return locales;
  }

  public void setServletRequest(HttpServletRequest request) {
    this.request = request;
  }

  public void setSession(Map<String, Object> session) {
    this.session = session;
  }

  @Override
  @Action(
    results = { @Result(name = "success", location = "locale-selector.jsp") }
  )
  @SkipValidation
  public String execute() throws Exception {
    Locale reqLocale = new Locale(request.getLocale().getLanguage());
    if (locales.containsKey(reqLocale)) {
      currentLocale = reqLocale;
    }
    else {
      currentLocale = DEFAULT_LOCALE;
    }
    session.put(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE, currentLocale);

    return "success";
  }

  @Action(
    value = "change-locale",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  @SkipValidation
  public String change() throws Exception {
    if (!locales.containsKey(currentLocale)) {
      currentLocale = DEFAULT_LOCALE;
    }
    session.put(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE, currentLocale);

    return "success";
  }

}
