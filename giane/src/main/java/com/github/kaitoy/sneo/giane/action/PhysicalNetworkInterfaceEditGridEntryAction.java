package com.github.kaitoy.sneo.giane.action;

import java.util.Map;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;

import com.github.kaitoy.sneo.giane.model.PhysicalNetworkInterface;
import com.github.kaitoy.sneo.giane.model.PhysicalNetworkInterfaceIpAddressRelation;
import com.github.kaitoy.sneo.giane.model.dao.NodeDao;
import com.github.kaitoy.sneo.giane.model.dao.PhysicalNetworkInterfaceDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class PhysicalNetworkInterfaceEditGridEntryAction extends ActionSupport {

  /**
   *
   */
  private static final long serialVersionUID = -4910868981551628408L;

  private PhysicalNetworkInterfaceDao physicalNetworkInterfaceDao;
  private NodeDao nodeDao;

  private String oper;
  private Integer id;
  private String name;

  // for DI
  public void setPhysicalNetworkInterfaceDao(
    PhysicalNetworkInterfaceDao networkInterfaceDao
  ) {
    this.physicalNetworkInterfaceDao = networkInterfaceDao;
  }

  // for DI
  public void seNodeDao(NodeDao nodeDao) {
    this.nodeDao = nodeDao;
  }

  public String execute() throws Exception {
    if (oper.equalsIgnoreCase("add")) {
      PhysicalNetworkInterface model = new PhysicalNetworkInterface();
      model.setName(name);

      Map<String, Object> params = ActionContext.getContext().getParameters();
      Integer node_id
        = Integer.valueOf(((String[])params.get("node_id"))[0]);
      model.setNode(nodeDao.findByKey(node_id));

      model.setIpAddressRelation(
        new PhysicalNetworkInterfaceIpAddressRelation()
      );

      physicalNetworkInterfaceDao.create(model);
    }
    else if (oper.equalsIgnoreCase("edit")) {
      PhysicalNetworkInterface model
        = physicalNetworkInterfaceDao.findByKey(id);
      model.setName(name);
      physicalNetworkInterfaceDao.update(model);
    }
    else if (oper.equalsIgnoreCase("del")) {
      PhysicalNetworkInterface model
        = physicalNetworkInterfaceDao.findByKey(id);
      physicalNetworkInterfaceDao.delete(model);
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
