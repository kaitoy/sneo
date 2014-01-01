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
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.kaitoy.sneo.giane.action.message.AssociateActionMessage;
import com.github.kaitoy.sneo.giane.action.message.DialogMessage;
import com.github.kaitoy.sneo.giane.action.message.PhysicalNetworkInterfaceMessage;
import com.github.kaitoy.sneo.giane.model.L2Connection;
import com.github.kaitoy.sneo.giane.model.PhysicalNetworkInterface;
import com.github.kaitoy.sneo.giane.model.dao.L2ConnectionDao;
import com.github.kaitoy.sneo.giane.model.dao.PhysicalNetworkInterfaceDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class AssociateL2ConnectionWithPhysicalNetworkInterfacesAction
extends ActionSupport
implements AssociateActionMessage, PhysicalNetworkInterfaceMessage, DialogMessage {

  /**
   *
   */
  private static final long serialVersionUID = -6021384373237360601L;

  private static final Logger logger
    = LoggerFactory.getLogger(AssociateL2ConnectionWithPhysicalNetworkInterfacesAction.class);

  private PhysicalNetworkInterfaceDao physicalNetworkInterfaceDao;
  private L2ConnectionDao l2ConnectionDao;
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
  public void setL2ConnectionDao(L2ConnectionDao l2ConnectionDao) {
    this.l2ConnectionDao = l2ConnectionDao;
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
      Integer l2Connection_id = Integer.valueOf(((String[])params.get("l2Connection_id"))[0]);
      L2Connection l2Conn = l2ConnectionDao.findByKey(l2Connection_id);

      if (pnifs.equals(l2Conn.getPhysicalNetworkInterfaces())) {
        dialogTitleKey = "dialog.title.result";
        dialogTextKey  = "dialog.text.association.noChange";
        return "noChange";
      }

      for (PhysicalNetworkInterface pnif: l2Conn.getPhysicalNetworkInterfaces()) {
        if (!pnifs.contains(pnif)) {
          pnif.setL2Connection(null);
          physicalNetworkInterfaceDao.update(pnif);
        }
      }
      for (PhysicalNetworkInterface pnif: pnifs) {
        if (!l2Conn.getPhysicalNetworkInterfaces().contains(pnif)) {
          pnif.setL2Connection(l2Conn);
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
    value = "associate-l2-connection-with-physical-network-interfaces-grid-box",
    results = {
      @Result(
        name = "grid",
        location = "associate-l2-connection-with-physical-network-interfaces-grid.jsp"
      )
    }
  )
  @SkipValidation
  public String associationdGridBox() throws Exception {
    return "grid";
  }

}
