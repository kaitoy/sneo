/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
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

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.github.kaitoy.sneo.giane.model.FileMibFormat;
import com.github.kaitoy.sneo.giane.model.SnmpAgent;
import com.github.kaitoy.sneo.giane.model.dao.SnmpAgentDao;
import com.github.kaitoy.sneo.giane.model.dto.SnmpAgentDto;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class SnmpAgentGridAction extends ActionSupport {

  /**
   *
   */
  private static final long serialVersionUID = 4627409854020385702L;

  private static final Map<String, Comparator<SnmpAgentDto>> comparators
    = new HashMap<String, Comparator<SnmpAgentDto>>();

  static  {
    comparators.put(
      "id",
      new Comparator<SnmpAgentDto>() {
        public int compare(SnmpAgentDto o1, SnmpAgentDto o2) {
          return o2.getId().compareTo(o1.getId());
        }
      }
    );
    comparators.put(
      "address",
      new Comparator<SnmpAgentDto>() {
        public int compare(SnmpAgentDto o1, SnmpAgentDto o2) {
          byte[] o1Addr;
          byte[] o2Addr;
          try {
            o1Addr = InetAddress.getByName(o1.getAddress()).getAddress();
            o2Addr = InetAddress.getByName(o2.getAddress()).getAddress();
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
      "port",
      new Comparator<SnmpAgentDto>() {
        public int compare(SnmpAgentDto o1, SnmpAgentDto o2) {
          return o2.getPort().compareTo(o1.getPort());
        }
      }
    );
    comparators.put(
      "communityName",
      new Comparator<SnmpAgentDto>() {
        public int compare(SnmpAgentDto o1, SnmpAgentDto o2) {
          return o2.getCommunityName().compareTo(o1.getCommunityName());
        }
      }
    );
    comparators.put(
      "securityName",
      new Comparator<SnmpAgentDto>() {
        public int compare(SnmpAgentDto o1, SnmpAgentDto o2) {
          return o2.getSecurityName().compareTo(o1.getSecurityName());
        }
      }
    );
    comparators.put(
      "fileMibPath",
      new Comparator<SnmpAgentDto>() {
        public int compare(SnmpAgentDto o1, SnmpAgentDto o2) {
          return o2.getFileMibPath().compareTo(o1.getFileMibPath());
        }
      }
    );
    comparators.put(
      "fileMibFormat",
      new Comparator<SnmpAgentDto>() {
        public int compare(SnmpAgentDto o1, SnmpAgentDto o2) {
          return o2.getFileMibFormat().compareTo(o1.getFileMibFormat());
        }
      }
    );
    comparators.put(
      "communityStringIndexList",
      new Comparator<SnmpAgentDto>() {
        public int compare(SnmpAgentDto o1, SnmpAgentDto o2) {
          return o2.getCommunityStringIndexList()
                   .compareTo(o1.getCommunityStringIndexList());
        }
      }
    );
  }

  private SnmpAgentDao snmpAgentDao;

  // result List
  private List<SnmpAgentDto> gridModel;

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
  public void setSnmpAgentDao(SnmpAgentDao snmpAgentDao) {
    this.snmpAgentDao = snmpAgentDao;
  }

  @Action(
    results = {
      @Result(name = "success", type = "json")
    }
  )
  public String execute() {
    DetachedCriteria criteria = DetachedCriteria.forClass(SnmpAgent.class);

    Map<String, Object> params = ActionContext.getContext().getParameters();
    Integer node_id
      = Integer.valueOf(((String[])params.get("node_id"))[0]);
    criteria.add(Restrictions.eq("node.id", node_id));

    if (searchField != null) {
      if (searchField.equals("id") || searchField.equals("port")) {
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
      else if (
           searchField.equals("address")
        || searchField.equals("communityName")
        || searchField.equals("securityName")
        || searchField.equals("fileMibPath")
        || searchField.equals("communityStringIndexList")
      ) {
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
      else if (searchField.equals("fileMibFormat")) {
        if (searchOper.equals("eq")) {
          criteria.add(Restrictions.eq(searchField, FileMibFormat.valueOf(searchString)));
        }
        else if (searchOper.equals("ne")) {
          criteria.add(Restrictions.ne(searchField, FileMibFormat.valueOf(searchString)));
        }
      }
    }

    gridModel = new ArrayList<SnmpAgentDto>();
    for (SnmpAgent model: snmpAgentDao.findByCriteria(criteria)) {
      gridModel.add(new SnmpAgentDto(model));
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

  public List<SnmpAgentDto> getGridModel() {
    return gridModel;
  }

  public void setGridModel(List<SnmpAgentDto> gridModel) {
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