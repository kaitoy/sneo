/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.interceptor;

import java.lang.reflect.Field;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.kaitoy.sneo.giane.model.dao.impl.EntityManagerInjectee;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class EntityManagerInjectionInterceptor extends AbstractInterceptor {

  /**
   *
   */
  private static final long serialVersionUID = 4062045728313229561L;

  private static final Logger logger
    = LoggerFactory.getLogger(
        EntityManagerInjectionInterceptor.class
      );
  private static final EntityManagerFactory entityManagerFactory;

  static {
    try {
      entityManagerFactory
        = Persistence.createEntityManagerFactory("hibernate-h2-persistenceUnit");
    } catch (Exception  e) {
      logger.error("Initial EntityManagerFactory creation failed: " + e);
      throw new ExceptionInInitializerError(e);
    }
  }

  @Override
  public String intercept(ActionInvocation invocation) throws Exception {
    Object action = invocation.getAction();
    EntityManager em = null;
    EntityTransaction tx = null;

    try {
      for (Class<?> cls = action.getClass(); cls != null; cls = cls.getSuperclass()) {
        if (cls.isAssignableFrom(ActionSupport.class)) {
          break;
        }

        for (Field field: cls.getDeclaredFields()) {
          field.setAccessible(true);
          Object fieldObj = field.get(action);
          if (!(fieldObj instanceof EntityManagerInjectee)) {
            continue;
          }

          if (em == null) {
            em = entityManagerFactory.createEntityManager();
          }

          EntityManagerInjectee injectee = (EntityManagerInjectee)fieldObj;
          injectee.setEntityManager(em);
        }
      }

      if (em != null) {
        tx = em.getTransaction();
        tx.begin();
      }
      String result = invocation.invoke();
      if (tx != null && tx.isActive()) {
        tx.commit();
      }

      return result;
    } catch (Exception e) {
      if (tx != null && tx.isActive()) {
        tx.rollback();
      }
      throw e;
    } finally {
      if (em != null && em.isOpen()) {
        em.close();
      }
    }
  }

}
