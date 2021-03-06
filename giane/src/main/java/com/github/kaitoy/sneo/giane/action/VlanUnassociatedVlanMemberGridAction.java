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
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import com.github.kaitoy.sneo.giane.model.VlanMember;
import com.github.kaitoy.sneo.giane.model.dao.VlanMemberDao;
import com.github.kaitoy.sneo.giane.model.dto.VlanMemberDto;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class VlanUnassociatedVlanMemberGridAction extends ActionSupport {

  /**
   *
   */
  private static final long serialVersionUID = 5352764975558711436L;

  private VlanMemberDao vlanMemberDao;

  // The following comparators are not used. A sort is done at client side.
  private static final Comparator<VlanMemberDto> idComparator
    = new Comparator<VlanMemberDto>() {
        public int compare(VlanMemberDto o1, VlanMemberDto o2) {
          return o2.getId().compareTo(o1.getId());
        }
      };
  private static final Comparator<VlanMemberDto> nameComparator
    = new Comparator<VlanMemberDto>() {
        public int compare(VlanMemberDto o1, VlanMemberDto o2) {
          return o2.getName().compareTo(o1.getName());
        }
      };

  // result List
  private List<VlanMemberDto> gridModel;

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
  public void setVlanMemberDao(VlanMemberDao vlanMemberDao) {
    this.vlanMemberDao = vlanMemberDao;
  }

  @Override
  @Action(
    results = {
      @Result(name = "success", type = "json")
    }
  )
  public String execute() {
    CriteriaBuilder cb = vlanMemberDao.getCriteriaBuilder();
    CriteriaQuery<VlanMember> cq = cb.createQuery(VlanMember.class);
    Root<VlanMember> r = cq.from(VlanMember.class);
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
    Integer node_id = Integer.valueOf(((String[])params.get("node_id"))[0]);
    Integer vlan_id = Integer.valueOf(((String[])params.get("vlan_id"))[0]);
    List<VlanMember> models
      = vlanMemberDao
          .findByCriteriaAndNodeIdAndVlanId(cq, node_id, vlan_id, false);

    gridModel = new ArrayList<VlanMemberDto>();
    for (VlanMember nif: models) {
      gridModel.add(new VlanMemberDto(nif));
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

  public List<VlanMemberDto> getGridModel() {
    return gridModel;
  }

  public void setGridModel(List<VlanMemberDto> gridModel) {
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