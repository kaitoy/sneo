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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import com.github.kaitoy.sneo.giane.model.FixedIpV4Route;
import com.github.kaitoy.sneo.giane.model.dao.FixedIpV4RouteDao;
import com.github.kaitoy.sneo.giane.model.dto.IpV4RouteDto;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class FixedIpV4RouteGridAction extends ActionSupport {

  /**
   *
   */
  private static final long serialVersionUID = 4627409854020385702L;

  private static final Map<String, Comparator<IpV4RouteDto>> comparators
    = new HashMap<String, Comparator<IpV4RouteDto>>();

  static  {
    comparators.put(
      "id",
      new Comparator<IpV4RouteDto>() {
        public int compare(IpV4RouteDto o1, IpV4RouteDto o2) {
          return o2.getId().compareTo(o1.getId());
        }
      }
    );
    comparators.put(
      "networkDestination",
      new Comparator<IpV4RouteDto>() {
        public int compare(IpV4RouteDto o1, IpV4RouteDto o2) {
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
      new Comparator<IpV4RouteDto>() {
        public int compare(IpV4RouteDto o1, IpV4RouteDto o2) {
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
      new Comparator<IpV4RouteDto>() {
        public int compare(IpV4RouteDto o1, IpV4RouteDto o2) {
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
      new Comparator<IpV4RouteDto>() {
        public int compare(IpV4RouteDto o1, IpV4RouteDto o2) {
          return o2.getMetric().compareTo(o1.getMetric());
        }
      }
    );
  }

  private FixedIpV4RouteDao fixedIpV4RouteDao;

  // result List
  private List<IpV4RouteDto> gridModel;

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
  public void setFixedIpV4RouteDao(FixedIpV4RouteDao fixedIpV4RouteDao) {
    this.fixedIpV4RouteDao = fixedIpV4RouteDao;
  }

  @Override
  @Action(
    results = {
      @Result(name = "success", type = "json")
    }
  )
  public String execute() {
    CriteriaBuilder cb = fixedIpV4RouteDao.getCriteriaBuilder();
    CriteriaQuery<FixedIpV4Route> cq = cb.createQuery(FixedIpV4Route.class);
    Root<FixedIpV4Route> r = cq.from(FixedIpV4Route.class);
    cq.select(r);

    Map<String, Object> params = ActionContext.getContext().getParameters();
    Integer node_id
      = Integer.valueOf(((String[])params.get("node_id"))[0]);
    List<Predicate> predicates = new ArrayList<Predicate>();
    predicates.add(cb.equal(r.get("node"), node_id));

    if (searchField != null) {
      if (
           searchField.equals("id")
        || searchField.equals("metric")
      ) {
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
      else if (
           searchField.equals("networkDestination")
        || searchField.equals("netmask")
        || searchField.equals("gateway")
      ) {
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

    List<FixedIpV4Route> models = fixedIpV4RouteDao.findByCriteria(cq);
    gridModel = new ArrayList<IpV4RouteDto>();
    for (FixedIpV4Route entry: models) {
      gridModel.add(new IpV4RouteDto(entry));
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

  public List<IpV4RouteDto> getGridModel() {
    return gridModel;
  }

  public void setGridModel(List<IpV4RouteDto> gridModel) {
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