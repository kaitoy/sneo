/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2015 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.github.kaitoy.sneo.network.dto.TrapTargetDto;
import com.github.kaitoy.sneo.network.dto.TrapTargetGroupDto;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;

@Entity
@Table(name = "TRAP_TARGET_GROUP")
public class TrapTargetGroup extends AbstractModel implements FormMessage {

  /**
   *
   */
  private static final long serialVersionUID = 6246027465167386978L;

  private String name;
  private String descr;
  private List<TrapTarget> trapTargets;

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
    name = "TRAP_TARGET_GROUP__TRAP_TARGET",
    joinColumns = @JoinColumn(name = "TRAP_TARGET_GROUP_ID"),
    inverseJoinColumns = @JoinColumn(name = "TRAP_TARGET_ID")
  )
  public List<TrapTarget> getTrapTargets() {
    return trapTargets;
  }

  public void setTrapTargets(List<TrapTarget> trapTargets) {
    this.trapTargets = trapTargets;
  }

  public TrapTargetGroupDto toDto() {
    List<TrapTargetDto> trapTargetDtos = new ArrayList<TrapTargetDto>();
    for (TrapTarget trapTarget: trapTargets) {
      trapTargetDtos.add(trapTarget.toDto());
    }

    TrapTargetGroupDto dto = new TrapTargetGroupDto();
    dto.setId(getId());
    dto.setName(name);
    dto.setTrapTargets(trapTargetDtos);
    return dto;
  }

}
