package com.frc.teacherhelper.dao;

import java.util.List;

import com.frc.teacherhelper.entity.Subject;

public interface ISubjectDao extends IGenericDao<Subject, Integer> {
	public Subject querySubject(String subjectName);
	public Subject querySubjectById(int id);
	public String addSubject(Subject subject);
	public String updateSubject(Subject subject);
}
