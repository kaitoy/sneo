/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;

@Entity
@Table(name = "ADDITIONAL_IP_V4_ROUTE_GROUP")
public class AdditionalIpV4RouteGroup implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6638309871641624063L;
  
  private Integer id;
  private String name;
  private List<AdditionalIpV4Route> additionalIpV4Routes;

  @Id
  @GeneratedValue(generator = "SequenceStyleGenerator")
  @GenericGenerator(
    name = "SequenceStyleGenerator",
    strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters = {
      @Parameter(name = "sequence_name", value = "ADDITIONAL_IP_V4_ROUTE_GROUP_SEQUENCE"),
      @Parameter(name = "initial_value", value = "1"),
      @Parameter(name = "increment_size", value = "1")
    }
  )
  @Column(name = "ID")
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

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

  @ManyToMany(fetch = FetchType.LAZY)
  //@LazyCollection(LazyCollectionOption.TRUE)
  @JoinTable(
    name = "ADDITIONAL_IP_V4_ROUTE_GROUP__ADDITIONAL_IP_V4_ROUTE",
    joinColumns = @JoinColumn(name = "ADDITIONAL_IP_V4_ROUTE_GROUP_ID"),
    inverseJoinColumns = @JoinColumn(name = "ADDITIONAL_IP_V4_ROUTE_ID")
  )
  public List<AdditionalIpV4Route> getAdditionalIpV4Routes() {
    return additionalIpV4Routes;
  }

  public void setAdditionalIpV4Routes(List<AdditionalIpV4Route> additionalIpV4Routes) {
    this.additionalIpV4Routes = additionalIpV4Routes;
  }

}
