package com.frc.teacherhelper.entity;
// Generated 2016-8-4 23:17:06 by Hibernate Tools 3.5.0.Final

/**
 * Subject generated by hbm2java
 */
public class Subject implements java.io.Serializable {

	private int subjectid;
	private String subjectname;

	public Subject() {
	}

	public Subject(int subjectid) {
		this.subjectid = subjectid;
	}

	public Subject(int subjectid, String subjectname) {
		this.subjectid = subjectid;
		this.subjectname = subjectname;
	}

	public int getSubjectid() {
		return this.subjectid;
	}

	public void setSubjectid(int subjectid) {
		this.subjectid = subjectid;
	}

	public String getSubjectname() {
		return this.subjectname;
	}

	public void setSubjectname(String subjectname) {
		this.subjectname = subjectname;
	}

}
