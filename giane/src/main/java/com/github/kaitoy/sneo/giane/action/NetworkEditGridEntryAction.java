package com.github.kaitoy.sneo.giane.action;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;

import com.github.kaitoy.sneo.giane.model.Network;
import com.github.kaitoy.sneo.giane.model.dao.NetworkDao;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class NetworkEditGridEntryAction extends ActionSupport {

  /**
   *
   */
  private static final long serialVersionUID = -6566874140197419290L;

  private NetworkDao networkDao;

  private String oper;
  private Integer id;
  private String name;

  // for DI
  public void setNetworkDao(NetworkDao networkDao) {
    this.networkDao = networkDao;
  }

  public String execute() throws Exception {
    if (oper.equalsIgnoreCase("add")) {
      Network model = new Network();
      model.setName(name);
      networkDao.create(model);
    }
    else if (oper.equalsIgnoreCase("edit")) {
      Network model = networkDao.findByKey(id);
      model.setName(name);
      networkDao.update(model);
    }
    else if (oper.equalsIgnoreCase("del")) {
      Network model = networkDao.findByKey(id);
      networkDao.delete(model);
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

}
