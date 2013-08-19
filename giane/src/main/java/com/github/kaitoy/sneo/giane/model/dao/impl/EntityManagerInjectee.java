/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.impl;

import javax.persistence.EntityManager;

public interface EntityManagerInjectee {

  public void setEntityManager(EntityManager entityManager);

}
