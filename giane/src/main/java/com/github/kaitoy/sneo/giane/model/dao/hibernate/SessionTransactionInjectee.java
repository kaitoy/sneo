/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;

public interface SessionTransactionInjectee {

  public void setSession(Session session);

  public void setTransaction(Transaction transaction);

}
