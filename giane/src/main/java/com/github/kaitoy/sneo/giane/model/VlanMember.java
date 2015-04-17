/*_##########################################################################
  _##
  _##  Copyright (C) 2013-2015  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.github.kaitoy.sneo.network.dto.VlanMemberDto;
import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "VLAN_MEMBER")
public abstract class VlanMember extends AbstractModel implements FormMessage {

  /**
   *
   */
  private static final long serialVersionUID = 3962887048674714833L;

  private String name;
  private boolean trunk;
  private List<Vlan> vlans;

  @Column(name = "NAME", nullable = false, length = 200)
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
  @RegexFieldValidator(
    key = "RegexFieldValidator.error.objectName",
    // this field's value is/will be used for an MBean object name, and may be used in a command line.
    expression = "[^,=:\"*?]+",
    shortCircuit = true
  )
  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "TRUNK", nullable = false)
  public boolean isTrunk() {
    return trunk;
  }

  public void setTrunk(boolean trunk) {
    this.trunk = trunk;
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
