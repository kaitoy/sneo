/*_##########################################################################
  _##
  _##  Copyright (C) 2012  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.network.dto;

import java.io.Serializable;
import java.util.List;
import com.github.kaitoy.sneo.util.SneoVariableTextFormat;

public class SnmpAgentDto implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 8415645192596375541L;

  private Integer id;
  private String address;
  private Integer port;
  private String communityName;
  private String securityName;
  private String fileMibPath;
  private SneoVariableTextFormat fileMibFormat;
  private TrapTargetGroupDto trapTargetGroup;
  private List<String> communityStringIndexes;

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

  public SneoVariableTextFormat getFileMibFormat() {
    return fileMibFormat;
  }

  public void setFileMibFormat(SneoVariableTextFormat fileMibFormat) {
    this.fileMibFormat = fileMibFormat;
  }

  public TrapTargetGroupDto getTrapTargetGroup() {
    return trapTargetGroup;
  }

  public void setTrapTargetGroup(TrapTargetGroupDto trapTargetGroup) {
    this.trapTargetGroup = trapTargetGroup;
  }

  public List<String> getCommunityStringIndexes() {
    return communityStringIndexes;
  }

  public void setCommunityStringIndexes(List<String> communityStringIndexes) {
    this.communityStringIndexes = communityStringIndexes;
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
