/*_##########################################################################
  _##
  _##  Copyright (C) 2011  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.jmx;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import javax.management.DynamicMBean;

public final class DynamicMBeanFactory {

  private static final String DYNAMIC_MBEAN_SUFFIX = "DynamicMBean";

  private DynamicMBeanFactory() { throw new AssertionError(); }

  public static Class<?> getClassOfManagedBy(
    Class<? extends DynamicMBean> dynamicMBeanClass
  ) throws ClassNotFoundException {
    return  Class.forName(
              dynamicMBeanClass.getName()
                .replaceFirst(DYNAMIC_MBEAN_SUFFIX + "\\z", "")
            );
  }


  public static
  Class<? extends DynamicMBean> getClassOfMBeanThatManages(Class<?> moClass)
  throws ClassNotFoundException {
    Class<?> dynamicMBeanClass
      = Class.forName(
          moClass.getName() + DYNAMIC_MBEAN_SUFFIX
        );

    if (!DynamicMBean.class.isAssignableFrom(dynamicMBeanClass)) {
      throw new ClassNotFoundException(
        moClass.getName() + DYNAMIC_MBEAN_SUFFIX
          + " doesn't implement DynamicMBean."
      );
    }

    @SuppressWarnings("unchecked")
    Class<? extends DynamicMBean> clazz
      = (Class<? extends DynamicMBean>)dynamicMBeanClass;
    return clazz;
  }

  public static DynamicMBean newDynamicMBean(Object mo) {
    try {
      Constructor<? extends DynamicMBean> constructor
        = getClassOfMBeanThatManages(mo.getClass())
            .getConstructor(mo.getClass());

      DynamicMBean mbean
        = constructor.newInstance(mo.getClass().cast(mo));

      return mbean;
    } catch (ClassNotFoundException e) {
      throw new IllegalArgumentException(
              "Can not create Dynamic MBean of " + mo.getClass().getName(),
              e
            );
    } catch (SecurityException e) {
      throw new IllegalArgumentException(
              "Can not create Dynamic MBean of " + mo.getClass().getName(),
              e
            );
    } catch (NoSuchMethodException e) {
      throw new IllegalArgumentException(
              "Can not create Dynamic MBean of " + mo.getClass().getName(),
              e
            );
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(
              "Can not create Dynamic MBean of " + mo.getClass().getName(),
              e
            );
    } catch (InstantiationException e) {
      throw new IllegalArgumentException(
              "Can not create Dynamic MBean of " + mo.getClass().getName(),
              e
            );
    } catch (IllegalAccessException e) {
      throw new IllegalArgumentException(
              "Can not create Dynamic MBean of " + mo.getClass().getName(),
              e
            );
    } catch (InvocationTargetException e) {
      throw new IllegalArgumentException(
              "Can not create Dynamic MBean of " + mo.getClass().getName(),
              e
            );
    }
  }

}
