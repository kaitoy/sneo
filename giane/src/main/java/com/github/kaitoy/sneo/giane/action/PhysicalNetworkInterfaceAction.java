/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.ParameterAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import com.github.kaitoy.sneo.giane.action.message.BreadCrumbsMessage;
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.github.kaitoy.sneo.giane.action.message.PhysicalNetworkInterfaceMessage;
import com.github.kaitoy.sneo.giane.interceptor.GoingBackward;
import com.github.kaitoy.sneo.giane.interceptor.GoingForward;
import com.github.kaitoy.sneo.giane.model.PhysicalNetworkInterface;
import com.github.kaitoy.sneo.giane.model.PhysicalNetworkInterfaceIpAddressRelation;
import com.github.kaitoy.sneo.giane.model.dao.IpAddressRelationDao;
import com.github.kaitoy.sneo.giane.model.dao.NodeDao;
import com.github.kaitoy.sneo.giane.model.dao.PhysicalNetworkInterfaceDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class PhysicalNetworkInterfaceAction
extends ActionSupport
implements ModelDriven<PhysicalNetworkInterface>, ParameterAware, FormMessage,
  PhysicalNetworkInterfaceMessage, BreadCrumbsMessage {

  /**
   *
   */
  private static final long serialVersionUID = 695790906346600143L;

  private PhysicalNetworkInterface model = new PhysicalNetworkInterface();
  private Map<String, String[]> parameters;
  private PhysicalNetworkInterfaceDao physicalNetworkInterfaceDao;
  private IpAddressRelationDao ipAddressRelationDao;
  private NodeDao nodeDao;
  private String uniqueColumn;
  private String uniqueDomain;
  private String deletingIdList;

  public PhysicalNetworkInterface getModel() { return model; }

  @VisitorFieldValidator(appendPrefix = true)
  public void setModel(PhysicalNetworkInterface model) { this.model = model; }

  public void setParameters(Map<String, String[]> parameters) {
    this.parameters = parameters;
  }

  // for DI
  public void setPhysicalNetworkInterfaceDao(
    PhysicalNetworkInterfaceDao physicalNetworkInterfaceDao
  ) {
    this.physicalNetworkInterfaceDao = physicalNetworkInterfaceDao;
  }

  // for DI
  public void setIpAddressRelationDao(IpAddressRelationDao ipAddressRelationDao) {
    this.ipAddressRelationDao = ipAddressRelationDao;
  }

  // for DI
  public void setNodeDao(NodeDao nodeDao) {
    this.nodeDao = nodeDao;
  }

  public String getUniqueColumn() {
    return uniqueColumn;
  }

  public String getUniqueDomain() {
    return uniqueDomain;
  }

  public void setDeletingIdList(String deletingIdList) {
    this.deletingIdList = deletingIdList;
  }

  @Override
  @GoingForward
  public String execute() throws Exception {
    ValueStack stack = ActionContext.getContext().getValueStack();
    Map<String, Object> valueMap = new HashMap<String, Object>();
    setModel(physicalNetworkInterfaceDao.findByKey(model.getId()));
    valueMap.put("physicalNetworkInterface_name", model.getName());
    valueMap.put(
      "ipAddressRelation_id", model.getIpAddressRelation().getId()
    );
    stack.push(valueMap);

    return "config";
  }

  @Action(
    value = "back-to-physical-network-interface-config",
    results = { @Result(name = "config", location = "physical-network-interface-config.jsp")}
  )
  @SkipValidation
  @GoingBackward
  public String back() throws Exception {
    ValueStack stack = ActionContext.getContext().getValueStack();
    Map<String, Object> valueMap = new HashMap<String, Object>();
    valueMap.put("physicalNetworkInterface_name", parameters.get("physicalNetworkInterface_name")[0]);
    valueMap.put("ipAddressRelation_id", parameters.get("ipAddressRelation_id")[0]);
    stack.push(valueMap);

    return "config";
  }

  @Action(
    value = "physical-network-interface-tab-content",
    results = { @Result(name = "tab", location = "physical-network-interface-tab-content.jsp")}
  )
  @SkipValidation
  public String tab() throws Exception {
    return "tab";
  }

  @Action(
    value = "physical-network-interface-create",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  public String create() throws Exception {
    Integer nodeId = Integer.valueOf(parameters.get("node_id")[0]);
    model.setNode(nodeDao.findByKey(nodeId));

    PhysicalNetworkInterfaceIpAddressRelation relation
      = new PhysicalNetworkInterfaceIpAddressRelation();
    relation.setPhysicalNetworkInterface(model);
    ipAddressRelationDao.create(relation);

    model.setIpAddressRelation(relation);
    physicalNetworkInterfaceDao.create(model);

    return "success";
  }

  @Action(
    value = "physical-network-interface-update",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  public String update() throws Exception {
    PhysicalNetworkInterface update
      = physicalNetworkInterfaceDao.findByKey(model.getId());
    update.setName(model.getName());
    update.setTrunk(model.isTrunk());
    physicalNetworkInterfaceDao.update(update);

    return "success";
  }

  @Action(
    value = "physical-network-interface-delete",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  @SkipValidation
  public String delete() throws Exception {
    List<PhysicalNetworkInterface> deletingList = new ArrayList<PhysicalNetworkInterface>();
    for (String idStr: deletingIdList.split(",")) {
      deletingList.add(physicalNetworkInterfaceDao.findByKey(Integer.valueOf(idStr)));
    }
    physicalNetworkInterfaceDao.delete(deletingList);
    return "success";
  }

  @Override
  public void validate() {
    String contextName = ActionContext.getContext().getName();

    if (contextName.equals("physical-network-interface")) {
      if (model.getId() == null) {
        addActionError(getText("select.a.row"));
        return;
      }
    }

    if (contextName.equals("physical-network-interface-update")) {
      if (model.getId() == null) {
        addActionError(getText("select.a.row"));
        return;
      }

      if (model.getName() != null) {
        Map<String, Object> params = ActionContext.getContext().getParameters();
        Integer nodeId = Integer.valueOf(((String[])params.get("node_id"))[0]);

        PhysicalNetworkInterface someone
          = physicalNetworkInterfaceDao
              .findByNameAndNodeId(model.getName(), nodeId);
        if (someone != null && !someone.getId().equals(model.getId())) {
          uniqueDomain = getText("physicalNetworkInterface.node.label");
          uniqueColumn = getText("physicalNetworkInterface.name.label");
          addFieldError("name", getText("need.to.be.unique.in.domain"));
          return;
        }
      }
    }

    if (contextName.equals("physical-network-interface-create")) {
      Map<String, Object> params = ActionContext.getContext().getParameters();
      Integer nodeId = Integer.valueOf(((String[])params.get("node_id"))[0]);
      if (
           model.getName() != null
        && physicalNetworkInterfaceDao
             .findByNameAndNodeId(model.getName(), nodeId) != null
      ) {
        uniqueDomain = getText("physicalNetworkInterface.node.label");
        uniqueColumn = getText("physicalNetworkInterface.name.label");
        addFieldError("name", getText("need.to.be.unique.in.domain"));
        return;
      }
    }
  }

}
