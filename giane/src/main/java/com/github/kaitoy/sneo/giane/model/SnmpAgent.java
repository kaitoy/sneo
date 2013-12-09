/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.github.kaitoy.sneo.giane.action.message.FormMessage;
import com.github.kaitoy.sneo.network.dto.SnmpAgentDto;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;

@Entity
@Table(name = "SNMP_AGENT")
public class SnmpAgent implements Serializable, FormMessage {

  private static final long serialVersionUID = 5566464840276683510L;

  private Integer id;
  private String address;
  private Integer port;
  private String communityName;
  private String securityName;
  private String fileMibPath;
  private FileMibFormat fileMibFormat;
  private String communityStringIndexList;
  private Node node;

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
  @RegexFieldValidator(
    key = "RegexFieldValidator.error",
    expression = "[0-9]{1,3}(\\.[0-9]{1,3}){3}",
    shortCircuit = true
  )
  public void setAddress(String address) {
    this.address = address;
  }

  @Column(name = "PORT", nullable = false)
  public Integer getPort() {
    return port;
  }

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

  @Column(name = "COMMUNITY_NAME", nullable = false)
  public String getCommunityName() {
    return communityName;
  }

  @RequiredStringValidator(
    key = "RequiredStringValidator.error",
    trim = true,
    shortCircuit = true // Stops checking if detects error
  )
  public void setCommunityName(String communityName) {
    this.communityName = communityName;
  }

  @Column(name = "SECURITY_NAME", nullable = false)
  public String getSecurityName() {
    return securityName;
  }

  @RequiredStringValidator(
    key = "RequiredStringValidator.error",
    trim = true,
    shortCircuit = true // Stops checking if detects error
  )
  public void setSecurityName(String securityName) {
    this.securityName = securityName;
  }

  @Column(name = "FILEMIB_PATH", nullable = false)
  public String getFileMibPath() {
    return fileMibPath;
  }

  @RequiredStringValidator(
    key = "RequiredStringValidator.error",
    trim = true,
    shortCircuit = true // Stops checking if detects error
  )
  public void setFileMibPath(String fileMibPath) {
    this.fileMibPath = fileMibPath;
  }

  @Enumerated(EnumType.STRING)
  @Column(name = "FILEMIB_FORMAT", nullable = false)
  public FileMibFormat getFileMibFormat() {
    return fileMibFormat;
  }

  @RequiredFieldValidator(
    key = "RequiredFieldValidator.error",
    shortCircuit = true
  )
  public void setFileMibFormat(FileMibFormat format) {
    this.fileMibFormat = format;
  }

  @Column(name = "COMMUNITY_STRING_INDEX_LIST")
  public String getCommunityStringIndexList() {
    return communityStringIndexList;
  }

  @RegexFieldValidator(
    key = "RegexFieldValidator.error",
    expression = "([\\w]+(,[\\w]+)*)?",
    shortCircuit = true
  )
  public void setCommunityStringIndexList(String communityStringIndexList) {
    this.communityStringIndexList = communityStringIndexList;
  }

  @OneToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "NODE_ID", nullable = false)
  public Node getNode() {
    return node;
  }

  public void setNode(Node node) {
    this.node = node;
  }

  public SnmpAgentDto toDto() {
    List<String> communityStringIndexes = new ArrayList<String>();
    if (
         communityStringIndexList != null
      && communityStringIndexList.length() != 0
    ) {
      for (String communityStringIndex: communityStringIndexList.split(",")) {
        communityStringIndexes.add(communityStringIndex);
      }
    }

    SnmpAgentDto dto = new SnmpAgentDto();
    dto.setId(id);
    dto.setAddress(address);
    dto.setPort(port);
    dto.setCommunityName(communityName);
    dto.setSecurityName(securityName);
    dto.setFileMibPath(fileMibPath);
    dto.setFileMibFormat(fileMibFormat.getFormat());
    dto.setCommunityStringIndexes(communityStringIndexes);
    return dto;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) { return true; }
    if (!this.getClass().isInstance(obj)) { return false; }
    return this.id.equals(((SnmpAgent)obj).getId());
  }

  @Override
  public int hashCode() {
    return id;
  }

}
