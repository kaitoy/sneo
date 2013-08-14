/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao.hibernate;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;

abstract class AbstractDao<T> implements SessionTransactionInjectee {

  private Session session;
  private Transaction transaction;

  public void setSession(Session session) {
    this.session = session;
  }

  protected Session getSession() {
    return session;
  }

  public void setTransaction(Transaction transaction) {
    this.transaction = transaction;
  }

  protected Transaction getTransaction() {
    return transaction;
  }

  public void save(T obj) throws Exception {
    try {
      getSession().save(obj);
    } catch (Exception e)  {
      transaction.rollback();
      throw e;
    }
  }

  public void update(T obj) throws Exception {
    try {
      getSession().update(obj);
    } catch (Exception e)  {
      transaction.rollback();
      throw e;
    }
  }

  public void update(List<T> objs) throws Exception {
    try {
      for (T obj: objs) {
        getSession().update(obj);
      }
    } catch (Exception e)  {
      transaction.rollback();
      throw e;
    }
  }

  public void delete(T obj) throws Exception {
    try {
      getSession().delete(obj);
    } catch (Exception e)  {
      transaction.rollback();
      throw e;
    }
  }

  @SuppressWarnings("unchecked")
  public List<T> findByCriteria(DetachedCriteria criteria) {
    return criteria.getExecutableCriteria(getSession()).list();
  }

}
