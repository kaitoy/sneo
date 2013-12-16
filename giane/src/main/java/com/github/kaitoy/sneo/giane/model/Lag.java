/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.github.kaitoy.sneo.network.dto.LagDto;
import com.github.kaitoy.sneo.network.dto.PhysicalNetworkInterfaceDto;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;

@Entity
@Table(name = "LAG")
public class Lag implements Serializable, FormMessage {

  /**
   *
   */
  private static final long serialVersionUID = -2959495249780946320L;

  private Integer id;
  private String name;
  private Integer channelGroupNumber;
  private LagIpAddressRelation ipAddressRelation;
  private List<PhysicalNetworkInterface> physicalNetworkInterfaces;
  private Node node;

  @Id
  @GeneratedValue(strategy=GenerationType.AUTO, generator="giane_seq_gen")
  @SequenceGenerator(name="giane_seq_gen", sequenceName="GIANE_SEQ")
  @Column(name = "ID")
  public Integer getId() { return id; }

  public void setId(Integer id) { this.id = id; }

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
    key = "RegexFieldValidator.error",
    // this field's value is/will be used for an MBean object name, and may be used in a command line.
    expression = "[^,=:\"*?]+",
    shortCircuit = true
  )
  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "CHANNEL_GROUP_NUMBER", nullable = false)
  public Integer getChannelGroupNumber() {
    return channelGroupNumber;
  }

  @RequiredFieldValidator(
    key = "RequiredFieldValidator.error",
    shortCircuit = true
  )
  @IntRangeFieldValidator(
    key = "IntRangeFieldValidator.error.min.max",
    min = "1",
    max = "4094",
    shortCircuit = true
  )
  public void setChannelGroupNumber(Integer channelGroupNumber) {
    this.channelGroupNumber = channelGroupNumber;
  }

  @OneToOne(
    fetch = FetchType.LAZY,
    orphanRemoval = true,
    cascade = {
      CascadeType.REMOVE
    },
    optional = false
  )
  @JoinColumn(
    name = "IP_ADDRESS_RELATION_ID",
    nullable = false,
    unique = true
  )
  public LagIpAddressRelation getIpAddressRelation() {
    return ipAddressRelation;
  }

  public void setIpAddressRelation(LagIpAddressRelation ipAddressRelation) {
    this.ipAddressRelation = ipAddressRelation;
  }

  @OneToMany(
    mappedBy = "lag",
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
  @JoinColumn(name = "NODE_ID", nullable = false)
  public Node getNode() {
    return node;
  }

  public void setNode(Node node) {
    this.node = node;
  }

  public LagDto toDto() {
    List<PhysicalNetworkInterfaceDto> pnifDtos
      = new ArrayList<PhysicalNetworkInterfaceDto>();
    for (PhysicalNetworkInterface pnif: physicalNetworkInterfaces) {
      pnifDtos.add(pnif.toDto());
    }

    LagDto dto = new LagDto();
    dto.setId(id);
    dto.setName(name);
    dto.setChannelGroupNumber(channelGroupNumber);
    dto.setIpAddresses(ipAddressRelation.getIpAddressDtos());
    dto.setPhysicalNetworkInterfaces(pnifDtos);
    return dto;
  }

}
