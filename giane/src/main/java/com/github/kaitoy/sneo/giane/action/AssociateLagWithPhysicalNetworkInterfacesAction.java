/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
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
import com.github.kaitoy.sneo.giane.action.message.AssociateActionMessage;
import com.github.kaitoy.sneo.giane.model.Lag;
import com.github.kaitoy.sneo.giane.model.PhysicalNetworkInterface;
import com.github.kaitoy.sneo.giane.model.dao.LagDao;
import com.github.kaitoy.sneo.giane.model.dao.PhysicalNetworkInterfaceDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class AssociateLagWithPhysicalNetworkInterfacesAction extends ActionSupport
implements AssociateActionMessage {

  /**
   *
   */
  private static final long serialVersionUID = 7957474802263528822L;

  private PhysicalNetworkInterfaceDao physicalNetworkInterfaceDao;
  private LagDao lagDao;
  private String idList;
  private String dialogTitleKey;
  private String dialogTextKey;

  // for DI
  public void setPhysicalNetworkInterfaceDao(
    PhysicalNetworkInterfaceDao physicalNetworkInterfaceDao
  ) {
    this.physicalNetworkInterfaceDao = physicalNetworkInterfaceDao;
  }

  // for DI
  public void setLagDao(LagDao lagDao) {
    this.lagDao = lagDao;
  }

  public String getIdList() {
    return idList;
  }

  public void setIdList(String idList) {
    this.idList = idList;
  }

  public String getDialogTitleKey() {
    return dialogTitleKey;
  }

  public String getDialogTextKey() {
    return dialogTextKey;
  }

  @Override
  @Action(
    results = {
      @Result(name = "success", location = "dialog.jsp"),
      @Result(name = "noChange", location = "dialog.jsp")
    }
  )
  public String execute() throws Exception {
    if (idList.equals("undefined")) {
      dialogTitleKey
        = "associateAction.noChange.dialog.title";
      dialogTextKey
        = "associateAction.noChange.dialog.text";
      return "noChange";
    }

    List<PhysicalNetworkInterface> pnifs = new ArrayList<PhysicalNetworkInterface>();
    if (idList != null && idList.length() != 0) {
      for (String strId: idList.split(",")) {
        pnifs.add(physicalNetworkInterfaceDao.findByKey(Integer.valueOf(strId)));
      }
    }

    Map<String, Object> params = ActionContext.getContext().getParameters();
    Integer lag_id = Integer.valueOf(((String[])params.get("lag_id"))[0]);
    Lag lag = lagDao.findByKey(lag_id);

    if (pnifs.equals(lag.getPhysicalNetworkInterfaces())) {
      dialogTitleKey = "associateAction.noChange.dialog.title";
      dialogTextKey = "associateAction.noChange.dialog.text";
      return "noChange";
    }

    for (PhysicalNetworkInterface pnif: lag.getPhysicalNetworkInterfaces()) {
      if (!pnifs.contains(pnif)) {
        pnif.setLag(null);
        physicalNetworkInterfaceDao.update(pnif);
      }
    }
    for (PhysicalNetworkInterface pnif: pnifs) {
      if (!lag.getPhysicalNetworkInterfaces().contains(pnif)) {
        pnif.setLag(lag);
        physicalNetworkInterfaceDao.update(pnif);
      }
    }

    dialogTitleKey = "associateAction.success.dialog.title";
    dialogTextKey = "associateAction.success.dialog.text";
    return "success";
  }

}
