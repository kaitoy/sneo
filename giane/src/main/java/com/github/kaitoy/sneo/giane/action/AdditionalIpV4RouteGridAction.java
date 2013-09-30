/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.action;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import com.github.kaitoy.sneo.giane.model.AdditionalIpV4Route;
import com.github.kaitoy.sneo.giane.model.dao.AdditionalIpV4RouteDao;
import com.github.kaitoy.sneo.giane.model.dto.AdditionalIpV4RouteDto;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class AdditionalIpV4RouteGridAction extends ActionSupport {

  /**
   *
   */
  private static final long serialVersionUID = 4627409854020385702L;

  private static final Map<String, Comparator<AdditionalIpV4RouteDto>> comparators
    = new HashMap<String, Comparator<AdditionalIpV4RouteDto>>();

  static  {
    comparators.put(
      "name",
      new Comparator<AdditionalIpV4RouteDto>() {
        public int compare(AdditionalIpV4RouteDto o1, AdditionalIpV4RouteDto o2) {
          return o2.getName().compareTo(o1.getName());
        }
      }
    );
    comparators.put(
      "id",
      new Comparator<AdditionalIpV4RouteDto>() {
        public int compare(AdditionalIpV4RouteDto o1, AdditionalIpV4RouteDto o2) {
          return o2.getId().compareTo(o1.getId());
        }
      }
    );
    comparators.put(
      "networkDestination",
      new Comparator<AdditionalIpV4RouteDto>() {
        public int compare(AdditionalIpV4RouteDto o1, AdditionalIpV4RouteDto o2) {
          byte[] o1Addr;
          byte[] o2Addr;
          try {
            o1Addr = InetAddress
                       .getByName(o1.getNetworkDestination()).getAddress();
            o2Addr = InetAddress
                       .getByName(o2.getNetworkDestination()).getAddress();
          } catch (UnknownHostException e) {
            throw new AssertionError("Never get here");
          }

          if (o1Addr.length != o2Addr.length) {
            return o2Addr.length - o1Addr.length;
          }

          for (int i = 0; i < o1Addr.length; i++) {
            if (o1Addr[i] != o2Addr[i]) {
              return (0xFF & o2Addr[i]) - (0xFF & o1Addr[i]);
            }
          }

          return 0;
        }
      }
    );
    comparators.put(
      "netmask",
      new Comparator<AdditionalIpV4RouteDto>() {
        public int compare(AdditionalIpV4RouteDto o1, AdditionalIpV4RouteDto o2) {
          byte[] o1Addr;
          byte[] o2Addr;
          try {
            o1Addr = InetAddress.getByName(o1.getNetmask()).getAddress();
            o2Addr = InetAddress.getByName(o2.getNetmask()).getAddress();
          } catch (UnknownHostException e) {
            throw new AssertionError("Never get here");
          }

          if (o1Addr.length != o2Addr.length) {
            return o2Addr.length - o1Addr.length;
          }

          for (int i = 0; i < o1Addr.length; i++) {
            if (o1Addr[i] != o2Addr[i]) {
              return (0xFF & o2Addr[i]) - (0xFF & o1Addr[i]);
            }
          }

          return 0;
        }
      }
    );
    comparators.put(
      "gateway",
      new Comparator<AdditionalIpV4RouteDto>() {
        public int compare(AdditionalIpV4RouteDto o1, AdditionalIpV4RouteDto o2) {
          byte[] o1Addr;
          byte[] o2Addr;
          try {
            o1Addr = InetAddress.getByName(o1.getGateway()).getAddress();
            o2Addr = InetAddress.getByName(o2.getGateway()).getAddress();
          } catch (UnknownHostException e) {
            throw new AssertionError("Never get here");
          }

          if (o1Addr.length != o2Addr.length) {
            return o2Addr.length - o1Addr.length;
          }

          for (int i = 0; i < o1Addr.length; i++) {
            if (o1Addr[i] != o2Addr[i]) {
              return (0xFF & o2Addr[i]) - (0xFF & o1Addr[i]);
            }
          }

          return 0;
        }
      }
    );
    comparators.put(
      "metric",
      new Comparator<AdditionalIpV4RouteDto>() {
        public int compare(AdditionalIpV4RouteDto o1, AdditionalIpV4RouteDto o2) {
          return o2.getMetric().compareTo(o1.getMetric());
        }
      }
    );
    comparators.put(
      "descr",
      new Comparator<AdditionalIpV4RouteDto>() {
        public int compare(AdditionalIpV4RouteDto o1, AdditionalIpV4RouteDto o2) {
          return o2.getDescr().compareTo(o1.getDescr());
        }
      }
    );
  }

  private AdditionalIpV4RouteDao additionalIpV4RouteDao;

  // result List
  private List<AdditionalIpV4RouteDto> gridModel;

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
  public void setAdditionalIpV4RouteDao(
    AdditionalIpV4RouteDao additionalIpV4RouteDao
  ) {
    this.additionalIpV4RouteDao = additionalIpV4RouteDao;
  }

  @Override
  @Action(
    results = {
      @Result(name = "success", type = "json")
    }
  )
  public String execute() {
    CriteriaBuilder cb = additionalIpV4RouteDao.getCriteriaBuilder();
    CriteriaQuery<AdditionalIpV4Route> cq = cb.createQuery(AdditionalIpV4Route.class);
    Root<AdditionalIpV4Route> r = cq.from(AdditionalIpV4Route.class);
    cq.select(r);

    if (searchField != null) {
      if (
           searchField.equals("id")
        || searchField.equals("metric")
      ) {
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
      else if (
           searchField.equals("name")
        || searchField.equals("networkDestination")
        || searchField.equals("netmask")
        || searchField.equals("gateway")
        || searchField.equals("descr")
      ) {
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

    List<AdditionalIpV4Route> models = additionalIpV4RouteDao.findByCriteria(cq);
    gridModel = new ArrayList<AdditionalIpV4RouteDto>();
    for (AdditionalIpV4Route entry: models) {
      gridModel.add(new AdditionalIpV4RouteDto(entry));
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

  public List<AdditionalIpV4RouteDto> getGridModel() {
    return gridModel;
  }

  public void setGridModel(List<AdditionalIpV4RouteDto> gridModel) {
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