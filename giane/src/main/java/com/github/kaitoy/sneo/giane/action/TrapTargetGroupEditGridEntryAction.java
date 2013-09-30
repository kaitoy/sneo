package com.github.kaitoy.sneo.giane.action;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import com.github.kaitoy.sneo.giane.model.TrapTargetGroup;
import com.github.kaitoy.sneo.giane.model.dao.TrapTargetGroupDao;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class TrapTargetGroupEditGridEntryAction extends ActionSupport {

  /**
   *
   */
  private static final long serialVersionUID = 3668942402243289260L;

  private TrapTargetGroupDao trapTargetGroupDao;

  private String oper;
  private Integer id;
  private String name;
  private String descr;

  // for DI
  public void setTrapTargetGroupDao(TrapTargetGroupDao trapTargetGroupDao) {
    this.trapTargetGroupDao = trapTargetGroupDao;
  }

  @Override
  public String execute() throws Exception {
    if (oper.equalsIgnoreCase("add")) {
      TrapTargetGroup model = new TrapTargetGroup();
      model.setName(name);
      model.setDescr(descr);
      trapTargetGroupDao.create(model);
    }
    else if (oper.equalsIgnoreCase("edit")) {
      TrapTargetGroup model = trapTargetGroupDao.findByKey(id);
      model.setName(name);
      model.setDescr(descr);
      trapTargetGroupDao.update(model);
    }
    else if (oper.equalsIgnoreCase("del")) {
      TrapTargetGroup model = trapTargetGroupDao.findByKey(id);
      trapTargetGroupDao.delete(model);
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
