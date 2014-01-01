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
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.kaitoy.sneo.giane.action.message.AssociateActionMessage;
import com.github.kaitoy.sneo.giane.action.message.DialogMessage;
import com.github.kaitoy.sneo.giane.action.message.PhysicalNetworkInterfaceMessage;
import com.github.kaitoy.sneo.giane.model.Lag;
import com.github.kaitoy.sneo.giane.model.PhysicalNetworkInterface;
import com.github.kaitoy.sneo.giane.model.dao.LagDao;
import com.github.kaitoy.sneo.giane.model.dao.PhysicalNetworkInterfaceDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class AssociateLagWithPhysicalNetworkInterfacesAction extends ActionSupport
implements AssociateActionMessage, PhysicalNetworkInterfaceMessage, DialogMessage {

  /**
   *
   */
  private static final long serialVersionUID = 7957474802263528822L;

  private static final Logger logger
    = LoggerFactory.getLogger(AssociateLagWithPhysicalNetworkInterfacesAction.class);

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
      @Result(name = "noChange", location = "dialog.jsp"),
      @Result(name = "error", location = "dialog.jsp")
    }
  )
  public String execute() throws Exception {
    if (idList.equals("undefined")) {
      dialogTitleKey
        = "dialog.title.result";
      dialogTextKey
        = "dialog.text.association.noChange";
      return "noChange";
    }

    try {
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
        dialogTitleKey = "dialog.title.result";
        dialogTextKey = "dialog.text.association.noChange";
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

      dialogTitleKey = "dialog.title.result";
      dialogTextKey = "dialog.text.association.success";
      return "success";
    } catch (Exception e) {
      logger.error("An error occurred: ", e);
      dialogTitleKey = "dialog.title.result";
      dialogTextKey = "dialog.text.association.error";
      return "error";
    }
  }

  @Action(
    value = "associate-lag-with-physical-network-interfaces-grid-box",
    results = { @Result(name = "grid", location = "associate-lag-with-physical-network-interfaces-grid.jsp")}
  )
  @SkipValidation
  public String associationdGridBox() throws Exception {
    return "grid";
  }

}
