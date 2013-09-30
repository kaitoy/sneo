/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.github.kaitoy.sneo.network.dto.TrapTargetDto;
import com.github.kaitoy.sneo.network.dto.TrapTargetGroupDto;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;

@Entity
@Table(name = "TRAP_TARGET_GROUP")
public class TrapTargetGroup implements Serializable {

  private static final long serialVersionUID = -7283853777773516267L;

  private Integer id;
  private String name;
  private String descr;
  private List<TrapTarget> trapTargets;

  @Id
  @GeneratedValue(strategy=GenerationType.AUTO, generator="giane_seq_gen")
  @SequenceGenerator(name="giane_seq_gen", sequenceName="GIANE_SEQ")
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
    dto.setId(id);
    dto.setName(name);
    dto.setTrapTargets(trapTargetDtos);
    return dto;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) { return true; }
    if (!this.getClass().isInstance(obj)) { return false; }
    return this.getId() == ((TrapTargetGroup)obj).getId();
  }

  @Override
  public int hashCode() {
    return getId();
  }

}
