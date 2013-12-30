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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import com.github.kaitoy.sneo.giane.model.AdditionalIpV6RouteGroup;
import com.github.kaitoy.sneo.giane.model.Node;
import com.github.kaitoy.sneo.giane.model.Simulation;
import com.github.kaitoy.sneo.giane.model.dao.NodeDao;
import com.github.kaitoy.sneo.giane.model.dao.SimulationDao;
import com.github.kaitoy.sneo.giane.model.dto.NodeWithAdditionalIpV6RouteGroupDto;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class NodeWithAdditionalIpV6RouteGroupGridAction extends ActionSupport {

  /**
   *
   */
  private static final long serialVersionUID = 1323010448929669272L;

  private static final
  Map<String, Comparator<NodeWithAdditionalIpV6RouteGroupDto>> comparators
    = new HashMap<String, Comparator<NodeWithAdditionalIpV6RouteGroupDto>>();

  static  {
    comparators.put(
      "id",
      new Comparator<NodeWithAdditionalIpV6RouteGroupDto>() {
        public int compare(
          NodeWithAdditionalIpV6RouteGroupDto o1,
          NodeWithAdditionalIpV6RouteGroupDto o2
        ) {
          return o2.getId().compareTo(o1.getId());
        }
      }
    );
    comparators.put(
      "name",
      new Comparator<NodeWithAdditionalIpV6RouteGroupDto>() {
        public int compare(
          NodeWithAdditionalIpV6RouteGroupDto o1,
          NodeWithAdditionalIpV6RouteGroupDto o2
        ) {
          return o2.getName().compareTo(o1.getName());
        }
      }
    );
    comparators.put(
      "additionalIpV6RouteGroup",
      new Comparator<NodeWithAdditionalIpV6RouteGroupDto>() {
        public int compare(
          NodeWithAdditionalIpV6RouteGroupDto o1,
          NodeWithAdditionalIpV6RouteGroupDto o2
        ) {
          return o2.getAdditionalIpV6RouteGroup().compareTo(o1.getAdditionalIpV6RouteGroup());
        }
      }
    );
  }

  private NodeDao nodeDao;
  private SimulationDao simulationDao;

  // result List
  private List<NodeWithAdditionalIpV6RouteGroupDto> gridModel;

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

  @Override
  @Action(
    results = {
      @Result(name = "success", type = "json")
    }
  )
  public String execute() {
    CriteriaBuilder cb = nodeDao.getCriteriaBuilder();
    CriteriaQuery<Node> cq = cb.createQuery(Node.class);
    Root<Node> r = cq.from(Node.class);
    cq.select(r);

    Map<String, Object> params = ActionContext.getContext().getParameters();
    Integer simulationId
      = Integer.valueOf(((String[])params.get("simulation_id"))[0]);
    Simulation conf
      = simulationDao.findByKey(simulationId);

    List<Predicate> predicates = new ArrayList<Predicate>();
    predicates.add(cb.equal(r.get("network"), conf.getNetwork().getId()));

    if (searchField != null) {
      if (searchField.equals("id")) {
        Integer searchValue = Integer.valueOf(searchString);
        if (searchOper.equals("eq")) {
          predicates.add(cb.equal(r.get(searchField), searchValue));
        }
        else if (searchOper.equals("ne")) {
          predicates.add(cb.notEqual(r.get(searchField), searchValue));
        }
        else if (searchOper.equals("lt")) {
          predicates.add(cb.lt(r.get(searchField).as(Integer.class), searchValue));
        }
        else if (searchOper.equals("gt")) {
          predicates.add(cb.gt(r.get(searchField).as(Integer.class), searchValue));
        }
      }
      else if (searchField.equals("name")) {
        if (searchOper.equals("eq")) {
          predicates.add(cb.equal(r.get(searchField), searchString));
        }
        else if (searchOper.equals("ne")) {
          predicates.add(cb.notEqual(r.get(searchField), searchString));
        }
        else if (searchOper.equals("bw")) {
          predicates.add(cb.like(r.get(searchField).as(String.class), searchString + "%"));
        }
        else if (searchOper.equals("ew")) {
          predicates.add(cb.like(r.get(searchField).as(String.class), "%" + searchString));
        }
        else if (searchOper.equals("cn")) {
          predicates.add(cb.like(r.get(searchField).as(String.class), "%" + searchString + "%"));
        }
      }
    }

    cq.where(cb.and(predicates.toArray(new Predicate[0])));

    gridModel =  new ArrayList<NodeWithAdditionalIpV6RouteGroupDto>();
    if (searchField != null && searchField.equals("additionalIpV6RouteGroup")) {
      if (searchOper.equals("eq")) {
        for (Node model: nodeDao.findByCriteria(cq)) {
          AdditionalIpV6RouteGroup routeg = conf.getAdditionalIpV6RouteGroups().get(model);
          if (routeg == null) {
            if (searchString.length() != 0) {
              continue;
            }
          }
          else if (!routeg.getName().equals(searchString)) {
            continue;
          }
          gridModel.add(new NodeWithAdditionalIpV6RouteGroupDto(model, routeg));
        }
      }
      else if (searchOper.equals("ne")) {
        for (Node model: nodeDao.findByCriteria(cq)) {
          AdditionalIpV6RouteGroup routeg = conf.getAdditionalIpV6RouteGroups().get(model);
          if (routeg == null) {
            if (searchString.length() != 0) {
              continue;
            }
          }
          else if (routeg.getName().equals(searchString)) {
            continue;
          }
          gridModel.add(new NodeWithAdditionalIpV6RouteGroupDto(model, routeg));
        }
      }
      else if (searchOper.equals("bw")) {
        for (Node model: nodeDao.findByCriteria(cq)) {
          AdditionalIpV6RouteGroup routeg = conf.getAdditionalIpV6RouteGroups().get(model);
          if (routeg == null) {
            if (searchString.length() != 0) {
              continue;
            }
          }
          else if (!routeg.getName().startsWith(searchString)) {
            continue;
          }
          gridModel.add(new NodeWithAdditionalIpV6RouteGroupDto(model, routeg));
        }
      }
      else if (searchOper.equals("ew")) {
        for (Node model: nodeDao.findByCriteria(cq)) {
          AdditionalIpV6RouteGroup routeg = conf.getAdditionalIpV6RouteGroups().get(model);
          if (routeg == null) {
            if (searchString.length() != 0) {
              continue;
            }
          }
          else if (!routeg.getName().endsWith(searchString)) {
            continue;
          }
          gridModel.add(new NodeWithAdditionalIpV6RouteGroupDto(model, routeg));
        }
      }
      else if (searchOper.equals("cn")) {
        for (Node model: nodeDao.findByCriteria(cq)) {
          AdditionalIpV6RouteGroup routeg = conf.getAdditionalIpV6RouteGroups().get(model);
          if (routeg == null) {
            if (searchString.length() != 0) {
              continue;
            }
          }
          else if (!routeg.getName().contains(searchString)) {
            continue;
          }
          gridModel.add(new NodeWithAdditionalIpV6RouteGroupDto(model, routeg));
        }
      }
    }
    else {
      for (Node model: nodeDao.findByCriteria(cq)) {
        AdditionalIpV6RouteGroup routeg = conf.getAdditionalIpV6RouteGroups().get(model);
        gridModel.add(new NodeWithAdditionalIpV6RouteGroupDto(model, routeg));
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

  public List<NodeWithAdditionalIpV6RouteGroupDto> getGridModel() {
    return gridModel;
  }

  public void setGridModel(List<NodeWithAdditionalIpV6RouteGroupDto> gridModel) {
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