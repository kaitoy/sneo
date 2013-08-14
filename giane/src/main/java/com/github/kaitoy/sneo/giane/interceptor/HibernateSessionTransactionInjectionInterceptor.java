/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.interceptor;

import java.lang.reflect.Field;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kaitoy.sneo.giane.model.dao.hibernate.SessionTransactionInjectee;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class HibernateSessionTransactionInjectionInterceptor
extends AbstractInterceptor {

  /**
   *
   */
  private static final long serialVersionUID = -1130749708575128238L;

  private static final Logger logger
    = LoggerFactory.getLogger(
        HibernateSessionTransactionInjectionInterceptor.class
      );
  private static final SessionFactory sessionFactory;

  static {
    try {
      // Create the SessionFactory from standard config file. (i.e. hibernate.cfg.xml)
      Configuration configuration = new Configuration();
      configuration = configuration.configure();
      ServiceRegistry serviceRegistry
        = new ServiceRegistryBuilder()
            .applySettings(configuration.getProperties())
            .buildServiceRegistry();
      sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    } catch (Exception  e) {
      logger.error("Initial SessionFactory creation failed." + e);
      throw new ExceptionInInitializerError(e);
    }
  }

  @Override
  public String intercept(ActionInvocation invocation) throws Exception {
    Object action = invocation.getAction();
    Session session = null;
    Transaction tx = null;

    try {
      for (Class<?> cls = action.getClass(); cls != null; cls = cls.getSuperclass()) {
        if (cls.isAssignableFrom(ActionSupport.class)) {
          break;
        }

        for (Field field: cls.getDeclaredFields()) {
          field.setAccessible(true);
          Object fieldObj = field.get(action);
          if (!(fieldObj instanceof SessionTransactionInjectee)) {
            continue;
          }

          if (session == null) {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
          }

          SessionTransactionInjectee injectee
            = (SessionTransactionInjectee)fieldObj;
          injectee.setSession(session);
          injectee.setTransaction(tx);
        }
      }

      String result = invocation.invoke();

      if (
           tx != null
        && tx.isActive()
        && !tx.wasCommitted()
        && !tx.wasRolledBack()
      ) {
        tx.commit();
      }

      return result;
    } finally {
      if (session != null && session.isOpen()) {
        session.close();
      }
    }
  }

}
