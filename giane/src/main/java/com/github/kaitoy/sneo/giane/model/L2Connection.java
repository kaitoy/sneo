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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.github.kaitoy.sneo.network.dto.L2ConnectionDto;
import com.github.kaitoy.sneo.network.dto.PhysicalNetworkInterfaceDto;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;

@Entity
@Table(name = "L2_CONNECTION")
public class L2Connection implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -4801635716101021853L;

  private Integer id;
  private String name;
  private List<PhysicalNetworkInterface> physicalNetworkInterfaces;
  private Network network;

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

  @OneToMany(
    mappedBy = "l2Connection",
    fetch = FetchType.LAZY,
    cascade = {
      CascadeType.PERSIST,
      CascadeType.MERGE
    }
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
    dto.setId(id);
    dto.setName(name);
    dto.setPhysicalNetworkInterfaces(physicalNetworkInterfaceDtos);
    return dto;
  }

}
