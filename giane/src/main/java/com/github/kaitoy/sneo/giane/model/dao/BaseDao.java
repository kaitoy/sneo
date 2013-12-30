/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
  _##
  _##########################################################################
 */

package com.github.kaitoy.sneo.giane.model.dao;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

public interface BaseDao<T> {

  public T findByKey(Integer id);

  public void create(T object) throws Exception;

  public void update(T object) throws Exception;

  public void update(List<T> objects) throws Exception;

  public void delete(T object) throws Exception;

  public void delete(List<T> objects) throws Exception;

  public List<T> findByCriteria(CriteriaQuery<T> cq);

  public CriteriaBuilder getCriteriaBuilder();

}
