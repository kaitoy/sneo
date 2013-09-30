/*_##########################################################################
  _##
  _##  Copyright (C) 2013  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;

@Entity
@PrimaryKeyJoinColumn(name = "IP_V4_ROUTE_ID")
@Table(name = "ADDITIONAL_IP_V4_ROUTE")
public class AdditionalIpV4Route extends IpV4Route {

  /**
   *
   */
  private static final long serialVersionUID = -7596585819992941362L;

  private String name;
  private String descr;
  private List<AdditionalIpV4RouteGroup> additionalIpV4RouteGroups;

  @Column(name = "NAME", nullable = false, length = 50, unique = true)
  public String getName() {
    return name;
  }

  @RequiredStringValidator(
    key = "RequiredStringValidator.error",
    trim = true,
    shortCircuit = true // Stops checking if detects error
  )
  @StringLengthFieldValidator(
    key = "StringLengthFieldValidator.error.max",
    trim = true,
    maxLength = "50",
    shortCircuit = true
  )
  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "DESCR", nullable = true, length = 2000, unique = false)
  public String getDescr() {
    return descr;
  }

  @StringLengthFieldValidator(
    key = "StringLengthFieldValidator.error.max",
    trim = true,
    maxLength = "2000",
    shortCircuit = true // Stops checking if detects error
  )
  public void setDescr(String descr) {
    this.descr = descr;
  }

  @ManyToMany(mappedBy = "additionalIpV4Routes", fetch = FetchType.LAZY)
  //@LazyCollection(LazyCollectionOption.TRUE)
  public List<AdditionalIpV4RouteGroup> getAdditionalIpV4RouteGroups() {
    return additionalIpV4RouteGroups;
  }

  public void setAdditionalIpV4RouteGroups(List<AdditionalIpV4RouteGroup> additionalIpV4RouteGroups) {
    this.additionalIpV4RouteGroups = additionalIpV4RouteGroups;
  }

}
