/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
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

import com.github.kaitoy.sneo.giane.model.L2Connection;
import com.github.kaitoy.sneo.giane.model.PhysicalNetworkInterface;
import com.github.kaitoy.sneo.giane.model.dao.L2ConnectionDao;
import com.github.kaitoy.sneo.giane.model.dao.PhysicalNetworkInterfaceDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class AssociateL2ConnectionWithPhysicalNetworkInterfacesAction
extends ActionSupport {

  /**
   *
   */
  private static final long serialVersionUID = -6021384373237360601L;

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

  @Action(
    results = {
      @Result(name = "success", location = "dialog.jsp"),
      @Result(name = "noChange", location = "dialog.jsp")
    }
  )
  public String execute() throws Exception {
    if (idList.equals("undefined")) {
      dialogTitleKey
        = "associate.l2Connection.with.physicalNetworkInterfaces.noChange.dialog.title";
      dialogTextKey
        = "associate.l2Connection.with.physicalNetworkInterfaces.noChange.dialog.text";
      return "noChange";
    }

    List<PhysicalNetworkInterface> nifs = new ArrayList<PhysicalNetworkInterface>();
    if (idList != null && idList.length() != 0) {
      for (String strId: idList.split(",")) {
        nifs.add(physicalNetworkInterfaceDao.findByKey(Integer.valueOf(strId)));
      }
    }

    Map<String, Object> params = ActionContext.getContext().getParameters();
    Integer l2Connection_id = Integer.valueOf(((String[])params.get("l2Connection_id"))[0]);
    L2Connection l2Conn = l2ConnectionDao.findByKey(l2Connection_id);

    boolean foundAll = true;
    for (PhysicalNetworkInterface nif: nifs) {
      nif.setL2Connection(l2Conn);

      boolean found = false;
      for (PhysicalNetworkInterface other: l2Conn.getPhysicalNetworkInterfaces()) {
        if (nif.getId().equals(other.getId())) {
          found = true;
          break;
        }
      }
      if (!found) {
        foundAll = false;
      }
    }

    if (foundAll && l2Conn.getPhysicalNetworkInterfaces().size() == nifs.size()) {
      dialogTitleKey
        = "associate.l2Connection.with.physicalNetworkInterfaces.noChange.dialog.title";
      dialogTextKey
        = "associate.l2Connection.with.physicalNetworkInterfaces.noChange.dialog.text";
      return "noChange";
    }

    List<PhysicalNetworkInterface> removedNifs = new ArrayList<PhysicalNetworkInterface>();
    for (PhysicalNetworkInterface nif: l2Conn.getPhysicalNetworkInterfaces()) {
      boolean found = false;
      for (PhysicalNetworkInterface other: nifs) {
        if (nif.getId().equals(other.getId())) {
          found = true;
          break;
        }
      }
      if (!found) {
        nif.setL2Connection(null);
        removedNifs.add(nif);
      }
    }

    l2Conn.setPhysicalNetworkInterfaces(nifs);
    l2ConnectionDao.update(l2Conn);
    physicalNetworkInterfaceDao.update(removedNifs);

    dialogTitleKey
      = "associate.l2Connection.with.physicalNetworkInterfaces.success.dialog.title";
    dialogTextKey
      = "associate.l2Connection.with.physicalNetworkInterfaces.success.dialog.text";
    return "success";
  }

}
