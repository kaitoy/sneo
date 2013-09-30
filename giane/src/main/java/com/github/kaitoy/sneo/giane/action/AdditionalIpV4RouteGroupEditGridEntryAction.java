package com.github.kaitoy.sneo.giane.action;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import com.github.kaitoy.sneo.giane.model.AdditionalIpV4RouteGroup;
import com.github.kaitoy.sneo.giane.model.dao.AdditionalIpV4RouteGroupDao;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class AdditionalIpV4RouteGroupEditGridEntryAction extends ActionSupport {

  /**
   *
   */
  private static final long serialVersionUID = -5836111746528321582L;

  private AdditionalIpV4RouteGroupDao additionalIpV4RouteGroupDao;

  private String oper;
  private Integer id;
  private String name;
  private String descr;

  // for DI
  public void setAdditionalIpV4RouteGroupDao(AdditionalIpV4RouteGroupDao additionalIpV4RouteGroupDao) {
    this.additionalIpV4RouteGroupDao = additionalIpV4RouteGroupDao;
  }

  @Override
  public String execute() throws Exception {
    if (oper.equalsIgnoreCase("add")) {
      AdditionalIpV4RouteGroup model = new AdditionalIpV4RouteGroup();
      model.setName(name);
      model.setDescr(descr);
      additionalIpV4RouteGroupDao.create(model);
    }
    else if (oper.equalsIgnoreCase("edit")) {
      AdditionalIpV4RouteGroup model = additionalIpV4RouteGroupDao.findByKey(id);
      model.setName(name);
      model.setDescr(descr);
      additionalIpV4RouteGroupDao.update(model);
    }
    else if (oper.equalsIgnoreCase("del")) {
      AdditionalIpV4RouteGroup model = additionalIpV4RouteGroupDao.findByKey(id);
      additionalIpV4RouteGroupDao.delete(model);
    }

    return NONE;
  }

  public String getOper() {
    return oper;
  }

  public void setOper(String oper) {
    this.oper = oper;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescr() {
    return descr;
  }

  public void setDescr(String descr) {
    this.descr = descr;
  }

}
