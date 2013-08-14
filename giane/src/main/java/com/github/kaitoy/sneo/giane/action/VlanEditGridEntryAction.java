package com.github.kaitoy.sneo.giane.action;

import java.util.Map;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;

import com.github.kaitoy.sneo.giane.model.Vlan;
import com.github.kaitoy.sneo.giane.model.VlanIpAddressRelation;
import com.github.kaitoy.sneo.giane.model.dao.NodeDao;
import com.github.kaitoy.sneo.giane.model.dao.VlanDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class VlanEditGridEntryAction extends ActionSupport {

  /**
   *
   */
  private static final long serialVersionUID = 6660474834971572928L;

  private VlanDao vlanDao;
  private NodeDao nodeDao;

  private String oper;
  private Integer id;
  private String name;
  private Integer vid;

  // for DI
  public void setVlanDao(VlanDao vlanDao) {
    this.vlanDao = vlanDao;
  }

  // for DI
  public void seNodeDao(NodeDao nodeDao) {
    this.nodeDao = nodeDao;
  }

  public String execute() throws Exception {
    if (oper.equalsIgnoreCase("add")) {
      Vlan model = new Vlan();
      model.setName(name);
      model.setVid(vid);

      Map<String, Object> params = ActionContext.getContext().getParameters();
      Integer node_id
        = Integer.valueOf(((String[])params.get("node_id"))[0]);
      model.setNode(nodeDao.findByKey(node_id));

      model.setIpAddressRelation(new VlanIpAddressRelation());

      vlanDao.save(model);
    }
    else if (oper.equalsIgnoreCase("edit")) {
      Vlan model = vlanDao.findByKey(id);
      model.setName(name);
      model.setVid(vid);
      vlanDao.update(model);
    }
    else if (oper.equalsIgnoreCase("del")) {
      Vlan model = vlanDao.findByKey(id);
      vlanDao.delete(model);
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

  public Integer getVid() {
    return vid;
  }

  public void setVid(Integer vid) {
    this.vid = vid;
  }

}
