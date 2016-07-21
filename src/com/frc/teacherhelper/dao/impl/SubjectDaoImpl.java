package com.frc.teacherhelper.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.frc.teacherhelper.dao.ISubjectDao;
import com.frc.teacherhelper.entity.Subject;

@Component("SubjectDao")
public class SubjectDaoImpl extends GenericDaoImpl<Subject, Integer> implements ISubjectDao {


	@Override
	public Subject querySubject(String subjectName) {
		String sql = String.format("from Subject t where t.subjectname='%s'", subjectName);
		List list = find(sql);
		if (list != null && list.size() == 1) {
			Subject subject = (Subject)list.get(0);
			return subject;
		}
		return null;
	}

	@Override
	public Subject querySubjectById(int id) {
		Subject subject = load(id);
		return subject;
	}

	@Override
	public String addSubject(Subject subject) {
		save(subject);
		return "OK";
	}

	@Override
	public String updateSubject(Subject subject) {
		update(subject);
		return null;
	}

}
