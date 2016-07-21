package com.frc.teacherhelper.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.frc.teacherhelper.dao.ISubjectDao;
import com.frc.teacherhelper.dao.ITestitemDao;
import com.frc.teacherhelper.entity.Subject;
import com.frc.teacherhelper.entity.Testitem;
import com.frc.teacherhelper.module.TestitemBean;
import com.frc.teacherhelper.util.DateUtil;

@Service("TestItemService")
public class TestItemService {

	private ITestitemDao testitemDao;
	private ISubjectDao subjectDao;
	public String addTestitem(List<TestitemBean> list) {
		Map<String, Subject> subjectMap = new HashMap<>();
		boolean success = true;
		for (TestitemBean bean : list) {
			String subjectName = bean.getSubjectName();
			Subject subject = null;
			if (subjectMap.containsKey(subjectName)) {
				subject = subjectMap.get(subjectName);
			} else {
				subject = subjectDao.querySubject(subjectName);
				subjectMap.put(subjectName, subject);
			}
			if (bean.getContentL3() != null && !"".equals(bean.getContentL3())) {
				addTestitem(bean.getGrade(), subject.getSubjectid(), bean.getUnit(), bean.getContentL2(), bean.getContentL3());
			} else if (bean.getContentL2() != null && !"".equals(bean.getContentL2())) {
				addTestitem(bean.getGrade(), subject.getSubjectid(), bean.getUnit(), bean.getContentL1(), bean.getContentL2());
			} else if (bean.getContentL1() != null && !"".equals(bean.getContentL1())) {
				addTestitem(bean.getGrade(), subject.getSubjectid(), bean.getUnit(), null, bean.getContentL1());
			} else {
				success = false;
			}
		}
		return success ? "OK" : "FAIL";
	}
	public Testitem addTestitem(String grade, int subjectId, int unit, String parentContent, String content) {
		int parentId = 0;
		int level = 0;
		if (parentContent == null || parentContent.length() == 0) {
			parentId = 0;
			level = 1;
		} else {
			Testitem parentItem = testitemDao.queryTestitemsByContent(parentContent);
			if (parentItem == null) {
				return null;
			}
			level = parentItem.getLevel() + 1;
			parentId = parentItem.getId();
		}
		
		Testitem it = testitemDao.queryTestitemsByContent(content);
		if (it != null) {
			System.out.println("Already exist :" + content);
			return it;
		}

		String seq = ImportTestItemService.generateSEQ(grade, subjectId, level, parentId);
		Testitem item = new Testitem();
		item.setGrade(grade);
		item.setContent(content);
		item.setLevel(level);
		item.setParentId(parentId);
		item.setUnit(unit);
		item.setSubjectid(subjectId);
		item.setSeq(seq);
		item.setCreatetime(DateUtil.getDateTime());
		item.setCreator("SYS");

		addTestitem(item);
		return item;
	}
	public Testitem addTestitem(String grade, String subjectName, int unit, String parentContent, String content) {
		Subject subject = subjectDao.querySubject(subjectName);
		if (subject == null) {
			return null;
		}
		int subjectId = subject.getSubjectid();
		return addTestitem(grade, subjectId, unit, parentContent, content);
	}

	public List<Testitem> queryTestitemsByParentId(int parentId) {
		return testitemDao.queryTestitemsByParentId(parentId);
	}

	public List<TestitemBean> queryTestitemsByGradeNSubject(String grade, String subjectName) {
		Subject sub = subjectDao.querySubject(subjectName);
		List<Testitem> list = testitemDao.queryTestitemsByGradeNSubject(grade, sub.getSubjectid());
		List<TestitemBean> rs = convert(list);
		return rs;
	}

	public Testitem queryTestitemsById(int id) {
		return testitemDao.queryTestitemsById(id);
	}

	public String addTestitem(Testitem testitem) {
		return testitemDao.addTestitem(testitem);
	}

	public String updateTestitem(Testitem testitem) {
		return testitemDao.updateTestitem(testitem);
	}
	protected Testitem convert(TestitemBean bean) {
		Testitem rs = new Testitem();
		rs.setContent(bean.getContent());
		rs.setGrade(bean.getGrade());
		rs.setLevel(bean.getLevel());
		rs.setSubjectid(bean.getSubjectid());
		rs.setUnit(bean.getUnit());
		return rs;
	}
	protected List<TestitemBean> convert(List<Testitem> list) {
		List<TestitemBean> result = new ArrayList<>();
		Map<Integer, Testitem> map = new HashMap<>();
		List<Integer> idlist = null;
		Map<Integer, Boolean> tmpMap = new HashMap<>();
		Integer[] parentIdlist = null;
		List<Testitem> rs = null;

		// Level 1
		for (Testitem ti : list) {
			if (ti.getLevel() != 1 || map.containsKey(ti.getId())) {
				continue;
			}
			TestitemBean bean = new TestitemBean(ti);
			bean.setContentL1(ti.getContent());
			result.add(bean);
			map.put(ti.getId(), ti);
		}

		// Level 2
		idlist = new ArrayList<>();
		for (Testitem ti : list) {
			if (ti.getLevel() != 2 || map.containsKey(ti.getId())) {
				continue;
			}
			if (!tmpMap.containsKey(ti.getParentId())) {
				tmpMap.put(ti.getParentId(), true);
				idlist.add(ti.getParentId());
			}
		}
		parentIdlist = new Integer[idlist.size()];
		if (idlist.size() > 0) {
			for (int i = 0; i < idlist.size(); i++) {
				parentIdlist[i] = idlist.get(i);
			}
			rs = testitemDao.queryById(parentIdlist);
			for (Testitem ti : rs) {
				if (map.containsKey(ti.getId())) {
					continue;
				}
				map.put(ti.getId(), ti);				
			}
		}
		for (Testitem ti : list) {
			if (ti.getLevel() != 2 || map.containsKey(ti.getId())) {
				continue;
			}
			TestitemBean bean = new TestitemBean(ti);
			bean.setContentL2(ti.getContent());
			Testitem p = map.get(ti.getParentId());
			if (p != null) {
				bean.setContentL1(p.getContent());
			}
			result.add(bean);
			map.put(ti.getId(), ti);
		}
		

		// Level 3
		idlist = new ArrayList<>();
		for (Testitem ti : list) {
			if (ti.getLevel() != 3 || map.containsKey(ti.getId())) {
				continue;
			}
			if (!tmpMap.containsKey(ti.getParentId()) && !map.containsKey(ti.getParentId())) {
				tmpMap.put(ti.getParentId(), true);
				idlist.add(ti.getParentId());
			}
		}
		if (idlist.size() > 0) {
			parentIdlist = new Integer[idlist.size()];
			for (int i = 0; i < idlist.size(); i++) {
				parentIdlist[i] = idlist.get(i);
			}
			rs = testitemDao.queryById(parentIdlist);
			idlist = new ArrayList<>(); //level 1 
			for (Testitem ti : rs) {
				map.put(ti.getId(), ti);
				if (!tmpMap.containsKey(ti.getParentId()) && !map.containsKey(ti.getParentId())) {
					tmpMap.put(ti.getParentId(), true);
					idlist.add(ti.getParentId());
				}
			}
			if (idlist.size() > 0) {
				parentIdlist = new Integer[idlist.size()];
				for (int i = 0; i < idlist.size(); i++) {
					parentIdlist[i] = idlist.get(i);
				}
				rs = testitemDao.queryById(parentIdlist);
				for (Testitem ti : rs) {
					map.put(ti.getId(), ti);
				}
			}
		}
		for (Testitem ti : list) {
			if (ti.getLevel() != 3) {
				continue;
			}
			Testitem t2 = map.get(ti.getParentId());
			Testitem t1 = map.get(t2.getParentId());
			TestitemBean bean = new TestitemBean(ti);
			bean.setContentL1(t1.getContent());
			bean.setContentL2(t2.getContent());
			bean.setContentL3(ti.getContent());
			result.add(bean);
		}		

		return result;
	}

	protected List<Testitem> queryIn(List<String> idList) {
		return null;
	}

	@Autowired
	public void setTestitemDao(ITestitemDao testitemDao) {
		this.testitemDao = testitemDao;
	}

	@Autowired
	public void setSubjectDao(ISubjectDao subjectDao) {
		this.subjectDao = subjectDao;
	}

}
