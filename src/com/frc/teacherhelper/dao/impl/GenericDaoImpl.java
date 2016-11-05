package com.frc.teacherhelper.dao.impl;

/**
 * @author Administrator
 *
 */
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;

import com.frc.teacherhelper.dao.IGenericDao;



/**
 * GenericHibernateDao 继承 HibernateDao，简单封装 HibernateTemplate 各项功能，
 * 简化基于Hibernate Dao 的编写。
 * 
 */

public class GenericDaoImpl<T extends Serializable, PK extends Serializable>
		implements IGenericDao<T, PK> {
	private SessionFactory sessionFactory;
	
	@Resource
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}
	

	// 实体类类型(由构造方法自动赋值)
	private Class<T> entityClass;

	// 构造方法，根据实例类自动获取实体类类型
	public GenericDaoImpl() {
		this.entityClass = null;
		Class c = getClass();
		Type t = c.getGenericSuperclass();
		if (t instanceof ParameterizedType) {
			Type[] p = ((ParameterizedType) t).getActualTypeArguments();
			this.entityClass = (Class<T>) p[0];
		}
	}

	// -------------------- 基本检索、增加、修改、删除操作 --------------------

	

	
	// 根据主键获取实体。如果没有相应的实体，抛出异常。
	public T load(PK id) {
		return (T) getSession().get(entityClass, id);
//		return (T) getHibernateTemplate().load(entityClass, id);
	}

	

	// loadAllWithLock() ?

	// 更新实体
	public void update(T entity) {
		getSession().update(entity);
	}


	// 存储实体到数据库
	public void save(T entity) {
		Session session = sessionFactory.openSession();
		session.save(entity);
//		getHibernateTemplate().save(entity);
	}

	// saveWithLock()？

	// 删除指定的实体
	public void delete(T entity) {
		getSession().delete(entity);
	}

	// 根据主键删除指定实体
	public void deleteByKey(PK id) {
		this.delete(this.load(id));
	}


	// -------------------- HSQL ----------------------------------------------


	// 使用HSQL语句检索数据
	public List find(String queryString) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery(queryString);
		return query.list();
		
	}


	// -------------------------------- Criteria ------------------------------

	

	// public List<T> findPageCriteria(final DetachedCriteria
	// detachedCriteria,final int firstResult,final int maxResults){
	// return (List<T>) getHibernateTemplate().execute(new HibernateCallback() {
	// public Object doInHibernate(Session session) throws HibernateException {
	// Criteria criteria = detachedCriteria.getExecutableCriteria(session);
	// return criteria.list();
	// 　　}
	// 　},true);
	// }

	/** */
	/**
	 * 将联合查询的结果内容从Map或者Object[]转换为实体类型，如果没有转换必要则直接返回
	 */
	private List transformResults(List items) {
		if (items.size() > 0) {
			if (items.get(0) instanceof Map) {
				ArrayList list = new ArrayList(items.size());
				for (int i = 0; i < items.size(); i++) {
					Map map = (Map) items.get(i);
					list.add(map.get(CriteriaSpecification.ROOT_ALIAS));
				}
				return list;
			} else if (items.get(0) instanceof Object[]) {
				ArrayList list = new ArrayList(items.size());
				int pos = 0;
				for (int i = 0; i < ((Object[]) items.get(0)).length; i++) {
					if (((Object[]) items.get(0))[i].getClass() == this.entityClass) {
						pos = i;
						break;
					}
				}
				for (int i = 0; i < items.size(); i++) {
					list.add(((Object[]) items.get(i))[pos]);
				}
				return list;
			} else
				return items;
		} else
			return items;
	}


}