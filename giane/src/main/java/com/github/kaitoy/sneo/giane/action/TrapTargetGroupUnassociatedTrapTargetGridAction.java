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

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.github.kaitoy.sneo.giane.model.TrapTarget;
import com.github.kaitoy.sneo.giane.model.dao.TrapTargetDao;
import com.github.kaitoy.sneo.giane.model.dto.TrapTargetDto;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class TrapTargetGroupUnassociatedTrapTargetGridAction extends ActionSupport {

  /**
   *
   */
  private static final long serialVersionUID = 5262631166036515125L;

  private TrapTargetDao trapTargetDao;

  // The following comparators are not used. A sort is done at client side.
  private static final Comparator<TrapTargetDto> idComparator
    = new Comparator<TrapTargetDto>() {
        public int compare(TrapTargetDto o1, TrapTargetDto o2) {
          return o2.getId().compareTo(o1.getId());
        }
      };
  private static final Comparator<TrapTargetDto> nameComparator
    = new Comparator<TrapTargetDto>() {
        public int compare(TrapTargetDto o1, TrapTargetDto o2) {
          return o2.getName().compareTo(o1.getName());
        }
      };

  // result List
  private List<TrapTargetDto> gridModel;

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
  public void setTrapTargetDao(TrapTargetDao trapTargetDao) {
    this.trapTargetDao = trapTargetDao;
  }

  @Action(
    results = {
      @Result(name = "success", type = "json")
    }
  )
  public String execute() {
    DetachedCriteria criteria = DetachedCriteria.forClass(TrapTarget.class);

    if (searchField != null) {
      if (searchField.equals("id")) {
        Integer searchValue = Integer.valueOf(searchString);
        if (searchOper.equals("eq")) criteria.add(Restrictions.eq(searchField, searchValue));
        else if (searchOper.equals("ne")) criteria.add(Restrictions.ne(searchField, searchValue));
        else if (searchOper.equals("lt")) criteria.add(Restrictions.lt(searchField, searchValue));
        else if (searchOper.equals("gt")) criteria.add(Restrictions.gt(searchField, searchValue));
      }
      else if (searchField.equals("name")) {
        if (searchOper.equals("eq")) criteria.add(Restrictions.eq(searchField, searchString));
        else if (searchOper.equals("ne")) criteria.add(Restrictions.ne(searchField, searchString));
        else if (searchOper.equals("bw")) criteria.add(Restrictions.like(searchField, searchString + "%"));
        else if (searchOper.equals("ew")) criteria.add(Restrictions.like(searchField, "%" + searchString));
        else if (searchOper.equals("cn")) criteria.add(Restrictions.like(searchField, "%" + searchString + "%"));
      }
    }

    Map<String, Object> params = ActionContext.getContext().getParameters();
    Integer trapTargetGroup_id = Integer.valueOf(((String[])params.get("trapTargetGroup_id"))[0]);
    List<TrapTarget> models
      = trapTargetDao
          .findByCriteriaAndTrapTargetGroupId(criteria, trapTargetGroup_id, false);

    gridModel = new ArrayList<TrapTargetDto>();
    for (TrapTarget trapTarget: models) {
      gridModel.add(new TrapTargetDto(trapTarget));
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

  public List<TrapTargetDto> getGridModel() {
    return gridModel;
  }

  public void setGridModel(List<TrapTargetDto> gridModel) {
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