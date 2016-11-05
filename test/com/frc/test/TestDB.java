package com.frc.test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.frc.teacherhelper.dao.ISubjectDao;
import com.frc.teacherhelper.dao.ITestitemDao;
import com.frc.teacherhelper.entity.Subject;
import com.frc.teacherhelper.entity.Testitem;
import com.frc.teacherhelper.util.DateUtil;

public class TestDB {
	protected ApplicationContext ac = null;
	@Before
	public void before() {
		ac = new FileSystemXmlApplicationContext("resource/teacherHelperAppContext.xml");
		System.out.println(ac);
	}
	@Test
	public void querySubject() {
		ISubjectDao dao = (ISubjectDao) ac.getBean("SubjectDao");
		Subject sub = dao.querySubject("语文");
		System.out.println(sub.getSubjectid() + "," + sub.getSubjectname());
	}
	@Test
	public void testLoad() {
		ITestitemDao dao = (ITestitemDao) ac.getBean("TestitemDao");
		Testitem item = dao.load(1);
		System.out.println(item.getContent());
	}
	@Test
	public void query() {
		ITestitemDao dao = (ITestitemDao) ac.getBean("TestitemDao");
		String cts[] = new String[]{"鑰冪偣12", "123123"};
		List<Testitem> list = dao.queryByContent(cts);
		for (Testitem ti : list) {
			System.out.println(ti.getContent() + "," + ti.getId());
		}
	}
	@Test
	public void testUpdate() {
		ITestitemDao dao = (ITestitemDao) ac.getBean("TestitemDao");
		Testitem item = dao.queryTestitemsById(1);
		if (item == null) {
			System.out.println("item is null");
		} else {
			System.out.println(item.getContent() + "," + item.getLevel());
		}
		item.setContent("FirstContent");
		dao.updateTestitem(item);
	}
	@Test
	public void test() {
		ITestitemDao dao = (ITestitemDao) ac.getBean("TestitemDao");
		List list = dao.queryTestitemsByParentId(0);
		System.out.println(list.size());
		for (int i = 0; i < list.size(); i++) {
			Testitem ti = (Testitem)list.get(i);
			System.out.println(ti.getContent() + "," + ti.getLevel());
		}
		
		Testitem item = new Testitem();
		item.setContent("12A浣�3123");
		item.setLevel(1);
		item.setParentId(0);
		String dateTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss");
		item.setCreatetime(dateTime);
		item.setCreator("WF");
		item.setSeq("D1232");
		dao.addTestitem(item);
	}
}
