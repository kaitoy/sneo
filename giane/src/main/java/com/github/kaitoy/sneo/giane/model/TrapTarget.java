/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2015 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.github.kaitoy.sneo.network.dto.TrapTargetDto;
import com.opensymphony.xwork2.validator.annotations.ConversionErrorFieldValidator;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;

@Entity
@Table(name = "TRAP_TARGET")
public class TrapTarget extends AbstractModel implements FormMessage {

  /**
   *
   */
  private static final long serialVersionUID = 1742187203844448320L;

  private String name;
  private String address;
  private Integer port;
  private String descr;
  private List<TrapTargetGroup> trapTargetGroups;

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

  @Column(name = "ADDRESS", nullable = false)
  public String getAddress() {
    return address;
  }

  @RequiredStringValidator(
    key = "RequiredStringValidator.error",
    trim = true,
    shortCircuit = true // Stops checking if detects error
  )
  @CustomValidator(
    key = "InetAddressStringValidator.error",
    type = "inetaddressstring"
  )
  public void setAddress(String address) {
    this.address = address;
  }

  @Column(name = "PORT", nullable = false)
  public Integer getPort() {
    return port;
  }

  @ConversionErrorFieldValidator(
    key = "ConversionErrorFieldValidator.error",
    shortCircuit = true
  )
  @RequiredFieldValidator(
    key = "RequiredFieldValidator.error",
    shortCircuit = true
  )
  @IntRangeFieldValidator(
    key = "IntRangeFieldValidator.error.min.max",
    min = "1",
    max = "65535",
    shortCircuit = true
  )
  public void setPort(Integer port) {
    this.port = port;
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

  @ManyToMany(mappedBy = "trapTargets", fetch = FetchType.LAZY)
  //@LazyCollection(LazyCollectionOption.TRUE)
  public List<TrapTargetGroup> getTrapTargetGroups() {
    return trapTargetGroups;
  }

  public void setTrapTargetGroups(List<TrapTargetGroup> trapTargetGroups) {
    this.trapTargetGroups = trapTargetGroups;
  }

  public TrapTargetDto toDto() {
    TrapTargetDto dto = new TrapTargetDto();
    dto.setId(getId());
    dto.setName(name);
    dto.setAddress(address);
    dto.setPort(port);
    return dto;
  }

}
