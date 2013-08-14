/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import com.github.kaitoy.sneo.network.dto.IpAddressDto;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;

@Entity
@Table(name = "IP_ADDRESS")
public class IpAddress implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 4278317055245966206L;

  private Integer id;
  private String address;
  private Integer prefixLength;
  private IpAddressRelation ipAddressRelation;

  @Id
  @GeneratedValue(generator = "SequenceStyleGenerator")
  @GenericGenerator(
    name = "SequenceStyleGenerator",
    strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters = {
      @Parameter(name = "sequence_name", value = "IP_ADDRESS_SEQUENCE"),
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

  @Column(name = "ADDRESS", nullable = false)
  public String getAddress() {
    return address;
  }

  @RequiredStringValidator(
    key = "RequiredStringValidator.error",
    trim = true,
    shortCircuit = true // Stops checking if detects error
  )
  @RegexFieldValidator(
    key = "RegexFieldValidator.error",
    expression = "[0-9]{1,3}(\\.[0-9]{1,3}){3}",
    shortCircuit = true
  )
  public void setAddress(String address) {
    this.address = address;
  }

  @Column(name = "PREFIX_LENGTH", nullable = false)
  public Integer getPrefixLength() {
    return prefixLength;
  }

  @RequiredFieldValidator(
    key = "RequiredFieldValidator.error",
    shortCircuit = true
  )
  @IntRangeFieldValidator(
    key = "IntRangeFieldValidator.error.min.max",
    min = "1",
    max = "32",
    shortCircuit = true
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