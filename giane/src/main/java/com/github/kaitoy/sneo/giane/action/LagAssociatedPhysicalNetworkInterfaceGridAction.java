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
import com.github.kaitoy.sneo.giane.model.PhysicalNetworkInterface;
import com.github.kaitoy.sneo.giane.model.dao.PhysicalNetworkInterfaceDao;
import com.github.kaitoy.sneo.giane.model.dto.PhysicalNetworkInterfaceDto;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class LagAssociatedPhysicalNetworkInterfaceGridAction extends ActionSupport {

  /**
   *
   */
  private static final long serialVersionUID = -4713498508317833387L;

  private PhysicalNetworkInterfaceDao physicalNetworkInterfaceDao;

  // The following comparators are not used. A sort is done at client side.
  private static final Comparator<PhysicalNetworkInterfaceDto> idComparator
    = new Comparator<PhysicalNetworkInterfaceDto>() {
        public int compare(PhysicalNetworkInterfaceDto o1, PhysicalNetworkInterfaceDto o2) {
          return o2.getId().compareTo(o1.getId());
        }
      };
  private static final Comparator<PhysicalNetworkInterfaceDto> nameComparator
    = new Comparator<PhysicalNetworkInterfaceDto>() {
        public int compare(PhysicalNetworkInterfaceDto o1, PhysicalNetworkInterfaceDto o2) {
          return o2.getName().compareTo(o1.getName());
        }
      };

  // result List
  private List<PhysicalNetworkInterfaceDto> gridModel;

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

  // Total Pages
  private Integer total = 0;

  // All Record
  private Integer records = 0;

  // for DI
  public void setPhysicalNetworkInterfaceDao(
    PhysicalNetworkInterfaceDao physicalNetworkInterfaceDao
  ) {
    this.physicalNetworkInterfaceDao = physicalNetworkInterfaceDao;
  }

  @Override
  @Action(
    results = {
      @Result(name = "success", type = "json")
    }
  )
  public String execute() {
    CriteriaBuilder cb = physicalNetworkInterfaceDao.getCriteriaBuilder();
    CriteriaQuery<PhysicalNetworkInterface> cq = cb.createQuery(PhysicalNetworkInterface.class);
    Root<PhysicalNetworkInterface> r = cq.from(PhysicalNetworkInterface.class);
    cq.select(r);

    Map<String, Object> params = ActionContext.getContext().getParameters();
    Integer node_id = Integer.valueOf(((String[])params.get("node_id"))[0]);
    List<Predicate> predicates = new ArrayList<Predicate>();
    predicates.add(cb.equal(r.get("node"), node_id));

    Integer lag_id = Integer.valueOf(((String[])params.get("lag_id"))[0]);
    predicates.add(cb.equal(r.get("lag"), lag_id));

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

    List<PhysicalNetworkInterface> models
      = physicalNetworkInterfaceDao.findByCriteria(cq);

    gridModel = new ArrayList<PhysicalNetworkInterfaceDto>();
    for (PhysicalNetworkInterface nif: models) {
      gridModel.add(new PhysicalNetworkInterfaceDto(nif));
    }

    records = gridModel.size();

    if (sord != null) {
      if (sidx.equalsIgnoreCase("id")) {
        Collections.sort(gridModel, idComparator);
      }
      else if (sidx.equalsIgnoreCase("name")) {
        Collections.sort(gridModel, nameComparator);
      }
    }
    if (sord != null && sord.equalsIgnoreCase("desc")) {
      Collections.reverse(gridModel);
    }
    //else {} // asc

    return "success";
  }

  public String getJSON() { return execute(); }

  public List<PhysicalNetworkInterfaceDto> getGridModel() {
    return gridModel;
  }

  public void setGridModel(List<PhysicalNetworkInterfaceDto> gridModel) {
    this.gridModel = gridModel;
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