package com.github.kaitoy.sneo.giane.action;

import java.util.Map;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;

import com.github.kaitoy.sneo.giane.model.L2Connection;
import com.github.kaitoy.sneo.giane.model.dao.L2ConnectionDao;
import com.github.kaitoy.sneo.giane.model.dao.NetworkDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class L2ConnectionEditGridEntryAction extends ActionSupport {

  /**
   *
   */
  private static final long serialVersionUID = -7662078050933718126L;

  private L2ConnectionDao l2ConnectionDao;
  private NetworkDao networkDao;

  private String oper;
  private Integer id;
  private String name;

  // for DI
  public void setL2ConnectionDao(L2ConnectionDao l2ConnectionDao) {
    this.l2ConnectionDao = l2ConnectionDao;
  }

  // for DI
  public void setNetworkDao(NetworkDao networkDao) {
    this.networkDao = networkDao;
  }

  public String execute() throws Exception {
    if (oper.equalsIgnoreCase("add")) {
      L2Connection model = new L2Connection();
      model.setName(name);

      Map<String, Object> params = ActionContext.getContext().getParameters();
      Integer network_id
        = Integer.valueOf(((String[])params.get("network_id"))[0]);
      model.setNetwork(networkDao.findByKey(network_id));

      l2ConnectionDao.save(model);
    }
    else if (oper.equalsIgnoreCase("edit")) {
      L2Connection model = l2ConnectionDao.findByKey(id);
      model.setName(name);
      l2ConnectionDao.update(model);
    }
    else if (oper.equalsIgnoreCase("del")) {
      L2Connection model = l2ConnectionDao.findByKey(id);
      l2ConnectionDao.delete(model);
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
