/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.model.dao;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;

public interface BaseDao<T> {

  public T findByKey(Integer id);

  public void save(T object) throws Exception;

  public void update(T object) throws Exception;

  public void update(List<T> objects) throws Exception;

  public void delete(T object) throws Exception;

  public List<T> findByCriteria(DetachedCriteria criteria);

}
