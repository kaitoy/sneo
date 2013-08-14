/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
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

import com.github.kaitoy.sneo.giane.model.RealNetworkInterface;
import com.github.kaitoy.sneo.giane.model.RealNetworkInterfaceConfiguration;
import com.github.kaitoy.sneo.giane.model.Simulation;
import com.github.kaitoy.sneo.giane.model.dao.RealNetworkInterfaceDao;
import com.github.kaitoy.sneo.giane.model.dao.SimulationDao;
import com.github.kaitoy.sneo.giane.model.dto.RealNetworkInterfaceWithRealNetworkInterfaceConfigurationDto;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class RealNetworkInterfaceWithRealNetworkInterfaceConfigurationGridAction
extends ActionSupport {

  /**
   *
   */
  private static final long serialVersionUID = 7024619322431261499L;

  private static final
  Map<String, Comparator<RealNetworkInterfaceWithRealNetworkInterfaceConfigurationDto>> comparators
    = new HashMap<String, Comparator<RealNetworkInterfaceWithRealNetworkInterfaceConfigurationDto>>();

  static  {
    comparators.put(
      "id",
      new Comparator<RealNetworkInterfaceWithRealNetworkInterfaceConfigurationDto>() {
        public int compare(
          RealNetworkInterfaceWithRealNetworkInterfaceConfigurationDto o1,
          RealNetworkInterfaceWithRealNetworkInterfaceConfigurationDto o2
        ) {
          return o2.getId().compareTo(o1.getId());
        }
      }
    );
    comparators.put(
      "name",
      new Comparator<RealNetworkInterfaceWithRealNetworkInterfaceConfigurationDto>() {
        public int compare(
          RealNetworkInterfaceWithRealNetworkInterfaceConfigurationDto o1,
          RealNetworkInterfaceWithRealNetworkInterfaceConfigurationDto o2
        ) {
          return o2.getName().compareTo(o1.getName());
        }
      }
    );
    comparators.put(
      "realNetworkInterfaceConfiguration",
      new Comparator<RealNetworkInterfaceWithRealNetworkInterfaceConfigurationDto>() {
        public int compare(
          RealNetworkInterfaceWithRealNetworkInterfaceConfigurationDto o1,
          RealNetworkInterfaceWithRealNetworkInterfaceConfigurationDto o2
        ) {
          return o2.getRealNetworkInterfaceConfiguration()
                   .compareTo(o1.getRealNetworkInterfaceConfiguration());
        }
      }
    );
  }

  private RealNetworkInterfaceDao realNetworkInterfaceDao;
  private SimulationDao simulationDao;

  // result List
  private List<RealNetworkInterfaceWithRealNetworkInterfaceConfigurationDto> gridModel;

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
  public void setRealNetworkInterfaceDao(
    RealNetworkInterfaceDao realNetworkInterfaceDao
  ) {
    this.realNetworkInterfaceDao = realNetworkInterfaceDao;
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
    DetachedCriteria criteria = DetachedCriteria.forClass(RealNetworkInterface.class);

    Map<String, Object> params = ActionContext.getContext().getParameters();
    Integer simulationId
      = Integer.valueOf(((String[])params.get("simulation_id"))[0]);
    Simulation simConf
      = simulationDao.findByKey(simulationId);

    criteria.createCriteria("node")
      .add(Restrictions.eq("network.id", simConf.getNetwork().getId()));

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

    gridModel
      =  new ArrayList<RealNetworkInterfaceWithRealNetworkInterfaceConfigurationDto>();
    if (searchField != null && searchField.equals("realNetworkInterfaceConfiguration")) {
      if (searchOper.equals("eq")) {
        for (RealNetworkInterface model: realNetworkInterfaceDao.findByCriteria(criteria)) {
          RealNetworkInterfaceConfiguration nifConf
            = simConf.getRealNetworkInterfaceConfigurations().get(model);
          if (nifConf == null) {
            if (!"".equals(searchString)) {
              continue;
            }
          }
          if (!nifConf.getName().equals(searchString)) {
            continue;
          }
          gridModel.add(
            new RealNetworkInterfaceWithRealNetworkInterfaceConfigurationDto(model, nifConf)
          );
        }
      }
      else if (searchOper.equals("ne")) {
        for (RealNetworkInterface model: realNetworkInterfaceDao.findByCriteria(criteria)) {
          RealNetworkInterfaceConfiguration nifConf
            = simConf.getRealNetworkInterfaceConfigurations().get(model);
          if (nifConf == null) {
            if ("".equals(searchString)) {
              continue;
            }
          }
          if (nifConf.getName().equals(searchString)) {
            continue;
          }
          gridModel.add(
            new RealNetworkInterfaceWithRealNetworkInterfaceConfigurationDto(model, nifConf)
          );
        }
      }
      else if (searchOper.equals("bw")) {
        for (RealNetworkInterface model: realNetworkInterfaceDao.findByCriteria(criteria)) {
          RealNetworkInterfaceConfiguration nifConf
            = simConf.getRealNetworkInterfaceConfigurations().get(model);
          if (nifConf == null) {
            if (!"".startsWith(searchString)) {
              continue;
            }
          }
          if (!nifConf.getName().startsWith(searchString)) {
            continue;
          }
          gridModel.add(
            new RealNetworkInterfaceWithRealNetworkInterfaceConfigurationDto(model, nifConf)
          );
        }
      }
      else if (searchOper.equals("ew")) {
        for (RealNetworkInterface model: realNetworkInterfaceDao.findByCriteria(criteria)) {
          RealNetworkInterfaceConfiguration nifConf
            = simConf.getRealNetworkInterfaceConfigurations().get(model);
          if (nifConf == null) {
            if (!"".endsWith(searchString)) {
              continue;
            }
          }
          if (!nifConf.getName().endsWith(searchString)) {
            continue;
          }
          gridModel.add(
            new RealNetworkInterfaceWithRealNetworkInterfaceConfigurationDto(model, nifConf)
          );
        }
      }
      else if (searchOper.equals("cn")) {
        for (RealNetworkInterface model: realNetworkInterfaceDao.findByCriteria(criteria)) {
          RealNetworkInterfaceConfiguration nifConf
            = simConf.getRealNetworkInterfaceConfigurations().get(model);
          if (nifConf == null) {
            if (!"".contains(searchString)) {
              continue;
            }
          }
          if (!nifConf.getName().contains(searchString)) {
            continue;
          }
          gridModel.add(
            new RealNetworkInterfaceWithRealNetworkInterfaceConfigurationDto(model, nifConf)
          );
        }
      }
    }
    else {
      for (RealNetworkInterface model: realNetworkInterfaceDao.findByCriteria(criteria)) {
        RealNetworkInterfaceConfiguration nifConf
          = simConf.getRealNetworkInterfaceConfigurations().get(model);
        gridModel.add(
          new RealNetworkInterfaceWithRealNetworkInterfaceConfigurationDto(model, nifConf)
        );
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

  public List<RealNetworkInterfaceWithRealNetworkInterfaceConfigurationDto> getGridModel() {
    return gridModel;
  }

  public void setGridModel(List<RealNetworkInterfaceWithRealNetworkInterfaceConfigurationDto> gridModel) {
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