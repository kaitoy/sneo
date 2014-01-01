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
import com.github.kaitoy.sneo.giane.action.message.DialogMessage;
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.github.kaitoy.sneo.giane.action.message.NodeMessage;
import com.github.kaitoy.sneo.giane.interceptor.GoingBackward;
import com.github.kaitoy.sneo.giane.interceptor.GoingForward;
import com.github.kaitoy.sneo.giane.model.AdditionalIpV4RouteGroup;
import com.github.kaitoy.sneo.giane.model.AdditionalIpV6RouteGroup;
import com.github.kaitoy.sneo.giane.model.Node;
import com.github.kaitoy.sneo.giane.model.dao.AdditionalIpV4RouteGroupDao;
import com.github.kaitoy.sneo.giane.model.dao.AdditionalIpV6RouteGroupDao;
import com.github.kaitoy.sneo.giane.model.dao.NetworkDao;
import com.github.kaitoy.sneo.giane.model.dao.NodeDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class NodeAction extends ActionSupport
implements ModelDriven<Node>,  ParameterAware,
  FormMessage, NodeMessage, BreadCrumbsMessage, DialogMessage {

  /**
   *
   */
  private static final long serialVersionUID = -2836536393523178033L;

  private Node model = new Node();
  private Map<String, String[]> parameters;
  private NodeDao nodeDao;
  private NetworkDao networkDao;
  private AdditionalIpV4RouteGroupDao additionalIpV4RouteGroupDao;
  private AdditionalIpV6RouteGroupDao additionalIpV6RouteGroupDao;
  private String uniqueColumn;
  private String uniqueDomain;
  private String deletingIdList;

  public Node getModel() { return model; }

  public void setParameters(Map<String, String[]> parameters) {
    this.parameters = parameters;
  }

  @VisitorFieldValidator(appendPrefix = true)
  public void setModel(Node model) { this.model = model; }

  // for DI
  public void setNodeDao(NodeDao nodeDao) {
    this.nodeDao = nodeDao;
  }

  // for DI
  public void setNetworkDao(NetworkDao networkDao) {
    this.networkDao = networkDao;
  }

  // for DI
  public void setAdditionalIpV4RouteGroupDao(AdditionalIpV4RouteGroupDao additionalIpV4RouteGroupDao) {
    this.additionalIpV4RouteGroupDao = additionalIpV4RouteGroupDao;
  }

  //for DI
  public void setAdditionalIpV6RouteGroupDao(AdditionalIpV6RouteGroupDao additionalIpV6RouteGroupDao) {
    this.additionalIpV6RouteGroupDao = additionalIpV6RouteGroupDao;
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

  public Map<Integer, String> getAdditionalIpV4RouteGroups() {
    Map<Integer, String> map = new HashMap<Integer, String>();
    for (AdditionalIpV4RouteGroup routeg: additionalIpV4RouteGroupDao.list()) {
      map.put(
        routeg.getId(),
        routeg.getName()
      );
    }
    return map;
  }

  public Map<Integer, String> getAdditionalIpV6RouteGroups() {
    Map<Integer, String> map = new HashMap<Integer, String>();
    for (AdditionalIpV6RouteGroup routeg: additionalIpV6RouteGroupDao.list()) {
      map.put(
        routeg.getId(),
        routeg.getName()
      );
    }
    return map;
  }

  @Override
  @GoingForward
  public String execute() throws Exception {
    ValueStack stack = ActionContext.getContext().getValueStack();
    Map<String, Object> valueMap = new HashMap<String, Object>();
    valueMap.put("node_id", model.getId());
    valueMap.put("node_name", model.getName());
    stack.push(valueMap);

    return "config";
  }

  @Action(
    value = "back-to-node-config",
    results = { @Result(name = "config", location = "node-config.jsp")}
  )
  @SkipValidation
  @GoingBackward
  public String back() throws Exception {
    ValueStack stack = ActionContext.getContext().getValueStack();
    Map<String, Object> valueMap = new HashMap<String, Object>();
    valueMap.put("node_id", parameters.get("node_id")[0]);
    valueMap.put("node_name", parameters.get("node_name")[0]);
    stack.push(valueMap);

    return "config";
  }

  @Action(
    value = "node-tab-content",
    results = { @Result(name = "tab", location = "node-tab-content.jsp") }
  )
  @SkipValidation
  public String tab() throws Exception {
    return "tab";
  }

  @Action(
    value = "set-additional-ip-v4-route-group-to-node-tab-content",
    results = { @Result(name = "tab", location = "set-additional-ip-v4-route-group-to-node-tab-content.jsp")}
  )
  @SkipValidation
  public String setAdditionalIpV4RouteGroupTab() throws Exception {
    return "tab";
  }

  @Action(
    value = "set-additional-ip-v6-route-group-to-node-tab-content",
    results = { @Result(name = "tab", location = "set-additional-ip-v6-route-group-to-node-tab-content.jsp")}
  )
  @SkipValidation
  public String setAdditionalIpV6RouteGroupTab() throws Exception {
    return "tab";
  }

  @Action(
    value = "node-create",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  public String create() throws Exception {
    Integer network_id = Integer.valueOf(parameters.get("network_id")[0]);
    model.setNetwork(networkDao.findByKey(network_id));
    nodeDao.create(model);

    return "success";
  }

  @Action(
    value = "node-update",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  public String update() throws Exception {
    Node update = nodeDao.findByKey(model.getId());
    update.setName(model.getName());
    update.setTtl(model.getTtl());
    update.setDescr(model.getDescr());
    nodeDao.update(update);

    return "success";
  }

  @Action(
    value = "node-delete",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  @SkipValidation
  public String delete() throws Exception {
    List<Node> deletingList = new ArrayList<Node>();
    for (String idStr: deletingIdList.split(",")) {
      deletingList.add(nodeDao.findByKey(Integer.valueOf(idStr)));
    }
    nodeDao.delete(deletingList);
    return "success";
  }

  @Override
  public void validate() {
    String contextName = ActionContext.getContext().getName();

    if (contextName.equals("node")) {
      if (model.getId() == null) {
        addActionError(getText("select.a.row"));
        return;
      }
    }

    if (contextName.equals("node-update")) {
      if (model.getId() == null) {
        addActionError(getText("select.a.row"));
        return;
      }

      if (model.getName() != null) {
        Map<String, Object> params = ActionContext.getContext().getParameters();
        Integer networkId
          = Integer.valueOf(((String[])params.get("network_id"))[0]);

        Node someone
          = nodeDao.findByNameAndNetworkId(model.getName(), networkId);
        if (someone != null && !someone.getId().equals(model.getId())) {
          uniqueDomain = getText("node.network.label");
          uniqueColumn = getText("node.name.label");
          addFieldError("name", getText("need.to.be.unique.in.domain"));
          return;
        }
      }
    }

    if (contextName.equals("node-create")) {
      Map<String, Object> params = ActionContext.getContext().getParameters();
      Integer networkId
        = Integer.valueOf(((String[])params.get("network_id"))[0]);
      if (
           model.getName() != null
        && nodeDao
             .findByNameAndNetworkId(model.getName(), networkId) != null
      ) {
        uniqueDomain = getText("node.network.label");
        uniqueColumn = getText("node.name.label");
        addFieldError("name", getText("need.to.be.unique.in.domain"));
        return;
      }
    }
  }

}
