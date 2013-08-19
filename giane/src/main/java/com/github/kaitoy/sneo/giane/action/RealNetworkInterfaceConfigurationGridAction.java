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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.pcap4j.util.ByteArrays;
import com.github.kaitoy.sneo.giane.model.RealNetworkInterfaceConfiguration;
import com.github.kaitoy.sneo.giane.model.dao.RealNetworkInterfaceConfigurationDao;
import com.github.kaitoy.sneo.giane.model.dto.RealNetworkInterfaceConfigurationDto;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("giane-default")
@InterceptorRef("gianeDefaultStack")
public class RealNetworkInterfaceConfigurationGridAction extends ActionSupport {

  /**
   *
   */
  private static final long serialVersionUID = -1847058191047792144L;

  private RealNetworkInterfaceConfigurationDao realNetworkInterfaceConfigurationDao;

  private static final Comparator<RealNetworkInterfaceConfigurationDto> idComparator
    = new Comparator<RealNetworkInterfaceConfigurationDto>() {
        public int compare(RealNetworkInterfaceConfigurationDto o1, RealNetworkInterfaceConfigurationDto o2) {
          return o2.getId().compareTo(o1.getId());
        }
      };
  private static final Comparator<RealNetworkInterfaceConfigurationDto> nameComparator
    = new Comparator<RealNetworkInterfaceConfigurationDto>() {
        public int compare(RealNetworkInterfaceConfigurationDto o1, RealNetworkInterfaceConfigurationDto o2) {
          return o2.getName().compareTo(o1.getName());
        }
      };
  private static final Comparator<RealNetworkInterfaceConfigurationDto> macAddressComparator
    = new Comparator<RealNetworkInterfaceConfigurationDto>() {
        public int compare(RealNetworkInterfaceConfigurationDto o1, RealNetworkInterfaceConfigurationDto o2) {
          byte[] o1Addr = ByteArrays.parseByteArray(o1.getMacAddress(), ":");
          byte[] o2Addr = ByteArrays.parseByteArray(o2.getMacAddress(), ":");

          for (int i = 0; i < o1Addr.length; i++) {
            if (o1Addr[i] != o2Addr[i]) {
              return (0xFF & o2Addr[i]) - (0xFF & o1Addr[i]);
            }
          }

          return 0;
        }
      };
  private static final Comparator<RealNetworkInterfaceConfigurationDto> deviceNameComparator
    = new Comparator<RealNetworkInterfaceConfigurationDto>() {
        public int compare(RealNetworkInterfaceConfigurationDto o1, RealNetworkInterfaceConfigurationDto o2) {
          return o2.getDeviceName().compareTo(o1.getDeviceName());
        }
      };

  // result List
  private List<RealNetworkInterfaceConfigurationDto> gridModel;

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
  public void setRealNetworkInterfaceConfigurationDao(
    RealNetworkInterfaceConfigurationDao realNetworkInterfaceConfigurationDao
  ) {
    this.realNetworkInterfaceConfigurationDao
      = realNetworkInterfaceConfigurationDao;
  }

  @Override
  @Action(
    results = {
      @Result(name = "success", type = "json")
    }
  )
  public String execute() {
    CriteriaBuilder cb = realNetworkInterfaceConfigurationDao.getCriteriaBuilder();
    CriteriaQuery<RealNetworkInterfaceConfiguration> cq
      = cb.createQuery(RealNetworkInterfaceConfiguration.class);
    Root<RealNetworkInterfaceConfiguration> r
      = cq.from(RealNetworkInterfaceConfiguration.class);
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
      else if (
           searchField.equals("name")
        || searchField.equals("macAddress")
        || searchField.equals("deviceName")
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

    List<RealNetworkInterfaceConfiguration> confs
      = realNetworkInterfaceConfigurationDao.findByCriteria(cq);
    gridModel = new ArrayList<RealNetworkInterfaceConfigurationDto>();
    for (RealNetworkInterfaceConfiguration conf: confs) {
      gridModel.add(new RealNetworkInterfaceConfigurationDto(conf));
    }

    records = gridModel.size();

    if (sord != null && sord.length() != 0 && sidx != null && sidx.length() != 0) {
      if (sidx.equalsIgnoreCase("id")) {
        Collections.sort(gridModel, idComparator);
      }
      else if (sidx.equalsIgnoreCase("name")) {
        Collections.sort(gridModel, nameComparator);
      }
      else if (sidx.equalsIgnoreCase("macAddress")) {
        Collections.sort(gridModel, macAddressComparator);
      }
      else if (sidx.equalsIgnoreCase("deviceName")) {
        Collections.sort(gridModel, deviceNameComparator);
      }

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

    return SUCCESS;
  }

  public String getJSON() { return execute(); }

  public List<RealNetworkInterfaceConfigurationDto> getGridModel() {
    return gridModel;
  }

  public void setGridModel(List<RealNetworkInterfaceConfigurationDto> gridModel) {
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