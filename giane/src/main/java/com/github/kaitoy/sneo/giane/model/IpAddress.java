/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.github.kaitoy.sneo.network.dto.IpAddressDto;
import com.opensymphony.xwork2.validator.annotations.ConversionErrorFieldValidator;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;

@Entity
@Table(name = "IP_ADDRESS")
public class IpAddress implements Serializable, FormMessage {

  /**
   *
   */
  private static final long serialVersionUID = 4278317055245966206L;

  private Integer id;
  private String address;
  private Integer prefixLength;
  private IpAddressRelation ipAddressRelation;

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

  @Column(name = "PREFIX_LENGTH", nullable = false)
  public Integer getPrefixLength() {
    return prefixLength;
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
    min = "0",
    max = "128",
    shortCircuit = true
  )
  @CustomValidator(
    key = "IpAddressPrefixLengthValidator.error",
    type = "ipaddressprefixlength"
  )
  public void setPrefixLength(Integer prefixLength) {
    this.prefixLength = prefixLength;
  }

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "IP_ADDRESS_RELATION_ID", nullable = false)
  public IpAddressRelation getIpAddressRelation() {
    return ipAddressRelation;
  }

  public void setIpAddressRelation(IpAddressRelation ipAddressRelation) {
    this.ipAddressRelation = ipAddressRelation;
  }

  public IpAddressDto toDto() {
    IpAddressDto dto = new IpAddressDto();
    dto.setId(id);
    dto.setAddress(address);
    dto.setPrefixLength(prefixLength);
    return dto;
  }

}
