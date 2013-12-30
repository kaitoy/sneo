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
import javax.persistence.criteria.Root;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import com.github.kaitoy.sneo.giane.model.AdditionalIpV6Route;
import com.github.kaitoy.sneo.giane.model.dao.AdditionalIpV6RouteDao;
import com.github.kaitoy.sneo.giane.model.dto.AdditionalIpV6RouteDto;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class AdditionalIpV6RouteGroupUnassociatedAdditionalIpV6RouteGridAction extends ActionSupport {

  /**
   *
   */
  private static final long serialVersionUID = 123445401212097817L;

  private AdditionalIpV6RouteDao additionalIpV6RouteDao;

  // The following comparators are not used. A sort is done at client side.
  private static final Comparator<AdditionalIpV6RouteDto> idComparator
    = new Comparator<AdditionalIpV6RouteDto>() {
        public int compare(AdditionalIpV6RouteDto o1, AdditionalIpV6RouteDto o2) {
          return o2.getId().compareTo(o1.getId());
        }
      };
  private static final Comparator<AdditionalIpV6RouteDto> nameComparator
    = new Comparator<AdditionalIpV6RouteDto>() {
        public int compare(AdditionalIpV6RouteDto o1, AdditionalIpV6RouteDto o2) {
          return o2.getName().compareTo(o1.getName());
        }
      };

  // result List
  private List<AdditionalIpV6RouteDto> gridModel;

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
  public void setAdditionalIpV6RouteDao(
    AdditionalIpV6RouteDao additionalIpV6RouteDao
  ) {
    this.additionalIpV6RouteDao = additionalIpV6RouteDao;
  }

  @Override
  @Action(
    results = {
      @Result(name = "success", type = "json")
    }
  )
  public String execute() {
    CriteriaBuilder cb = additionalIpV6RouteDao.getCriteriaBuilder();
    CriteriaQuery<AdditionalIpV6Route> cq = cb.createQuery(AdditionalIpV6Route.class);
    Root<AdditionalIpV6Route> r = cq.from(AdditionalIpV6Route.class);
    cq.select(r);

    if (searchField != null) {
      if (searchField.equals("id")) {
        Integer searchValue = Integer.valueOf(searchString);
        if (searchOper.equals("eq")) {
          cq.where(cb.equal(r.get(searchField), searchValue));
        }
        else if (searchOper.equals("ne")) {
          cq.where(cb.notEqual(r.get(searchField), searchValue));
        }
        else if (searchOper.equals("lt")) {
          cq.where(cb.lt(r.get(searchField).as(Integer.class), searchValue));
        }
        else if (searchOper.equals("gt")) {
          cq.where(cb.gt(r.get(searchField).as(Integer.class), searchValue));
        }
      }
      else if (searchField.equals("name")) {
        if (searchOper.equals("eq")) {
          cq.where(cb.equal(r.get(searchField), searchString));
        }
        else if (searchOper.equals("ne")) {
          cq.where(cb.notEqual(r.get(searchField), searchString));
        }
        else if (searchOper.equals("bw")) {
          cq.where(cb.like(r.get(searchField).as(String.class), searchString + "%"));
        }
        else if (searchOper.equals("ew")) {
          cq.where(cb.like(r.get(searchField).as(String.class), "%" + searchString));
        }
        else if (searchOper.equals("cn")) {
          cq.where(cb.like(r.get(searchField).as(String.class), "%" + searchString + "%"));
        }
      }
    }

    Map<String, Object> params = ActionContext.getContext().getParameters();
    Integer additionalIpV6RouteGroup_id
      = Integer.valueOf(((String[])params.get("additionalIpV6RouteGroup_id"))[0]);
    List<AdditionalIpV6Route> models
      = additionalIpV6RouteDao
          .findByCriteriaAndAdditionalIpV6RouteGroupId(cq, additionalIpV6RouteGroup_id, false);

    gridModel = new ArrayList<AdditionalIpV6RouteDto>();
    for (AdditionalIpV6Route additionalIpV6Route: models) {
      gridModel.add(new AdditionalIpV6RouteDto(additionalIpV6Route));
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

  public List<AdditionalIpV6RouteDto> getGridModel() {
    return gridModel;
  }

  public void setGridModel(List<AdditionalIpV6RouteDto> gridModel) {
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