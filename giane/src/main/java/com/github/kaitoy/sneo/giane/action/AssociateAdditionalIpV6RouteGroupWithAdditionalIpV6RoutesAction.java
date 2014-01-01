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
import com.github.kaitoy.sneo.giane.action.message.IpV6RouteMessage;
import com.github.kaitoy.sneo.giane.model.AdditionalIpV6Route;
import com.github.kaitoy.sneo.giane.model.AdditionalIpV6RouteGroup;
import com.github.kaitoy.sneo.giane.model.dao.AdditionalIpV6RouteDao;
import com.github.kaitoy.sneo.giane.model.dao.AdditionalIpV6RouteGroupDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class AssociateAdditionalIpV6RouteGroupWithAdditionalIpV6RoutesAction
extends ActionSupport
implements AssociateActionMessage, IpV6RouteMessage, DialogMessage {

  /**
   *
   */
  private static final long serialVersionUID = 436052566632144060L;

  private static final Logger logger
    = LoggerFactory.getLogger(AssociateAdditionalIpV6RouteGroupWithAdditionalIpV6RoutesAction.class);

  private AdditionalIpV6RouteDao additionalIpV6RouteDao;
  private AdditionalIpV6RouteGroupDao additionalIpV6RouteGroupDao;
  private String idList;
  private String dialogTitleKey;
  private String dialogTextKey;

  // for DI
  public void setAdditionalIpV6RouteDao(AdditionalIpV6RouteDao additionalIpV6RouteDao) {
    this.additionalIpV6RouteDao = additionalIpV6RouteDao;
  }

  // for DI
  public void setAdditionalIpV6RouteGroupDao(AdditionalIpV6RouteGroupDao additionalIpV6RouteGroupDao) {
    this.additionalIpV6RouteGroupDao = additionalIpV6RouteGroupDao;
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
      List<AdditionalIpV6Route> additionalIpV6Routes = new ArrayList<AdditionalIpV6Route>();
      if (idList != null && idList.length() != 0) {
        for (String strId: idList.split(",")) {
          additionalIpV6Routes.add(additionalIpV6RouteDao.findByKey(Integer.valueOf(strId)));
        }
      }

      Map<String, Object> params = ActionContext.getContext().getParameters();
      Integer additionalIpV6RouteGroup_id = Integer.valueOf(((String[])params.get("additionalIpV6RouteGroup_id"))[0]);
      AdditionalIpV6RouteGroup additionalIpV6RouteGroup = additionalIpV6RouteGroupDao.findByKey(additionalIpV6RouteGroup_id);

      boolean foundAll = true;
      for (AdditionalIpV6Route additionalIpV6Route: additionalIpV6Routes) {
        boolean found = false;
        for (AdditionalIpV6Route other: additionalIpV6RouteGroup.getAdditionalIpV6Routes()) {
          if (additionalIpV6Route.getId().equals(other.getId())) {
            found = true;
            break;
          }
        }
        if (!found) {
          foundAll = false;
        }
      }

      if (foundAll && additionalIpV6RouteGroup.getAdditionalIpV6Routes().size() == additionalIpV6Routes.size()) {
        dialogTitleKey
          = "dialog.title.result";
        dialogTextKey
          = "dialog.text.association.noChange";
        return "noChange";
      }

      additionalIpV6RouteGroup.setAdditionalIpV6Routes(additionalIpV6Routes);
      additionalIpV6RouteGroupDao.update(additionalIpV6RouteGroup);

      dialogTitleKey
        = "dialog.title.result";
      dialogTextKey
        = "dialog.text.association.success";
      return "success";
    } catch (Exception e) {
      logger.error("An error occurred: ", e);
      dialogTitleKey = "dialog.title.result";
      dialogTextKey = "dialog.text.association.error";
      return "error";
    }
  }

  @Action(
    value = "associate-additional-ip-v6-route-group-with-additional-ip-v6-routes-grid-box",
    results = { @Result(name = "grid", location = "associate-additional-ip-v6-route-group-with-additional-ip-v6-routes-grid.jsp")}
  )
  @SkipValidation
  public String associationdGrid() throws Exception {
    return "grid";
  }

}
