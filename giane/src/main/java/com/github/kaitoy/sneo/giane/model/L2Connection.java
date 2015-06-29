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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.github.kaitoy.sneo.network.dto.L2ConnectionDto;
import com.github.kaitoy.sneo.network.dto.PhysicalNetworkInterfaceDto;
import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;

@Entity
@Table(name = "L2_CONNECTION")
public class L2Connection extends AbstractModel implements FormMessage {

  /**
   *
   */
  private static final long serialVersionUID = 2921402232740051680L;

  private String name;
  private List<PhysicalNetworkInterface> physicalNetworkInterfaces;
  private Network network;

  @Column(name = "NAME", nullable = false, length = 1000)
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
    maxLength = "1000",
    shortCircuit = true
  )
  @RegexFieldValidator(
    key = "RegexFieldValidator.error.objectName",
    // this field's value is/will be used for an MBean object name, and may be used in a command line.
    regex = "[^,=:\"*?]+",
    shortCircuit = true
  )
  public void setName(String name) {
    this.name = name;
  }

  @OneToMany(
    mappedBy = "l2Connection",
    fetch = FetchType.LAZY,
    orphanRemoval = false
  )
  public List<PhysicalNetworkInterface> getPhysicalNetworkInterfaces() {
    return physicalNetworkInterfaces;
  }

  public void setPhysicalNetworkInterfaces(
    List<PhysicalNetworkInterface> physicalNetworkInterfaces
   ) {
    this.physicalNetworkInterfaces = physicalNetworkInterfaces;
  }

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "NETWORK_ID", nullable = false)
  public Network getNetwork() {
    return network;
  }

  public void setNetwork(Network network) {
    this.network = network;
  }

  public L2ConnectionDto toDto() {
    List<PhysicalNetworkInterfaceDto> physicalNetworkInterfaceDtos
      = new ArrayList<PhysicalNetworkInterfaceDto>();
    for (
      PhysicalNetworkInterface physicalNetworkInterface:
        physicalNetworkInterfaces
    ) {
      physicalNetworkInterfaceDtos.add(physicalNetworkInterface.toDto());
    }

    L2ConnectionDto dto = new L2ConnectionDto();
    dto.setId(getId());
    dto.setName(name);
    dto.setPhysicalNetworkInterfaces(physicalNetworkInterfaceDtos);
    return dto;
  }

}
