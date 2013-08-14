/*_##########################################################################
  _##
  _##  Copyright (C) 2012  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dto;

import java.io.Serializable;

import com.github.kaitoy.sneo.giane.model.FileMibFormat;
import com.github.kaitoy.sneo.giane.model.SnmpAgent;

public class SnmpAgentDto implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -5103147767597714532L;

  private Integer id;
  private String address;
  private Integer port;
  private String communityName;
  private String securityName;
  private String fileMibPath;
  private FileMibFormat fileMibFormat;
  private String communityStringIndexList;

  public SnmpAgentDto(SnmpAgent model) {
    this.id = model.getId();
    this.address = model.getAddress();
    this.port = model.getPort();
    this.communityName = model.getCommunityName();
    this.securityName = model.getSecurityName();
    this.fileMibPath = model.getFileMibPath();
    this.fileMibFormat = model.getFileMibFormat();
    this.communityStringIndexList = model.getCommunityStringIndexList();
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public String getCommunityName() {
    return communityName;
  }

  public void setCommunityName(String communityName) {
    this.communityName = communityName;
  }

  public String getSecurityName() {
    return securityName;
  }

  public void setSecurityName(String securityName) {
    this.securityName = securityName;
  }

  public String getFileMibPath() {
    return fileMibPath;
  }

  public void setFileMibPath(String fileMibPath) {
    this.fileMibPath = fileMibPath;
  }

  public FileMibFormat getFileMibFormat() {
    return fileMibFormat;
  }

  public void setFileMibFormat(FileMibFormat format) {
    this.fileMibFormat = format;
  }

  public String getCommunityStringIndexList() {
    return communityStringIndexList;
  }

  public void setCommunityStringIndexList(String communityStringIndexList) {
    this.communityStringIndexList = communityStringIndexList;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) { return true; }
    if (!this.getClass().isInstance(obj)) { return false; }
    return this.id == ((SnmpAgentDto)obj).getId();
  }

  @Override
  public int hashCode() {
    return id;
  }

}
