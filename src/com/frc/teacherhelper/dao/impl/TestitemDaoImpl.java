package com.frc.teacherhelper.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.frc.teacherhelper.dao.ITestitemDao;
import com.frc.teacherhelper.entity.Testitem;

@Component("TestitemDao")
public class TestitemDaoImpl extends GenericDaoImpl<Testitem, Integer> implements ITestitemDao {
//	public Logger log = LoggerFactory.getLogger(TestitemDaoImpl.class);
	
	@Override
	public List<Testitem> queryTestitemsByParentId(int parentId) {
		String sql = String.format("from Testitem t where t.parentId=%d", parentId);
		List list = find(sql);
		return list;
	}

	@Override
	public String addTestitem(Testitem testitem) {
		save(testitem);
		return "OK";
	}

	@Override
	public String updateTestitem(Testitem testitem) {
		update(testitem);
		
		if (11 < 2) {
			Session session = getSession();
			String sql = String.format("update Testitem t set t.content='%s' where t.id=%d", testitem.getContent(), testitem.getId());
			Query query = session.createQuery(sql);
			query.executeUpdate();	
		}
		
		return "OK";
	}
	
	@Override
	public Testitem queryTestitemsByContent(String content) {
		String sql = String.format("from Testitem t where t.content like '%s'", content);
		List list = find(sql);
		if (list != null && list.size() == 1) {
			Testitem ti = (Testitem)list.get(0);
			return ti;
		}
		return null;
	}
	
	@Override
	public List<Testitem> queryByContent(String[] contents) {
		String hql = "from Testitem t where t.content in (:cl)";
		Query query = getSession().createQuery(hql);
		query.setParameterList("cl", contents);
		List<Testitem> rs = query.list();
		return rs;
	}

	@Override
	public List<Testitem> queryById(Integer[] ids) {
		String hql = "from Testitem t where t.id in (:cl)";
		Query query = getSession().createQuery(hql);
		query.setParameterList("cl", ids);
		List<Testitem> rs = query.list();
		return rs;
	}

	@Override
	public List<Testitem> queryTestitemsByGradeNSubject(String grade, int subjectid) {
		String hql = "from Testitem t where t.grade=:grade and t.subjectid=:sid";
		Query query = getSession().createQuery(hql);
		query.setParameter("grade", grade);
		query.setParameter("sid", subjectid);
		List<Testitem> rs = query.list();
		return rs;
	}
	@Override
	public Testitem queryTestitemsById(int id) {
		Testitem item = load(id);
		return item;
	}


}
