/*_##########################################################################
  _##
  _##  Copyright (C) 2015  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.TableGenerator;

@MappedSuperclass
abstract class AbstractModel implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -7832723886208403827L;

  private Integer id;

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "giane_table_generator")
  @TableGenerator(
    name = "giane_table_generator",
    table = "GIANE_ID_GENERATOR",
    pkColumnName = "ENTITY_TABLE_NAME",
    initialValue = 1,
    allocationSize = 50
  )
  @Column(name = "ID")
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) { return true; }
    if (obj == null) { return false; }
    if (this.getClass() != obj.getClass()) { return false; }
    return this.id.equals(((AbstractModel)obj).id);
  }

  @Override
  public int hashCode() {
    return id;
  }

}
