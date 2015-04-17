/*_##########################################################################
  _##
  _##  Copyright (C) 2013-2015 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;

@Entity
@Table(name = "ADDITIONAL_IP_V6_ROUTE_GROUP")
public class AdditionalIpV6RouteGroup extends AbstractModel implements FormMessage {

  /**
   *
   */
  private static final long serialVersionUID = 3245207445792581774L;

  private String name;
  private String descr;
  private List<AdditionalIpV6Route> additionalIpV6Routes;

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

  @ManyToMany(fetch = FetchType.LAZY)
  //@LazyCollection(LazyCollectionOption.TRUE)
  @JoinTable(
    name = "ADDITIONAL_IP_V6_ROUTE_GROUP__ADDITIONAL_IP_V6_ROUTE",
    joinColumns = @JoinColumn(name = "ADDITIONAL_IP_V6_ROUTE_GROUP_ID"),
    inverseJoinColumns = @JoinColumn(name = "ADDITIONAL_IP_V6_ROUTE_ID")
  )
  public List<AdditionalIpV6Route> getAdditionalIpV6Routes() {
    return additionalIpV6Routes;
  }

  public void setAdditionalIpV6Routes(List<AdditionalIpV6Route> additionalIpV6Routes) {
    this.additionalIpV6Routes = additionalIpV6Routes;
  }

}
