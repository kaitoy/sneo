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
@PrimaryKeyJoinColumn(name = "IP_V6_ROUTE_ID")
@Table(name = "ADDITIONAL_IP_V6_ROUTE")
public class AdditionalIpV6Route extends IpV6Route {

  /**
   *
   */
  private static final long serialVersionUID = 1409726267372893718L;

  private String name;
  private String descr;
  private List<AdditionalIpV6RouteGroup> additionalIpV6RouteGroups;

  @Column(name = "NAME", nullable = false, length = 200, unique = true)
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
    maxLength = "200",
    shortCircuit = true
  )
  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "DESCR", nullable = true, length = 5000, unique = false)
  public String getDescr() {
    return descr;
  }

  @StringLengthFieldValidator(
    key = "StringLengthFieldValidator.error.max",
    trim = true,
    maxLength = "5000",
    shortCircuit = true // Stops checking if detects error
  )
  public void setDescr(String descr) {
    this.descr = descr;
  }

  @ManyToMany(mappedBy = "additionalIpV6Routes", fetch = FetchType.LAZY)
  //@LazyCollection(LazyCollectionOption.TRUE)
  public List<AdditionalIpV6RouteGroup> getAdditionalIpV6RouteGroups() {
    return additionalIpV6RouteGroups;
  }

  public void setAdditionalIpV6RouteGroups(List<AdditionalIpV6RouteGroup> additionalIpV6RouteGroups) {
    this.additionalIpV6RouteGroups = additionalIpV6RouteGroups;
  }

}
