/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.ParameterAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import com.github.kaitoy.sneo.giane.action.message.DialogMessage;
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.github.kaitoy.sneo.giane.action.message.IpAddressMessage;
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
extends ActionSupport
implements ModelDriven<IpAddress>, ParameterAware,
  FormMessage, IpAddressMessage, DialogMessage {

  /**
   *
   */
  private static final long serialVersionUID = 8060643236835866920L;

  private IpAddress model = new IpAddress();
  private Map<String, String[]> parameters;
  private IpAddressDao ipAddressDao;
  private IpAddressRelationDao ipAddressRelationDao;
  private String deletingIdList;

  public IpAddress getModel() { return model; }

  @VisitorFieldValidator(appendPrefix = true)
  public void setModel(IpAddress model) { this.model = model; }

  public void setParameters(Map<String, String[]> parameters) {
    this.parameters = parameters;
  }

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

  public void setDeletingIdList(String deletingIdList) {
    this.deletingIdList = deletingIdList;
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
    Integer ipAddressRelationId
      = Integer.valueOf(parameters.get("ipAddressRelation_id")[0]);
    model.setIpAddressRelation(
      ipAddressRelationDao.findByKey(ipAddressRelationId)
    );
    ipAddressDao.create(model);

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

  @Action(
    value = "ip-address-delete",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  @SkipValidation
  public String delete() throws Exception {
    List<IpAddress> deletingList = new ArrayList<IpAddress>();
    for (String idStr: deletingIdList.split(",")) {
      deletingList.add(ipAddressDao.findByKey(Integer.valueOf(idStr)));
    }
    ipAddressDao.delete(deletingList);
    return "success";
  }

  @Override
  public void validate() {
    if (ActionContext.getContext().getName().equals("ip-address-update")) {
      if (model.getId() == null) {
        addActionError(getText("select.a.row"));
      }
    }
  }

}
