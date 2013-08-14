package com.github.kaitoy.sneo.giane.action;

import java.util.Map;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;

import com.github.kaitoy.sneo.giane.model.RealNetworkInterface;
import com.github.kaitoy.sneo.giane.model.dao.NodeDao;
import com.github.kaitoy.sneo.giane.model.dao.RealNetworkInterfaceDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class RealNetworkInterfaceEditGridEntryAction extends ActionSupport {

  /**
   *
   */
  private static final long serialVersionUID = -4910868981551628408L;

  private RealNetworkInterfaceDao realNetworkInterfaceDao;
  private NodeDao nodeDao;

  private String oper;
  private Integer id;
  private String name;

  // for DI
  public void setRealNetworkInterfaceDao(
    RealNetworkInterfaceDao realNetworkInterfaceDao
  ) {
    this.realNetworkInterfaceDao = realNetworkInterfaceDao;
  }

  // for DI
  public void seNodeDao(NodeDao nodeDao) {
    this.nodeDao = nodeDao;
  }

  public String execute() throws Exception {
    if (oper.equalsIgnoreCase("add")) {
      RealNetworkInterface model = new RealNetworkInterface();
      model.setName(name);

      Map<String, Object> params = ActionContext.getContext().getParameters();
      Integer node_id
        = Integer.valueOf(((String[])params.get("node_id"))[0]);
      model.setNode(nodeDao.findByKey(node_id));

      realNetworkInterfaceDao.save(model);
    }
    else if (oper.equalsIgnoreCase("edit")) {
      RealNetworkInterface model
        = realNetworkInterfaceDao.findByKey(id);
      model.setName(name);
      realNetworkInterfaceDao.update(model);
    }
    else if (oper.equalsIgnoreCase("del")) {
      RealNetworkInterface model
        = realNetworkInterfaceDao.findByKey(id);
      realNetworkInterfaceDao.delete(model);
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
