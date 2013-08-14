/*_##########################################################################
  _##
  _##  Copyright (C) 2013  Kaito Yamada
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
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import com.github.kaitoy.sneo.network.dto.VlanMemberDto;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "VLAN_MEMBER")
public abstract class VlanMember implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1972869548303637586L;

  private Integer id;
  private String name;
  private List<Vlan> vlans;

  @Id
  @GeneratedValue(generator = "SequenceStyleGenerator")
  @GenericGenerator(
    name = "SequenceStyleGenerator",
    strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters = {
      @Parameter(name = "sequence_name", value = "VLAN_MEMBER_SEQUENCE"),
      @Parameter(name = "initial_value", value = "1"),
      @Parameter(name = "increment_size", value = "1")
    }
  )
  @Column(name = "ID")
  public Integer getId() { return id; }

  public void setId(Integer id) { this.id = id; }

  @Column(name = "NAME", nullable = false, length = 50)
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

  @ManyToMany(
    mappedBy = "vlanMembers",
    fetch = FetchType.LAZY,
    cascade = {}
  )
  public List<Vlan> getVlans() {
    return vlans;
  }

  public void setVlans(List<Vlan> vlans) {
    this.vlans = vlans;
  }

  public abstract VlanMemberDto toDto();

}
