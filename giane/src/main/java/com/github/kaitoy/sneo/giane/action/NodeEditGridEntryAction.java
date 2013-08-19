package com.github.kaitoy.sneo.giane.action;

import java.util.Map;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;

import com.github.kaitoy.sneo.giane.model.Node;
import com.github.kaitoy.sneo.giane.model.dao.NetworkDao;
import com.github.kaitoy.sneo.giane.model.dao.NodeDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class NodeEditGridEntryAction extends ActionSupport {

  /**
   *
   */
  private static final long serialVersionUID = -7662078050933718126L;

  private NodeDao nodeDao;
  private NetworkDao networkDao;

  private String oper;
  private Integer id;
  private String name;
  private Integer ttl;

  // for DI
  public void setNodeDao(NodeDao nodeDao) {
    this.nodeDao = nodeDao;
  }

  // for DI
  public void setNetworkDao(NetworkDao networkDao) {
    this.networkDao = networkDao;
  }

  public String execute() throws Exception {
    if (oper.equalsIgnoreCase("add")) {
      Node model = new Node();
      model.setName(name);
      model.setTtl(ttl);

      Map<String, Object> params = ActionContext.getContext().getParameters();
      Integer network_id
        = Integer.valueOf(((String[])params.get("network_id"))[0]);
      model.setNetwork(networkDao.findByKey(network_id));

      nodeDao.create(model);
    }
    else if (oper.equalsIgnoreCase("edit")) {
      Node model = nodeDao.findByKey(id);
      model.setName(name);
      model.setTtl(ttl);
      nodeDao.update(model);
    }
    else if (oper.equalsIgnoreCase("del")) {
      Node model = nodeDao.findByKey(id);
      nodeDao.delete(model);
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

  public Integer getTtl() {
    return ttl;
  }

  public void setTtl(Integer ttl) {
    this.ttl = ttl;
  }

}
