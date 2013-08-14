/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.action;

import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.github.kaitoy.sneo.giane.model.IpAddress;
import com.github.kaitoy.sneo.giane.model.dao.IpAddressDao;
import com.github.kaitoy.sneo.giane.model.dao.IpAddressRelationDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class IpAddressAction
extends ActionSupport implements ModelDriven<IpAddress> {

  /**
   *
   */
  private static final long serialVersionUID = 8060643236835866920L;

  private IpAddress model = new IpAddress();
  private IpAddressDao ipAddressDao;
  private IpAddressRelationDao ipAddressRelationDao;

  public IpAddress getModel() { return model; }

  @VisitorFieldValidator(appendPrefix = false)
  public void setModel(IpAddress model) { this.model = model; }

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

  @Action(
    value = "ip-address-tab-content",
    results = { @Result(name = "tab", location = "ip-address-tab-content.jsp")}
  )
  @SkipValidation
  public String tab() throws Exception {
    return "tab";
  }

  @Action(
    value = "ip-address-create",
    results = {
      @Result(name = "success", location = "empty.jsp")
    }
  )
  public String create() throws Exception {
    Map<String, Object> params = ActionContext.getContext().getParameters();
    Integer ipAddressRelationId
      = Integer.valueOf(((String[])params.get("ipAddressRelation_id"))[0]);

    model.setIpAddressRelation(
      ipAddressRelationDao.findByKey(ipAddressRelationId)
    );
    ipAddressDao.save(model);

    return "success";
  }

  @Action(
    value = "ip-address-update",
    results = {
      @Result(name = "success", location = "empty.jsp")
    }
  )
  public String update() throws Exception {
    IpAddress update = ipAddressDao.findByKey(model.getId());

    update.setAddress(model.getAddress());
    update.setPrefixLength(model.getPrefixLength());
    ipAddressDao.update(update);

    return "success";
  }

  public void validate() {
    if (ActionContext.getContext().getName().equals("ip-address-update")) {
      if (model.getId() == null) {
        addActionError(getText("select.a.row"));
      }
    }
  }

}
