/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.action;

import java.util.Map;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import com.github.kaitoy.sneo.giane.model.Lag;
import com.github.kaitoy.sneo.giane.model.dao.LagDao;
import com.github.kaitoy.sneo.giane.model.dao.NodeDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class LagEditGridEntryAction extends ActionSupport {

  /**
   *
   */
  private static final long serialVersionUID = -8407250300673097028L;

  private LagDao lagDao;
  private NodeDao nodeDao;

  private String oper;
  private Integer id;
  private String name;
  private Integer channelGroupNumber;

  // for DI
  public void setLagDao(LagDao lagDao) {
    this.lagDao = lagDao;
  }

  // for DI
  public void seNodeDao(NodeDao nodeDao) {
    this.nodeDao = nodeDao;
  }

  @Override
  public String execute() throws Exception {
    if (oper.equalsIgnoreCase("add")) {
      Lag model = new Lag();
      model.setName(name);
      model.setChannelGroupNumber(channelGroupNumber);

      Map<String, Object> params = ActionContext.getContext().getParameters();
      Integer node_id
        = Integer.valueOf(((String[])params.get("node_id"))[0]);
      model.setNode(nodeDao.findByKey(node_id));

      lagDao.create(model);
    }
    else if (oper.equalsIgnoreCase("edit")) {
      Lag model = lagDao.findByKey(id);
      model.setName(name);
      model.setChannelGroupNumber(channelGroupNumber);
      lagDao.update(model);
    }
    else if (oper.equalsIgnoreCase("del")) {
      Lag model = lagDao.findByKey(id);
      lagDao.delete(model);
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

  public Integer getChannelGroupNumber() {
    return channelGroupNumber;
  }

  public void setChannelGroupNumber(Integer channelGroupNumber) {
    this.channelGroupNumber = channelGroupNumber;
  }

}
