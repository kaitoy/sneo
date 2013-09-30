/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.action;

import java.util.HashMap;
import java.util.Map;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import com.github.kaitoy.sneo.giane.model.AdditionalIpV4RouteGroup;
import com.github.kaitoy.sneo.giane.model.Node;
import com.github.kaitoy.sneo.giane.model.dao.AdditionalIpV4RouteGroupDao;
import com.github.kaitoy.sneo.giane.model.dao.NetworkDao;
import com.github.kaitoy.sneo.giane.model.dao.NodeDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class NodeAction extends ActionSupport implements ModelDriven<Node> {

  /**
   *
   */
  private static final long serialVersionUID = -2836536393523178033L;

  private Node model = new Node();
  private NodeDao nodeDao;
  private NetworkDao networkDao;
  private AdditionalIpV4RouteGroupDao additionalIpV4RouteGroupDao;
  private String uniqueColumn;
  private String uniqueDomain;

  public Node getModel() { return model; }

  @VisitorFieldValidator(appendPrefix = false)
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

  public String getUniqueColumn() {
    return uniqueColumn;
  }

  public String getUniqueDomain() {
    return uniqueDomain;
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

  @Override
  @SkipValidation
  public String execute() throws Exception {
    // The following code is different from ActionContext.getContext().getParameters()
    // and ActionContext.getContext().setParameters() seems not to work.
    @SuppressWarnings("unchecked")
    Map<String, Object> parameters
      = (Map<String, Object>)ActionContext.getContext().get("parameters");
    if (parameters.get("network_id") == null) {
      setModel(nodeDao.findByKey(model.getId()));
      parameters.put("network_id", model.getNetwork().getId());
      parameters.put("network_name", model.getNetwork().getName());
      parameters.put("node_id", model.getId());
      parameters.put("node_name", model.getName());
    }

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
    value = "node-create",
    results = { @Result(name = "success", location = "empty.jsp") }
  )
  public String create() throws Exception {
    Map<String, Object> params = ActionContext.getContext().getParameters();
    Integer network_id = Integer.valueOf(((String[])params.get("network_id"))[0]);
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

  @Override
  public void validate() {
    String contextName = ActionContext.getContext().getName();

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
          addActionError(getText("need.to.be.unique.in.domain"));
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
        addActionError(getText("need.to.be.unique.in.domain"));
        return;
      }
    }
  }

}
