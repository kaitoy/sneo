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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.github.kaitoy.sneo.network.dto.IpAddressDto;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
  name = "IP_ADDRESS_HOST",
  discriminatorType = DiscriminatorType.STRING,
  length = 50
)
@Table(name = "IP_ADDRESS_RELATION")
public abstract class IpAddressRelation implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 5505521658885012585L;

  private Integer id;
  private List<IpAddress> ipAddresses;

  @Id
  @GeneratedValue(strategy=GenerationType.AUTO, generator="giane_seq_gen")
  @SequenceGenerator(name="giane_seq_gen", sequenceName="GIANE_SEQ")
  @Column(name = "ID")
  public Integer getId() { return id; }

  public void setId(Integer id) { this.id = id; }

  @OneToMany(
    mappedBy = "ipAddressRelation",
    fetch = FetchType.LAZY,
    orphanRemoval = true,
    cascade = {
      CascadeType.REMOVE
    }
  )
  public List<IpAddress> getIpAddresses() { return ipAddresses; }

  public void setIpAddresses(List<IpAddress> ipAddresses) {
    this.ipAddresses = ipAddresses;
  }

  @Transient
  public List<IpAddressDto> getIpAddressDtos() {
    List<IpAddressDto> ipAddressDtos = new ArrayList<IpAddressDto>();
    for (IpAddress ipAddress: ipAddresses) {
      ipAddressDtos.add(ipAddress.toDto());
    }
    return ipAddressDtos;
  }

}
