package com.github.kaitoy.sneo.giane.action;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import com.github.kaitoy.sneo.giane.model.TrapTarget;
import com.github.kaitoy.sneo.giane.model.dao.TrapTargetDao;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class TrapTargetEditGridEntryAction extends ActionSupport {

  /**
   *
   */
  private static final long serialVersionUID = 2494795963725271240L;

  private TrapTargetDao trapTargetDao;

  private String oper;
  private Integer id;
  private String name;
  private String address;
  private Integer port;
  private String descr;

  // for DI
  public void setTrapTargetDao(TrapTargetDao trapTargetDao) {
    this.trapTargetDao = trapTargetDao;
  }

  @Override
  public String execute() throws Exception {
    if (oper.equalsIgnoreCase("add")) {
      TrapTarget model = new TrapTarget();
      model.setName(name);
      model.setAddress(address);
      model.setPort(port);
      model.setDescr(descr);
      trapTargetDao.create(model);
    }
    else if (oper.equalsIgnoreCase("edit")) {
      TrapTarget model = trapTargetDao.findByKey(id);
      model.setName(name);
      model.setAddress(address);
      model.setPort(port);
      model.setDescr(descr);
      trapTargetDao.update(model);
    }
    else if (oper.equalsIgnoreCase("del")) {
      TrapTarget model = trapTargetDao.findByKey(id);
      trapTargetDao.delete(model);
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

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public String getDescr() {
    return descr;
  }

  public void setDescr(String descr) {
    this.descr = descr;
  }

}
