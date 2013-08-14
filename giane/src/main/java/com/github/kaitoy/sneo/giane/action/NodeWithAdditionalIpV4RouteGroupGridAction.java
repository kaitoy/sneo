/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.github.kaitoy.sneo.giane.model.AdditionalIpV4RouteGroup;
import com.github.kaitoy.sneo.giane.model.Node;
import com.github.kaitoy.sneo.giane.model.Simulation;
import com.github.kaitoy.sneo.giane.model.dao.NodeDao;
import com.github.kaitoy.sneo.giane.model.dao.SimulationDao;
import com.github.kaitoy.sneo.giane.model.dto.NodeWithAdditionalIpV4RouteGroupDto;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class NodeWithAdditionalIpV4RouteGroupGridAction extends ActionSupport {

  /**
   * 
   */
  private static final long serialVersionUID = 8348410625009416488L;
  
  private static final
  Map<String, Comparator<NodeWithAdditionalIpV4RouteGroupDto>> comparators
    = new HashMap<String, Comparator<NodeWithAdditionalIpV4RouteGroupDto>>();

  static  {
    comparators.put(
      "id",
      new Comparator<NodeWithAdditionalIpV4RouteGroupDto>() {
        public int compare(
          NodeWithAdditionalIpV4RouteGroupDto o1,
          NodeWithAdditionalIpV4RouteGroupDto o2
        ) {
          return o2.getId().compareTo(o1.getId());
        }
      }
    );
    comparators.put(
      "name",
      new Comparator<NodeWithAdditionalIpV4RouteGroupDto>() {
        public int compare(
          NodeWithAdditionalIpV4RouteGroupDto o1,
          NodeWithAdditionalIpV4RouteGroupDto o2
        ) {
          return o2.getName().compareTo(o1.getName());
        }
      }
    );
    comparators.put(
      "additionalIpV4RouteGroup",
      new Comparator<NodeWithAdditionalIpV4RouteGroupDto>() {
        public int compare(
          NodeWithAdditionalIpV4RouteGroupDto o1,
          NodeWithAdditionalIpV4RouteGroupDto o2
        ) {
          return o2.getAdditionalIpV4RouteGroup().compareTo(o1.getAdditionalIpV4RouteGroup());
        }
      }
    );
  }

  private NodeDao nodeDao;
  private SimulationDao simulationDao;

  // result List
  private List<NodeWithAdditionalIpV4RouteGroupDto> gridModel;

  // get how many rows we want to have into the grid - rowNum attribute in the grid
  private Integer rows = 0;

  // Get the requested page. By default grid sets this to 1.
  private Integer page = 0;

  // sorting order - asc or desc
  private String sord;

  // get index row - i.e. user click to sort.
  private String sidx;

  // Search Field
  private String searchField;

  // The Search String
  private String searchString;

  // The Search Operation ['eq','ne','lt','le','gt','ge','bw','bn','in','ni','ew','en','cn','nc']
  private String searchOper;

  private boolean loadonce = false;

  // Total Pages
  private Integer total = 0;

  // All Record
  private Integer records = 0;

  // for DI
  public void setNodeDao(NodeDao nodeDao) {
    this.nodeDao = nodeDao;
  }

  // for DI
  public void setSimulationDao(
    SimulationDao simulationDao
  ) {
    this.simulationDao = simulationDao;
  }

  @Action(
    results = {
      @Result(name = "success", type = "json")
    }
  )
  public String execute() {
    DetachedCriteria criteria = DetachedCriteria.forClass(Node.class);

    Map<String, Object> params = ActionContext.getContext().getParameters();
    Integer simulationId
      = Integer.valueOf(((String[])params.get("simulation_id"))[0]);
    Simulation conf
      = simulationDao.findByKey(simulationId);

    criteria.add(Restrictions.eq("network.id", conf.getNetwork().getId()));

    if (searchField != null) {
      if (searchField.equals("id")) {
        Integer searchValue = Integer.valueOf(searchString);
        if (searchOper.equals("eq")) {
          criteria.add(Restrictions.eq(searchField, searchValue));
        }
        else if (searchOper.equals("ne")) {
          criteria.add(Restrictions.ne(searchField, searchValue));
        }
        else if (searchOper.equals("lt")) {
          criteria.add(Restrictions.lt(searchField, searchValue));
        }
        else if (searchOper.equals("gt")) {
          criteria.add(Restrictions.gt(searchField, searchValue));
        }
      }
      else if (searchField.equals("name")) {
        if (searchOper.equals("eq")) {
          criteria.add(Restrictions.eq(searchField, searchString));
        }
        else if (searchOper.equals("ne")) {
          criteria.add(Restrictions.ne(searchField, searchString));
        }
        else if (searchOper.equals("bw")) {
          criteria.add(Restrictions.like(searchField, searchString + "%"));
        }
        else if (searchOper.equals("ew")) {
          criteria.add(Restrictions.like(searchField, "%" + searchString));
        }
        else if (searchOper.equals("cn")) {
          criteria.add(Restrictions.like(searchField, "%" + searchString + "%"));
        }
      }
    }

    gridModel =  new ArrayList<NodeWithAdditionalIpV4RouteGroupDto>();
    if (searchField != null && searchField.equals("additionalIpV4RouteGroup")) {
      if (searchOper.equals("eq")) {
        for (Node model: nodeDao.findByCriteria(criteria)) {
          AdditionalIpV4RouteGroup ttg = conf.getAdditionalIpV4RouteGroups().get(model);
          if (ttg == null) {
            if (!"".equals(searchString)) {
              continue;
            }
          }
          if (!ttg.getName().equals(searchString)) {
            continue;
          }
          gridModel.add(new NodeWithAdditionalIpV4RouteGroupDto(model, ttg));
        }
      }
      else if (searchOper.equals("ne")) {
        for (Node model: nodeDao.findByCriteria(criteria)) {
          AdditionalIpV4RouteGroup ttg = conf.getAdditionalIpV4RouteGroups().get(model);
          if (ttg == null) {
            if ("".equals(searchString)) {
              continue;
            }
          }
          if (ttg.getName().equals(searchString)) {
            continue;
          }
          gridModel.add(new NodeWithAdditionalIpV4RouteGroupDto(model, ttg));
        }
      }
      else if (searchOper.equals("bw")) {
        for (Node model: nodeDao.findByCriteria(criteria)) {
          AdditionalIpV4RouteGroup ttg = conf.getAdditionalIpV4RouteGroups().get(model);
          if (ttg == null) {
            if (!"".startsWith(searchString)) {
              continue;
            }
          }
          if (!ttg.getName().startsWith(searchString)) {
            continue;
          }
          gridModel.add(new NodeWithAdditionalIpV4RouteGroupDto(model, ttg));
        }
      }
      else if (searchOper.equals("ew")) {
        for (Node model: nodeDao.findByCriteria(criteria)) {
          AdditionalIpV4RouteGroup ttg = conf.getAdditionalIpV4RouteGroups().get(model);
          if (ttg == null) {
            if (!"".endsWith(searchString)) {
              continue;
            }
          }
          if (!ttg.getName().endsWith(searchString)) {
            continue;
          }
          gridModel.add(new NodeWithAdditionalIpV4RouteGroupDto(model, ttg));
        }
      }
      else if (searchOper.equals("cn")) {
        for (Node model: nodeDao.findByCriteria(criteria)) {
          AdditionalIpV4RouteGroup ttg = conf.getAdditionalIpV4RouteGroups().get(model);
          if (ttg == null) {
            if (!"".contains(searchString)) {
              continue;
            }
          }
          if (!ttg.getName().contains(searchString)) {
            continue;
          }
          gridModel.add(new NodeWithAdditionalIpV4RouteGroupDto(model, ttg));
        }
      }
    }
    else {
      for (Node model: nodeDao.findByCriteria(criteria)) {
        AdditionalIpV4RouteGroup ttg = conf.getAdditionalIpV4RouteGroups().get(model);
        gridModel.add(new NodeWithAdditionalIpV4RouteGroupDto(model, ttg));
      }
    }

    records = gridModel.size();

    if (sord != null && sord.length() != 0 && sidx != null && sidx.length() != 0) {
      Collections.sort(gridModel, comparators.get(sidx));
      if (sord.equalsIgnoreCase("desc")) {
        Collections.reverse(gridModel);
      }
    }

    if (!loadonce){
      int to = (rows * page);
      int from = to - rows;
      if (to > records) {
        to = records;
      }
      gridModel = gridModel.subList(from, to);
    }

    // calculate the total pages for the query
    total = (int)Math.ceil((double)records / (double)rows);

    return "success";
  }

  public String getJSON() { return execute(); }

  public List<NodeWithAdditionalIpV4RouteGroupDto> getGridModel() {
    return gridModel;
  }

  public void setGridModel(List<NodeWithAdditionalIpV4RouteGroupDto> gridModel) {
    this.gridModel = gridModel;
  }

  public Integer getRows() {
    return rows;
  }

  public void setRows(Integer rows) {
    this.rows = rows;
  }

  public Integer getPage() {
    return page;
  }

  public void setPage(Integer page) {
    this.page = page;
  }

  public String getSord() {
    return sord;
  }

  public void setSord(String sord) {
    this.sord = sord;
  }


  public String getSidx() {
    return sidx;
  }

  public void setSidx(String sidx) {
    this.sidx = sidx;
  }

  public String getSearchField() {
    return searchField;
  }

  public void setSearchField(String searchField) {
    this.searchField = searchField;
  }

  public String getSearchString() {
    return searchString;
  }

  public void setSearchString(String searchString) {
    this.searchString = searchString;
  }

  public String getSearchOper() {
    return searchOper;
  }

  public void setSearchOper(String searchOper) {
    this.searchOper = searchOper;
  }

  public boolean isLoadonce() {
    return loadonce;
  }

  public void setLoadonce(boolean loadonce) {
    this.loadonce = loadonce;
  }

  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }

  public Integer getRecords() {
    return records;
  }

  public void setRecords(Integer records) {
    this.records = records;
  }

}