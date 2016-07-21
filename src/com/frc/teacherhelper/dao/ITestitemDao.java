package com.frc.teacherhelper.dao;

import java.util.List;

import com.frc.teacherhelper.entity.Testitem;

public interface ITestitemDao extends IGenericDao<Testitem, Integer> {
	public List<Testitem> queryTestitemsByParentId(int parentId);
	public List<Testitem> queryTestitemsByGradeNSubject(String grade, int subjectid);
	public Testitem queryTestitemsById(int id);
	public Testitem queryTestitemsByContent(String content);
	public List<Testitem> queryByContent(String[] contents);
	public List<Testitem> queryById(Integer[] ids);
	public String addTestitem(Testitem testitem);
	public String updateTestitem(Testitem testitem);
}
