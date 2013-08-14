package com.github.kaitoy.sneo.giane.action;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;

import com.github.kaitoy.sneo.giane.model.FileMibFormat;
import com.github.kaitoy.sneo.giane.model.SnmpAgent;
import com.github.kaitoy.sneo.giane.model.dao.SnmpAgentDao;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class SnmpAgentEditGridEntryAction extends ActionSupport {

  /**
   *
   */
  private static final long serialVersionUID = -1778210989323221497L;

  private SnmpAgentDao snmpAgentDao;
  //private NodeDao nodeDao;

  private String oper;
  private Integer id;
  private String address;
  private Integer port;
  private String communityName;
  private String securityName;
  private String fileMibPath;
  private FileMibFormat fileMibFormat;
  private String communityStringIndexList;

  // for DI
  public void setSnmpAgentDao(SnmpAgentDao snmpAgentDao) {
    this.snmpAgentDao = snmpAgentDao;
  }

  // for DI
//  public void setNodeDao(NodeDao nodeDao) {
//    this.nodeDao = nodeDao;
//  }

  public String execute() throws Exception {
    if (oper.equalsIgnoreCase("add")) {
      throw new AssertionError("not yet implemented");
//      SnmpAgent model = new SnmpAgent();
//
//      Map<String, Object> params = ActionContext.getContext().getParameters();
//      Integer node_id
//        = Integer.valueOf(((String[])params.get("node_id"))[0]);
//      model.setNode(nodeDao.findByKey(node_id));
//
//      snmpAgentDao.save(model);
    }
    else if (oper.equalsIgnoreCase("edit")) {
      throw new AssertionError("not yet implemented");
//      SnmpAgent model = snmpAgentDao.findByKey(id);
//      snmpAgentDao.update(model);
    }
    else if (oper.equalsIgnoreCase("del")) {
      SnmpAgent model = snmpAgentDao.findByKey(id);
      snmpAgentDao.delete(model);
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

  public String getCommunityName() {
    return communityName;
  }

  public void setCommunityName(String communityName) {
    this.communityName = communityName;
  }

  public String getSecurityName() {
    return securityName;
  }

  public void setSecurityName(String securityName) {
    this.securityName = securityName;
  }

  public String getFileMibPath() {
    return fileMibPath;
  }

  public void setFileMibPath(String fileMibPath) {
    this.fileMibPath = fileMibPath;
  }

  public FileMibFormat getFileMibFormat() {
    return fileMibFormat;
  }

  public void setFileMibFormat(FileMibFormat fileMibFormat) {
    this.fileMibFormat = fileMibFormat;
  }

  public String getCommunityStringIndexList() {
    return communityStringIndexList;
  }

  public void setCommunityStringIndexList(String communityStringIndexList) {
    this.communityStringIndexList = communityStringIndexList;
  }

}
