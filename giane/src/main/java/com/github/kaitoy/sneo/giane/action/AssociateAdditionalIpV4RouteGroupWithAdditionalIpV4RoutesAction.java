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
import com.github.kaitoy.sneo.giane.action.message.IpV4RouteMessage;
import com.github.kaitoy.sneo.giane.model.AdditionalIpV4Route;
import com.github.kaitoy.sneo.giane.model.AdditionalIpV4RouteGroup;
import com.github.kaitoy.sneo.giane.model.dao.AdditionalIpV4RouteDao;
import com.github.kaitoy.sneo.giane.model.dao.AdditionalIpV4RouteGroupDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class AssociateAdditionalIpV4RouteGroupWithAdditionalIpV4RoutesAction
extends ActionSupport
implements AssociateActionMessage, IpV4RouteMessage {

  /**
   *
   */
  private static final long serialVersionUID = -1568203010254123584L;

  private static final Logger logger
    = LoggerFactory.getLogger(AssociateAdditionalIpV4RouteGroupWithAdditionalIpV4RoutesAction.class);

  private AdditionalIpV4RouteDao additionalIpV4RouteDao;
  private AdditionalIpV4RouteGroupDao additionalIpV4RouteGroupDao;
  private String idList;
  private String dialogTitleKey;
  private String dialogTextKey;

  // for DI
  public void setAdditionalIpV4RouteDao(AdditionalIpV4RouteDao additionalIpV4RouteDao) {
    this.additionalIpV4RouteDao = additionalIpV4RouteDao;
  }

  // for DI
  public void setAdditionalIpV4RouteGroupDao(AdditionalIpV4RouteGroupDao additionalIpV4RouteGroupDao) {
    this.additionalIpV4RouteGroupDao = additionalIpV4RouteGroupDao;
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
        = "associateAction.noChange.dialog.title";
      dialogTextKey
        = "associateAction.noChange.dialog.text";
      return "noChange";
    }

    try {
      List<AdditionalIpV4Route> additionalIpV4Routes = new ArrayList<AdditionalIpV4Route>();
      if (idList != null && idList.length() != 0) {
        for (String strId: idList.split(",")) {
          additionalIpV4Routes.add(additionalIpV4RouteDao.findByKey(Integer.valueOf(strId)));
        }
      }

      Map<String, Object> params = ActionContext.getContext().getParameters();
      Integer additionalIpV4RouteGroup_id = Integer.valueOf(((String[])params.get("additionalIpV4RouteGroup_id"))[0]);
      AdditionalIpV4RouteGroup additionalIpV4RouteGroup = additionalIpV4RouteGroupDao.findByKey(additionalIpV4RouteGroup_id);

      boolean foundAll = true;
      for (AdditionalIpV4Route additionalIpV4Route: additionalIpV4Routes) {
        boolean found = false;
        for (AdditionalIpV4Route other: additionalIpV4RouteGroup.getAdditionalIpV4Routes()) {
          if (additionalIpV4Route.getId().equals(other.getId())) {
            found = true;
            break;
          }
        }
        if (!found) {
          foundAll = false;
        }
      }

      if (foundAll && additionalIpV4RouteGroup.getAdditionalIpV4Routes().size() == additionalIpV4Routes.size()) {
        dialogTitleKey
          = "associateAction.noChange.dialog.title";
        dialogTextKey
          = "associateAction.noChange.dialog.text";
        return "noChange";
      }

      additionalIpV4RouteGroup.setAdditionalIpV4Routes(additionalIpV4Routes);
      additionalIpV4RouteGroupDao.update(additionalIpV4RouteGroup);

      dialogTitleKey
        = "associateAction.success.dialog.title";
      dialogTextKey
        = "associateAction.success.dialog.text";
      return "success";
    } catch (Exception e) {
      logger.error("An error occurred: ", e);
      dialogTitleKey = "associateAction.error.dialog.title";
      dialogTextKey = "associateAction.error.dialog.text";
      return "error";
    }
  }

  @Action(
    value = "associate-additional-ip-v4-route-group-with-additional-ip-v4-routes-grid-box",
    results = { @Result(name = "grid", location = "associate-additional-ip-v4-route-group-with-additional-ip-v4-routes-grid.jsp")}
  )
  @SkipValidation
  public String associationdGrid() throws Exception {
    return "grid";
  }

}
