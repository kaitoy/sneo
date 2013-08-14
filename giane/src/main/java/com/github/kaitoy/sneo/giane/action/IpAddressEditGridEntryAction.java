package com.github.kaitoy.sneo.giane.action;

import java.util.Map;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;

import com.github.kaitoy.sneo.giane.model.IpAddress;
import com.github.kaitoy.sneo.giane.model.dao.IpAddressDao;
import com.github.kaitoy.sneo.giane.model.dao.IpAddressRelationDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class IpAddressEditGridEntryAction extends ActionSupport {

  /**
   *
   */
  private static final long serialVersionUID = -2409916279930120460L;

  private IpAddressDao ipAddressDao;
  private IpAddressRelationDao ipAddressRelationDao;

  private String oper;
  private Integer id;
  private String address;
  private Integer prefixLength;

  // for DI
  public void setIpAddressDao(IpAddressDao ipAddressDao) {
    this.ipAddressDao = ipAddressDao;
  }

  // for DI
  public void setIpAddressRelationDao(
    IpAddressRelationDao ipAddressRelationDao
  ) {
    this.ipAddressRelationDao = ipAddressRelationDao;
  }

  public String execute() throws Exception {
    if (oper.equalsIgnoreCase("add")) {
      IpAddress model = new IpAddress();
      model.setAddress(address);
      model.setPrefixLength(prefixLength);

      Map<String, Object> params = ActionContext.getContext().getParameters();
      Integer ipAddressRelationId
        = Integer.valueOf(((String[])params.get("ipAddressRelation_id"))[0]);
      model.setIpAddressRelation(
        ipAddressRelationDao.findByKey(ipAddressRelationId)
      );

      ipAddressDao.save(model);
    }
    else if (oper.equalsIgnoreCase("edit")) {
      IpAddress model = ipAddressDao.findByKey(id);
      model.setAddress(address);
      model.setPrefixLength(prefixLength);
      ipAddressDao.update(model);
    }
    else if (oper.equalsIgnoreCase("del")) {
      IpAddress model = ipAddressDao.findByKey(id);
      ipAddressDao.delete(model);
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

  public Integer getPrefixLength() {
    return prefixLength;
  }

  public void PrefixLength(Integer prefixLength) {
    this.prefixLength = prefixLength;
  }

}
